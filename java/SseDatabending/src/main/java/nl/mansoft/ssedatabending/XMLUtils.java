/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.mansoft.ssedatabending;

import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;

/**
 *
 * @author manson
 */
public class XMLUtils {
    private Transformer trans;
    private DocumentBuilder docBuilder;
    private static XMLUtils singleton;

    public XMLUtils() {
        try {
            //set up a transformer
            TransformerFactory transfac = TransformerFactory.newInstance();
            trans = transfac.newTransformer();
            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            trans.setOutputProperty(OutputKeys.INDENT, "yes");
            DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
            docBuilder = dbfac.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(XMLUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    private String documentToStringInternal(Document doc) 
    {
        String xmlString = null;
        try {
            /////////////////
            //Output the XML

            //create string from xml tree
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(doc);
            trans.transform(source, result);
            xmlString = sw.toString();
        } catch (TransformerException ex) {
            Logger.getLogger(XMLUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return xmlString;
    }
    
    private Document createDocumentInternal()
    {
        return docBuilder.newDocument();
    }
    
    private static void createSingleton()
    {
        if (singleton == null)
        {
            singleton = new XMLUtils();
        }
    }
    
    public static String documentToString(Document doc)
    {
        createSingleton();
        return singleton.documentToStringInternal(doc);
    }
    
    public static Document createDocument()
    {
        createSingleton();
        return singleton.createDocumentInternal();
    }
}
