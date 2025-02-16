/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.HashMap;
import java.util.Map;

import static java.lang.System.getLogger;


/**
 * Singleton.
 * <p>
 * Subclasses in different packages must have public classes and default constructors.
 * </p>
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 030821 nsano initial version <br>
 */
public abstract class Singleton {

    private static final Logger logger = getLogger(Singleton.class.getName());

    /**
     * Make the constructor protected to allow subclassing
     */
    protected Singleton() {
logger.log(Level.DEBUG, "class: " + this.getClass());
        if (singletons.get(this.getClass()) != null) {
            throw new IllegalStateException("duplication, maybe bad constractor modifire");
        }
    }

    /** */
    protected static Map<Class<? extends Singleton>, Singleton> singletons = new HashMap<>();

    /**
     * @return The only instance of this class
     */
    @SuppressWarnings("unchecked")
    public static <T extends Singleton> T getInstance(Class<T> clazz) {
        try {
            T instance = (T) singletons.get(clazz);
            if (instance == null) {
                synchronized (Singleton.class) {
                    if (instance == null) {
                        instance = getInstanceInternal(clazz);
                    }
                }
            }
            return instance;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /** */
    private static synchronized <T extends Singleton> T getInstanceInternal(Class<T> clazz) throws Exception {
        T instance = clazz.newInstance();
        singletons.put(clazz, instance);
        return instance;
    }
}
