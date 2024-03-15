/*
 * Copyright (c) 2007 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import vavix.io.FastByteArrayOutputStream;


/**
 * Utility for making deep copies (vs. clone()'s shallow copies) of objects.
 * Objects are first serialized and then deserialized. Error checking is fairly
 * minimal in this implementation. If an object is encountered that cannot be
 * serialized (or that references an object that cannot be serialized) an error
 * is printed to System.err and null is returned. Depending on your specific
 * application, it might make more sense to have copy(...) re-throw the
 * exception.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 071106 nsano initial version <br>
 */
public class ObjectUtil {

    /**
     * Returns a copy of the object, or null if the object cannot be serialized.
     */
    @SuppressWarnings("unchecked")
    public static <T> T copy(T original) {
        T object = null;
        try {
            // Write the object out to a byte array
            FastByteArrayOutputStream fbaos = new FastByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(fbaos);
            out.writeObject(original);
            out.flush();
            out.close();

            // Retrieve an input stream from the byte array and read
            // a copy of the object back in.
            ObjectInputStream in = new ObjectInputStream(fbaos.getInputStream());
            object = (T) in.readObject();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
        return object;
    }
}
