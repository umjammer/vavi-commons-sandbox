/*
 * Copyright (c) 2019 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.util.dynamicmorphism;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static vavix.util.dynamicmorphism.NodeListToIterable.morpher;


/**
 * MorphableTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2019/06/09 umjammer initial version <br>
 */
class MorphableTest {

    @Test
    void test() throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(new File("pom.xml"));
        XPathFactory xpf = XPathFactory.newInstance();
        XPath xPath = xpf.newXPath();
        NodeList nodeList = (NodeList) xPath.evaluate("/project/dependencies/dependency", document, XPathConstants.NODESET);

        class Morphable1 extends Morphable<NodeList, Iterable<Node>> implements Iterable<Node> {
            @Mapped
            @Override
            public Iterator<Node> iterator() {
                return new Iterator<Node>() {
                    int i = 0;
                    @Override
                    public boolean hasNext() {
                        return i < get().getLength();
                    }
                    @Override
                    public Node next() {
                        return get().item(i++);
                    }
                };
            }
        }

        Morphable1 m1 = new Morphable1();
        for (Node node : m1.morph(nodeList)) {
            System.err.println(node.getNodeName());
        }
    }

    @Test
    void test3() throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(new File("pom.xml"));
        XPathFactory xpf = XPathFactory.newInstance();
        XPath xPath = xpf.newXPath();
        NodeList nodeList = (NodeList) xPath.evaluate("/project/dependencies/dependency", document, XPathConstants.NODESET);

        for (Node node : morpher.morph(nodeList)) {
            System.err.println(node.getNodeName());
        }
    }

    @SafeVarargs
    static <X> Class<?> test(X... args) {
        return args.getClass().getComponentType();
    }

    @Test
    void test2() {
        assertEquals(String.class, test("a"));
        assertEquals(Object.class, test("", new Object()));
        assertEquals(Number.class, test(1, 1D));
        assertEquals(Exception.class, test(new IOException(), new RuntimeException()));
    }
}
