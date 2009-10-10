/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util;

import java.util.ArrayList;
import java.util.EmptyStackException;


/**
 * A First-in first-out on the model of the java.util.Stack
 * 
 * @since Transmorpher 1.0
 * @see java.util.Stack#Stack()
 * @see java.util.Vector#Vector()
 * @author Fluxmedia and INRIA Rh�ne-Alpes.
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 030314 nsano ported <br>
 */
public class Fifo<E> extends ArrayList<E> {

    /** The first position at which there is unpoped value */
    private int first = 0;

    /** The last position at which there is no value */
    private int last = 0;

    /** The constructor allocates a vector */

    /**
     * Creates an empty Fifo
     */
    public Fifo() {
        super(1024); // That is arbitrary
    }

    /**
     * Pushes an item onto the top of the Fifo
     * 
     * @param item the item to be pushed on the Fifo
     * @see java.util.Vector#addElement(Object)
     */
    public void push(E item) {
        last++;
        add(item);
    }

    /**
     * Returns the first element of the Fifo which is withdrawn
     * 
     * @return the first element on top of the Fifo
     * @throws java.util.EmptyStackException
     */
    public E pop() throws EmptyStackException {
        if (first == last) {
            throw new EmptyStackException();
        }
        // For garbage collection
        return set(first++, null);
    }

    /**
     * Returns the first element of the Fifo (which is left unchanged)
     * 
     * @return the first element on top of the Fifo
     * @throws java.util.EmptyStackException
     */
    public E peek() throws EmptyStackException {
        if (first == last) {
            throw new EmptyStackException();
        }
        return get(first);
    }

    /**
     * T400 if the Fifo is empty
     * 
     * @return true if and only if the Fifo contains no item; false otherwise
     */
    public boolean isEmpty() {
        return (first == last);
    }

    /**
     * Returns the number of elements in the Fifo
     * 
     * @return the number of elements in the Fifo
     */
    public int size() {
        return (last - first);
    }

    /**
     * Empties the Fifo
     */
    public void clear() {
        super.clear(); // For garbage collection
        first = 0;
        last = 0;
    }

    /**
     * Empties the Fifo
     */
    public void removeAllElements() {
        super.clear(); // For garbage collection
        first = 0;
        last = 0;
    }

    /**
     * Returns the 1-based position where the object item is in the Fifo. If the object item accours at an element of the Fifo,
     * this method returns the distance from the bottom of the Fifo of the occurence nearest to the bottom. The bottom item is
     * considered at distance 1. The equals method is used to compare item to the items in this Fifo.
     * 
     * @param item the item to be found
     * @return the 1-based position for the begining of the Fifo where the object is located; the return value -1 indicates that
     *         the object is not in the Fifo.
     * @see java.util.Vector#addElement(Object)
     */
    public int search(E item) {
        boolean found = false;
        int i;
        for (i = first; !found && i != last; i++) {
            if (item.equals(get(i))) {
                found = true;
            }
        }
        return found ? (first - i) : -1;
    }
}

/* */
