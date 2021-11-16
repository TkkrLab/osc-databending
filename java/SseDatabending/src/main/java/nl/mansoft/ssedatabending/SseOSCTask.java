/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.mansoft.ssedatabending;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortIn;
import java.net.SocketException;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import nl.mansoft.sseserver.SseEvent;
import nl.mansoft.sseserver.SseTask;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author manson
 */
public class SseOSCTask implements SseTask
{
    private OSCPortIn receiver;
    private ServletContext servletContext;
    private Map<String, OSCInputDevice> inputDevices;
    private Map<String, OSCDevice> outputDevices;
    private Queue<SseEvent> queue;
    private DocumentBuilder docBuilder;
    private Map<OSCDeviceParameter, Set<OSCDeviceParameter>> links;
    
    private static String addressToName(String address)
    {
        int pos = address.indexOf('/', 1);
        return address.substring(1, pos);
    }
    
    @Override
    public void init(ServletConfig config, final Queue<SseEvent> queue) {
        this.queue = queue;
        try {
//            System.out.println("SseOSCTask::init");
            receiver = new OSCPortIn(10000);
            servletContext = config.getServletContext();
            servletContext.setAttribute("SseOSCTask", this);
            inputDevices = new LinkedHashMap<String, OSCInputDevice>();
            outputDevices = new LinkedHashMap<String, OSCDevice>();
            
            links = new LinkedHashMap<OSCDeviceParameter, Set<OSCDeviceParameter>>();
            
            OSCListener listener = new OSCListener() {
                @Override
                public void acceptMessage(java.util.Date time, OSCMessage message) {
                    String address = message.getAddress();
                    String name = addressToName(address);
//                    Logger.getLogger(SseOSCTask.class.getName()).log(Level.SEVERE,"address = {0}", address);
//                    System.out.println(address);
//                    System.out.println(name);
                    if (address.endsWith("/outputs"))
                    {
                        if (!outputDevices.containsKey(name))
                        {
                            Object[] args = message.getArguments();
                            String[] outputs = args[0].toString().split(" *, *");
                            outputDevices.put(name, new OSCDevice(outputs, message.getInetAddress()));
                            queue.offer(new SseEvent(name, "AddedOutputDevice"));
                        }
                    }
                    else if (address.endsWith("/inputs"))
                    {
                        if (!inputDevices.containsKey(name))
                        {
                            Object[] args = message.getArguments();
                            String[] inputs = args[0].toString().split(" *, *");
                            inputDevices.put(name, new OSCInputDevice(inputs, message.getInetAddress()));
                            queue.offer(new SseEvent(name, "AddedInputDevice"));
                        }
                    }
                    else if (address.endsWith("/port"))
                    {
                        Object[] args = message.getArguments();
                        OSCInputDevice inputDevice = inputDevices.get(name);
                        if (inputDevice != null)
                            inputDevice.setPort((Integer)args[0]);
                    }
                    else 
                    {
                        OSCDevice outputDevice = outputDevices.get(name);
                        if (outputDevice != null && message.getInetAddress().equals(outputDevice.getInetAddress()))
                        {
                            int pos = address.lastIndexOf('/');
                            String paramname = address.substring(pos + 1);
                            Object args[] = message.getArguments();
                            Float value = (Float)args[0];
                            outputDevice.setValue(paramname, value);
                            OSCDeviceParameter outputDeviceParameter = new OSCDeviceParameter(name, paramname);
                            Set<OSCDeviceParameter> outputlinks = links.get(outputDeviceParameter);
                            if (outputlinks != null)
                            {
                                for (OSCDeviceParameter inputDeviceParameter : outputlinks) {
                                    OSCInputDevice inputDevice = inputDevices.get(inputDeviceParameter.getDevice());
                                    if (inputDevice != null)
                                    {
                                        SendOSC.sendOSC(SseOSCTask.this, inputDeviceParameter.getDevice(), inputDeviceParameter.getParameter(), value);
                                    }
                                }
                            }
                            String[] data = { name, paramname, value.toString() };
                            queue.offer(new SseEvent(data, "change", null));
                        }
                    }
                }
            };
            receiver.addListener("/.*/out/.*", listener);
            receiver.startListening();
        } catch (SocketException ex) {
            Logger.getLogger(SseOSCTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void destroy() {
//        System.out.println("SseOSCTask::destroy");
        receiver.stopListening();
        receiver.close();
        servletContext.removeAttribute("SseOSCTask");
    }
    
    public Map<String, OSCInputDevice> getInputDevices()
    {
        return inputDevices;
    }

    public Map<String, OSCDevice> getOutputDevices()
    {
        return outputDevices;
    }
    
    public Queue<SseEvent> getQueue()
    {
        return queue;
    }
    
    private Document getDevicesXML(String type, Map<String, ? extends OSCDevice> devices)
    {
        Document doc = null;
        try {
            if (docBuilder == null)
            {
                DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
                docBuilder = dbfac.newDocumentBuilder();
            }
            doc = docBuilder.newDocument();
            Element devicesElement = doc.createElement("devices");
            devicesElement.setAttribute("type", type);
            for (Map.Entry<String, ? extends OSCDevice> entry : devices.entrySet())
            {
                String name = entry.getKey();
                Element device = doc.createElement("device");
                device.setAttribute("name", name);
//                OSCDevice inputDevice = entry.getValue();
//                devicesElement.appendChild(inputDevice.toXML(doc, name));
                devicesElement.appendChild(device);
            }
            doc.appendChild(devicesElement);
        } catch (Exception e) {
//            System.out.println(e);
        }
        return doc;
    }
    
    public Document getInputDevicesXML()
    {
        return getDevicesXML("input", inputDevices);
    }
    
    public Document getOutputDevicesXML()
    {
        return getDevicesXML("output", outputDevices);
    }
    
    public Map<OSCDeviceParameter, Set<OSCDeviceParameter>> getLinks()
    {
        return links;
    }
    
    public String linksToXML()
    {
        String xmlString = null;
        Document doc = XMLUtils.createDocument();
        Element linksElement = doc.createElement("links");
        for (Map.Entry<OSCDeviceParameter, Set<OSCDeviceParameter>> entry : links.entrySet()) {
            OSCDeviceParameter outputDeviceParameter = entry.getKey();
            String outputDeviceName = outputDeviceParameter.getDevice();
            String outputParameterName = outputDeviceParameter.getParameter();
            for (OSCDeviceParameter inputDeviceParameter : entry.getValue())
            {
                Element link = doc.createElement("link");
                link.setAttribute("outputdevice", outputDeviceName);
                link.setAttribute("outputparameter", outputParameterName);
                link.setAttribute("inputdevice", inputDeviceParameter.getDevice());
                link.setAttribute("inputparameter", inputDeviceParameter.getParameter());
                linksElement.appendChild(link);
            }
        }

        ////////////////////////
        //Creating the XML tree
        doc.appendChild(linksElement);

        /////////////////
        //Output the XML
        xmlString = XMLUtils.documentToString(doc);
        return xmlString;
    }
}
