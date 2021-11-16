/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.mansoft.ssedatabending;

import java.net.InetAddress;
import java.util.Map;
import java.util.LinkedHashMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author manson
 */
public class OSCDevice {
    protected InetAddress inetAddress;
    protected Map<String, Float> parameters;
    
    public OSCDevice(String[] params, InetAddress inetAddress) {
        this.inetAddress = inetAddress;
        parameters = new LinkedHashMap<String, Float>();
        for (int i = 0; i < params.length; i++) {
            parameters.put(params[i], 0f);
        }
    }

    public InetAddress getInetAddress()
    {
        return inetAddress;
    }
    
    public void setValue(String parameter, float value) {
        parameters.put(parameter, value);
    }

    public float getValue(String parameter) {
        return parameters.get(parameter).floatValue();
    }

    public Map<String, Float> getParameters() {
        return parameters;
    }

    public Element toXML(Document doc, String name) {
        //create the root element and add it to the document
        Element device = doc.createElement("device");        
        device.setAttribute("name", name);
        device.setAttribute("ip", inetAddress.getHostAddress());

        //create a comment and put it in the root element
        //Comment comment = doc.createComment("Just a thought");
        //root.appendChild(comment);

        //create child element, add an attribute, and add to root
        Element parametersElement = doc.createElement("parameters");
        for (Map.Entry<String, Float> entry : parameters.entrySet()) {
            Element parameterElement = doc.createElement("parameter");
            parameterElement.setAttribute("name", entry.getKey());
            parameterElement.setAttribute("value", String.valueOf(entry.getValue()));
            parametersElement.appendChild(parameterElement);
        }
        device.appendChild(parametersElement);
        return device;
    }

    public String createXML(String name) {
        String xmlString = null;
        //Creating an empty XML Document

        Document doc = XMLUtils.createDocument();

        ////////////////////////
        //Creating the XML tree
        doc.appendChild(toXML(doc, name));

        /////////////////
        //Output the XML
        xmlString = XMLUtils.documentToString(doc);
        return xmlString;
    }
    
    public void close()
    {
    }
}
