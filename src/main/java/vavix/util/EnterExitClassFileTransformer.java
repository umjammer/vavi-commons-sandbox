/*
 * Copyright (c) 2005 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;


/**
 * EnterExitClassFileTransformer.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 050320 nsano initial version <br>
 */
public class EnterExitClassFileTransformer implements VaviClassFileTransformer {

    /** */
    private static Pattern pattern; 

    /** */
    private static final String prefix = "vavix.util.EnterExitClassFileTransformer";

    /** */
    private String key; 

    /** */
    public String getKey() {
        return key;
    }

    /** */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * <pre>
     * vavix.util.EnterExitClassFileTransformer.pattern ... class name matcher in regex
     * </pre>
     */
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        ClassPool classPool = ClassPool.getDefault();

        if (pattern == null) {
            try {
                Properties props = new Properties();
                props.load(VaviInstrumentation.class.getResourceAsStream("/VaviInstrumentation.properties"));
        
                pattern = Pattern.compile(props.getProperty(prefix + "." + key + "." + "pattern"));
//System.err.println("pattern: " + pattern.pattern());
            } catch (IOException e) {
//e.printStackTrace(System.err);
System.err.println(e);
                throw (IllegalClassFormatException) new IllegalClassFormatException().initCause(e);
            }
        }

        Matcher matcher = pattern.matcher(className);
        if (matcher.matches()) {
//System.err.println("format: " + className);
            try {
                ByteArrayInputStream stream = new ByteArrayInputStream(classfileBuffer);
                CtClass ctClass = classPool.makeClass(stream);

                CtMethod[] ctMethods = ctClass.getDeclaredMethods();
                for (int i = 0; i < ctMethods.length; i++) {
                    ctMethods[i].insertBefore("{System.err.println(\"Enter " + ctClass.getName() + "#" + ctMethods[i].getName() + "\");}");
                    ctMethods[i].insertAfter("{System.err.println(\"Exit " + ctClass.getName() + "#" + ctMethods[i].getName() + "\");}");
                }

                return ctClass.toBytecode();
            } catch (Exception e) {
//e.printStackTrace(System.err);
System.err.println(className + ": " + e);
                throw (IllegalClassFormatException) new IllegalClassFormatException().initCause(e);
            }
        } else {
//System.err.println("ignore: " + className);
            return null;
        }
    }
}

/* */
