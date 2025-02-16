/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.util;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.StringTokenizer;

import static java.lang.System.getLogger;


/**
 * Class-related utility class.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020517 nsano initial version <br>
 *          0.01 040627 nsano add getWrapperClass <br>
 */
@Deprecated
public final class ClassUtil {

    private static final Logger logger = getLogger(ClassUtil.class.getName());

    /** Cannot access. */
    private ClassUtil() {}

    /**
     * Gets the class from a string.
     * TODO It seems like it's somewhere.
     *
     * @param className Primitive types can also be specified as is.
     *                  On the other hand, java.lang is not recognized, so please write it.
     */
    public static Class<?> forName(String className)
        throws ClassNotFoundException {

        Class<?> clazz;

        if ("boolean".equals(className)) {
            clazz = Boolean.TYPE;
        } else if ("byte".equals(className)) {
            clazz = Byte.TYPE;
        } else if ("char".equals(className)) {
            clazz = Character.TYPE;
        } else if ("double".equals(className)) {
            clazz = Double.TYPE;
        } else if ("float".equals(className)) {
            clazz = Float.TYPE;
        } else if ("int".equals(className)) {
            clazz = Integer.TYPE;
        } else if ("long".equals(className)) {
            clazz = Long.TYPE;
        } else if ("short".equals(className)) {
            clazz = Short.TYPE;
        } else if ("void".equals(className)) {
            clazz = Void.TYPE;
        } else {
            clazz = Class.forName(className);
        }
logger.log(Level.TRACE, clazz);
        return clazz;
    }

    /**
     * Gets a list of constructor argument type classes from a string.
     *
     * @param line The delimiters are { ',', '\t', ' ' }
     *            Primitive types are written as is: int, long ...
     */
    public static Class<?>[] getArgumentTypes(String line)
        throws ClassNotFoundException {

        StringTokenizer st = new StringTokenizer(line, "\t ,");
        Class<?>[] argTypes = new Class[st.countTokens()];
        for (int j = 0; j < argTypes.length; j++) {
            argTypes[j] = forName(st.nextToken());
        }

        return argTypes;
    }

    /**
     * Gets a list of constructor argument objects from a string.
     *
     * @param line delimiters are <code>',', '\t', ' '</code>
     *            null is <code>'null'</code>
     */
    static Object[] getArguments(String line, Class<?>[] argTypes)
        throws InstantiationException,
               IllegalAccessException {

        StringTokenizer st = new StringTokenizer(line, "\t ,");
        Object[] args = new Object[st.countTokens()];
        for (int j = 0; j < args.length; j++) {
            String arg = st.nextToken();
            if ("null".equals(arg)) {
                args[j] = null;
            } else if (argTypes[j] == Boolean.TYPE) {
                args[j] = Boolean.parseBoolean(arg);
            } else if (argTypes[j] == Byte.TYPE) {
                args[j] = Byte.parseByte(arg);
            } else if (argTypes[j] == Character.TYPE) {
                if (arg.length() > 1) {
                    throw new IllegalArgumentException(arg + " for char");
                }
                args[j] = arg.charAt(0);
            } else if (argTypes[j] == Double.TYPE) {
                args[j] = Double.parseDouble(arg);
            } else if (argTypes[j] == Float.TYPE) {
                args[j] = Float.parseFloat(arg);
            } else if (argTypes[j] == Integer.TYPE) {
                args[j] = new IntegerInstantiator().newInstance(arg);
            } else if (argTypes[j] == Long.TYPE) {
                args[j] = Long.parseLong(arg);
            } else if (argTypes[j] == Short.TYPE) {
                args[j] = Short.parseShort(arg);
            } else if (argTypes[j] == Void.TYPE) {
                throw new IllegalArgumentException(arg + " for void");
            } else if (argTypes[j] == String.class) {           // special
                args[j] = new StringInstantiator().newInstance(arg);
            } else if (argTypes[j] == java.awt.Color.class) {   // special
                args[j] = new ColorInstantiator().newInstance(arg);
            } else {    // TODO recursion may work
                args[j] = argTypes[j].newInstance();
            }
        }

        return args;
    }

    /**
     * Get a new instance.
     *
     * @param className Primitive types can also be specified as is.
     * @param argTypes The delimiters are { ',', '	', ' ' }
     *            Primitive types are written as is: int, long ...
     * @param args The delimiters are { ',', '	', ' ' }
     *            null is written as it is.
     */
    public static Object newInstance(String className,
                                     String argTypes,
                                     String args)
        throws ClassNotFoundException,
               InstantiationException,
               IllegalAccessException,
               NoSuchMethodException,
               InvocationTargetException {

        Class<?> clazz = Class.forName(className);

        Class<?>[] ats = getArgumentTypes(argTypes);
        Object[] as = getArguments(args, ats);

        Constructor<?> constructor = clazz.getConstructor(ats);
        return constructor.newInstance(as);
    }

    /**
     */
    static Field getField(String arg)
        throws NoSuchFieldException,
               ClassNotFoundException,
               IllegalAccessException {

        int p = arg.lastIndexOf('.');
        String className = arg.substring(0, p);
        String enumName = arg.substring(p + 1, arg.length());
logger.log(Level.TRACE, className + "#" + enumName);

        Class<?> clazz = Class.forName(className);

        return clazz.getDeclaredField(enumName);
    }

    /**
     * Gets the wrapper class for a primitive type.
     * @param primitiveClass int.class etc.
     */
    public Class<?> getWrapperClass(Class<?> primitiveClass) {
        Object array = Array.newInstance(primitiveClass, 1);
        Object wrapper = Array.get(array, 0);
        return wrapper.getClass();
    }
}

/** */
interface Instantiator {
    Object newInstance(String arg) throws InstantiationException;
}

/** */
class IntegerInstantiator implements Instantiator {
    public Object newInstance(String arg) throws InstantiationException {

        try {
            // integer
            return Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            // enumeration class.enum
            try {
                Field field = ClassUtil.getField(arg);

                if (field.getType() != Integer.TYPE)
                    throw new IllegalArgumentException(arg + " for int");

                return field.getInt(null);
            } catch (Exception f) {
                throw (InstantiationException)
                    new InstantiationException().initCause(f);
            }
        }
    }
}

/** */
class StringInstantiator implements Instantiator {
    public Object newInstance(String arg) throws InstantiationException {
        return arg;
    }
}

/** */
class ColorInstantiator implements Instantiator {
    public Object newInstance(String arg) throws InstantiationException {
        try {
            Field field = ClassUtil.getField(arg);

            if (field.getType() != java.awt.Color.class) {
                throw new IllegalArgumentException(arg + " for Color");
            }

            return field.get(null);
        } catch (Exception f) {
            throw (InstantiationException) new InstantiationException().initCause(f);
        }
    }
}
