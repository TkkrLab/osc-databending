/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.mansoft.ssedatabending;

import com.illposed.osc.OSCPortOut;
import java.net.InetAddress;
import java.net.SocketException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author manson
 */
public class OSCInputDevice extends OSCDevice {
    private OSCPortOut oscPortOut;
    private int port;
    
    public OSCInputDevice(String[] params, InetAddress inetAddress) {
        super(params, inetAddress);
        port = 7000;
    }
    
    @Override
    public Element toXML(Document doc, String name)
    {
        Element device = super.toXML(doc, name);
        device.setAttribute("port", String.valueOf(port));
        return device;
    }
        
    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public OSCPortOut getOSCPortOut() throws SocketException
    {
        if (oscPortOut == null)
        {
            oscPortOut = new OSCPortOut(inetAddress, port);
        }
        return oscPortOut;
    }    
    
    @Override
    public void close()
    {
        if (oscPortOut != null)
        {
            oscPortOut.close();
            oscPortOut = null;
        }
    }
}
