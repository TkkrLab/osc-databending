import re
from xml.etree import ElementTree
from twisted.internet.protocol import DatagramProtocol
from twisted.internet import reactor
from twisted.web.resource import Resource
from twisted.web.server import Site
from twisted.web.static import File
from pythonosc.osc_message import OscMessage
from pythonosc.osc_message_builder import OscMessageBuilder

from sseresource3 import SseResource


class OSCDevice(object):
    def __init__(self, name, paramstr, inetaddress):
        # devicenaam
        self.name = name
        # parameter string splitsen en parameters gebruiken als key voor de variabele self.params
        paramstrarr = paramstr.split(',')
        self.params = dict()
        # beginwaarde van parameters is 0
        for param in paramstrarr:
            self.params[param] = 0
        # IP adres van device
        self.inetaddress = inetaddress

    def toXML(self):
        # device status renderen als XML document, standaard type is 'output'
        root = ElementTree.Element("device", name = self.name, ip = self.inetaddress, type = "output")
        parameters = ElementTree.SubElement(root, "parameters")
        for key, value in self.params.items():
            ElementTree.SubElement(parameters, "parameter", name = key, value = str(value))
        return root

class OSCInputDevice(OSCDevice, DatagramProtocol):
    def __init__(self, name, paramstr, inetaddress):
        OSCDevice.__init__(self, name, paramstr, inetaddress)
        self.port = 7000

    def toXML(self):
        # device status renderen als XML document type is nu 'input'
        root = super(OSCInputDevice, self).toXML()
        root.set("type", "input")
        return root

    def startProtocol(self):
        # wordt aangeroepen door reactor.listenUDP()
        # UDP verbinding 'connecten'
        self.transport.connect(self.inetaddress, self.port)
        print("now we can only send to host %s port %d" % (self.inetaddress, self.port))

    def send(self, oscmsg):
#        print "send: " + str(oscmsg)
        if self.transport == None:
            # bij eerste keer zenden het protocol starten
            reactor.listenUDP(0, self)
        # OSC bericht encoderen en versturen           
        self.transport.write(oscmsg.dgram) # no need for address

    def setPort(self, port):
        self.port = port

# handler voor binnenkomende UDP berichten met het 'heinze' protocol
class OSCReceiver(DatagramProtocol):
    def datagramReceived(self, data, addr):
#        print "received %r from %s:%d" % (data, host, port)
#        self.transport.write(data, (host, port))

        # OSC bericht decoderen en opslaan in lokale variabelen
        # bv. /csound-drone/out/inputs ,s distortfactor,harmonic,disttable,feedback
        #   address  = 'csound-drone'
        #   typetags = 's'
        #   values   = [ 'distortfactor,harmonic,disttable,feedback' ]
        host = addr[0]
        port = addr[1]
        decoded = OscMessage(data)
        address = decoded.address
        value = decoded.params[0]
        m = re.match(r"/(.+)/(.+)/(.+)", address)
        if m:
            inout = m.group(2)
            if inout == "out":
                name = m.group(1)
                param = m.group(3)
                if type(value) is str:
                    # nieuw datadirigent apparaat
                    print("received from %s:%d" % addr)
                    if param == "outputs":
                        oscDevice = OSCDevice(name, value, host)
                        outputDevices[name] = oscDevice
                        print(ElementTree.tostring(oscDevice.toXML()))
                        sse.write(event = 'AddedOutputDevice', data = [ name ])
                    elif param == "inputs":
                        oscDevice = OSCInputDevice(name, value, host)
                        inputDevices[name] = oscDevice
                        print(ElementTree.tostring(oscDevice.toXML()))
                        sse.write(event = 'AddedInputDevice', data = [ name ])
                elif type(value) is int:
                    # voor invoerapparaten: standaard poort (7000) wijzigen NA opgeven parameters (zie hiervoor)
                    # bv. /csound-drone/out/port ,i 7001
                    if param == "port":
                        listenport = int(value)
                        print("port is %d" % listenport)
                        if name in inputDevices:
                            inputDevice = inputDevices[name]
                            inputDevice.setPort(listenport)
                elif type(value) is float:
                    # wijziging van een parameter van een uitvoerapparaat
                    if name in outputDevices:
                        outputDevice = outputDevices[name]
                        if param in outputDevice.params:
                            # waarde aanpassen in uitvoerapparaat
                            outputDevice.params[param] = value
                            # doorgeven naar browsers via SSE
                            sse.write(event = 'change', data = [ name, param, value ])
                            # gelinkte invoerapparaten updaten
                            outdevpar = (name, param)
                            if outdevpar in links:
                                outputlinks = links[outdevpar]
                                for outputlink in outputlinks:
                                    inputname = outputlink[0]
                                    inputparam = outputlink[1]
                                    # wijziging ook doorgeven aan browsers
                                    sse.write(event = "sliderchange", data = [ inputname, inputparam, value ])
                                    # OSC bericht versturen naar invoerapparaat
                                    builder = OscMessageBuilder("/" + inputname + "/in/" + inputparam)
                                    builder.add_arg(value)
                                    msg = builder.build()
                                    inputdevice = inputDevices[inputname]
                                    inputdevice.send(msg)
                                    
# http://localhost:8811/Devices?type=output                
# http://localhost:8811/Devices?type=input
# in- of uitvoerapparaat lijst opvragen, resultaat is XML
class Devices(Resource):
    isLeaf = True
    def render_GET(self, request):
        args = request.args
        print(args)
        request.setHeader("Content-Type", "text/xml;charset=UTF-8")
        typ = args[b"type"][0].decode() if b"type" in args else "input"
        root = ElementTree.Element("devices", type = typ)
        if typ == "output":
            for oscDevice in outputDevices:
                print(oscDevice)
                ElementTree.SubElement(root, "device", name = oscDevice)
        elif typ == "input":
            for oscDevice in inputDevices:
                print(oscDevice)
                ElementTree.SubElement(root, "device", name = oscDevice)
        return ElementTree.tostring(root)

# http://localhost:8811/Device?type=output&name=channels                
# http://localhost:8811/Device?type=input&name=csound
# in- of uitvoerapparaat opvragen, resultaat is XML
class Device(Resource):
    isLeaf = True
    def render_GET(self, request):
        args = request.args
        print(args)
        request.setHeader("Content-Type", "text/xml;charset=UTF-8")
        if b"name" in args and b"type" in args:
            name = args[b"name"][0].decode()
            typ = args[b"type"][0].decode()
            print(name)
            print(typ)
            if b"action" in args and args[b"action"][0] == b"delete":
                if typ == "output":
                    if name in outputDevices:
                        sse.write(event = 'DeletedOutputDevice', data = [ name ])
                        del outputDevices[name]
                elif typ == "input":
                    if name in inputDevices:
                        sse.write(event = 'DeletedInputDevice', data = [ name ])
                        del inputDevices[name]
            else:                
                if typ == "output":
                    if name in outputDevices:
                        outputDevice = outputDevices[name]
                        return ElementTree.tostring(outputDevice.toXML())
                elif typ == "input":
                    if name in inputDevices:
                        inputDevice = inputDevices[name]
                        return ElementTree.tostring(inputDevice.toXML())
        return b""
# wordt via ajax aangeroepen bij het schuiven van een slider van een invoerapparaat
# zodat de slider van de andere browsers word geupdated
# http://localhost:8811/SendOSC?address=127.0.0.1&name=csound-drone&param=distortfactor&value=0.008 
class SendOSC(Resource):
    isLeaf = True
    def render_GET(self, request):
        args = request.args
        # print(args)
        if b"name" in args and b"param" in args and b"value" in args:
            name = args[b"name"][0].decode()
            param = args[b"param"][0].decode()
            value = float(args[b"value"][0].decode())
            # sliderchange event doorgeven aan browsers
            sse.write(event = "sliderchange", data = [ name, param, value ])
            # OSC bericht sturen naar het invoerapparaat
            if name in inputDevices:
                inputDevice = inputDevices[name]
                if param in inputDevice.params:
                    inputDevice.params[param] = value
                    builder = OscMessageBuilder("/" + name + "/in/" + param)
                    builder.add_arg(value)
                    msg = builder.build()
#                    print msg
                    inputDevice.send(msg)
        return b""
# link creeren:
# http://localhost:8811/OSCLink?outputdevice=channels&outputparameter=ch1&inputdevice=csound-drone&inputparameter=distortfactor
# link verwijderen:
# http://localhost:8811/OSCLink?outputdevice=channels&outputparameter=ch1&inputdevice=csound-drone&inputparameter=distortfactor&action=delete
# links opvragen:
# http://localhost:8811/OSCLink

class OSCLink(Resource):
    isLeaf = True
    def render_GET(self, request):
        args = request.args
        print(args)
        if b"outputdevice" in args and b"outputparameter" in args and b"inputdevice" in args and b"inputparameter" in args:
            outputdevice = args[b"outputdevice"][0].decode()
            outputparameter = args[b"outputparameter"][0].decode()
            inputdevice = args[b"inputdevice"][0].decode()
            inputparameter = args[b"inputparameter"][0].decode()
            outdevpar = (outputdevice, outputparameter)
            indevpar = (inputdevice, inputparameter)
            if b"action" in args and args[b"action"][0] == b"delete":
                sse.write(event = "DeletedLink", data = [ outputdevice, outputparameter, inputdevice, inputparameter ])
                links[outdevpar].discard(indevpar)
            else:
                if not outdevpar in links:
                    links[outdevpar] = set()
                # interessante vraag die niet interessant gevonden wordt:
                # http://stackoverflow.com/questions/4221968/any-reason-there-are-no-returned-value-from-set-add
                if not indevpar in links[outdevpar]:
                    links[outdevpar].add(indevpar)
                    sse.write(event = "AddedLink", data = [ outputdevice, outputparameter, inputdevice, inputparameter ])
        else:
            request.setHeader("Content-Type", "text/xml;charset=UTF-8")
            root = ElementTree.Element("links")
            for outkey, outvalue in links.items():
                for inputvalue in outvalue:
                    ElementTree.SubElement(root, "link", outputdevice = outkey[0], outputparameter = outkey[1], inputdevice = inputvalue[0], inputparameter = inputvalue[1])
            return ElementTree.tostring(root)                
        return b""

outputDevices = dict()
inputDevices = dict()
links = dict()
oscreceiver = OSCReceiver()
# UDP poort 9999
reactor.listenUDP(9999, oscreceiver)
root = Resource()
sse = SseResource()
# urls koppelen aan Resource's
root.putChild(b"sse", sse)
root.putChild(b"page", File("osc.html"))
root.putChild(b"Devices", Devices())
root.putChild(b"Device", Device())
root.putChild(b"SendOSC", SendOSC())
root.putChild(b"OSCLink", OSCLink())
factory = Site(root)
# server draait op poort 8811
reactor.listenTCP(8811, factory)

reactor.run()
