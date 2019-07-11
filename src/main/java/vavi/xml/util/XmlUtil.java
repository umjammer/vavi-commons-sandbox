/*
 * Copyright (c) 2009 by KLab Inc., All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.xml.util;

import java.util.Properties;

import javax.imageio.metadata.IIOMetadataNode;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


/**
 * XmlUtil. TODO IIOMetadataNode やんか、もっと汎用に
 *
 * @author <a href="mailto:sano-n@klab.jp">Naohide Sano</a> (nsano)
 * @version 0.00 2009/05/18 nsano initial version <br>
 */
public class XmlUtil {

    /** */
    private XmlUtil() {
    }

    /** */
    public static void printNode(String title, IIOMetadataNode metadataNode) {
        try {
System.err.println("---- " + title + " ----");
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            DOMSource source = new DOMSource(metadataNode);
            StreamResult result = new StreamResult(System.err);
            Properties props = new Properties();
            props.setProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperties(props);
            transformer.transform(source, result);
        } catch (Exception e) {
            // TODO check error policy
            e.printStackTrace(System.err);
        }
    }

    /** */
    public static IIOMetadataNode getNode(IIOMetadataNode rootNode, String nodeName) {
        for (int i = 0; i < rootNode.getLength(); i++) {
            if (rootNode.item(i).getNodeName().compareToIgnoreCase(nodeName) == 0) {
                return ((IIOMetadataNode) rootNode.item(i));
            }
        }
        IIOMetadataNode node = new IIOMetadataNode(nodeName);
        rootNode.appendChild(node);
        return node;
    }
}

/* */
