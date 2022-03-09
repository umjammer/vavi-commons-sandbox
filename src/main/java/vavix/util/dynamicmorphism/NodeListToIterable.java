/*
 * Copyright (c) 2019 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.util.dynamicmorphism;

import java.util.Iterator;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * NodeListToIterable.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2019/07/04 umjammer initial version <br>
 */
public class NodeListToIterable extends Morphable<NodeList, Iterable<Node>> implements Iterable<Node> {
    /** */
    private NodeListToIterable() {}

    /** */
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

    /** */
    public static NodeListToIterable morpher = new NodeListToIterable();
}

/* */
