/*
 * Copyright (c) 2005 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.lang.instrumentation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Properties;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;


/**
 * PropertyClassFileTransformer.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 050320 nsano initial version <br>
 */
public class PropertiesClassFileTransformer implements VaviClassFileTransformer {

    /** */
    private static Properties props; 

    /** */
    private static final String prefix = "vavix.lang.instrumentation.PropertiesClassFileTransformer";

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
     * "VaviInstrumentation.properties" をクラスパスが通った場所においてください。
     * <pre>
     * vavix.lang.instrumentation.PropertiesClassFileTransformer.class ... package/name/ClassName
     * vavix.lang.instrumentation.PropertiesClassFileTransformer.method ... method name ("*" means for all methods)
     * vavix.lang.instrumentation.PropertiesClassFileTransformer.constructor ... constructor name ("vavix.lang.instrumentation.PropertiesClassFileTransformer.method" の方が優先する)
     * vavix.lang.instrumentation.PropertiesClassFileTransformer.inserBefore ... ex. {System.err.println("args: " + $$);}
     * vavix.lang.instrumentation.PropertiesClassFileTransformer.insertAfter ... ex. {System.err.println("result: " + $_);}
     * </pre>
     */
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        ClassPool classPool = ClassPool.getDefault();

        if (props == null) {
            try {
                props = new Properties();
                props.load(VaviInstrumentation.class.getResourceAsStream("/VaviInstrumentation.properties"));
            } catch (IOException e) {
e.printStackTrace(System.err);
                throw (IllegalClassFormatException) new IllegalClassFormatException().initCause(e);
            }
//System.err.println("class: " + props.getProperty("vavix.lang.instrumentation.PropertiesClassFileTransformer.class"));
        }

//System.err.println("className: " + className);
        if (className.equals(props.getProperty(prefix + "." + key + ".class"))) {
//System.err.println("modify: " + className);
            try {
                ByteArrayInputStream stream = new ByteArrayInputStream(classfileBuffer);
                CtClass ctClass = classPool.makeClass(stream);

                String method = props.getProperty(prefix + "." + key + "." + "method");
                String constructor = props.getProperty(prefix + "." + key + "." + "constructor");
                String inserBefore = props.getProperty(prefix + "." + key + "." + "inserBefore");
                String insertAfter = props.getProperty(prefix + "." + key + "." + "insertAfter");

                // TODO regex match
                if (method != null) {
	                if ("*".equals(method)) {
	                    CtMethod[] ctMethods = ctClass.getDeclaredMethods();
	                    for (int i = 0; i < ctMethods.length; i++) {
	                        if (inserBefore != null) {
	                            ctMethods[i].insertBefore(inserBefore);
	                        }
	                        if (insertAfter != null) {
	                            ctMethods[i].insertAfter(insertAfter);
	                        }
	                    }
	                } else {
	                    CtMethod ctMethod = ctClass.getDeclaredMethod(method);
	                    if (inserBefore != null) {
	                        ctMethod.insertBefore(inserBefore);
	                    }
	                    if (insertAfter != null) {
	                        ctMethod.insertAfter(insertAfter);
	                    }
	                }
                } else if (constructor != null) {
	                if ("*".equals(constructor)) {
	                	CtConstructor[] ctConstructors = ctClass.getConstructors();
	                    for (int i = 0; i < ctConstructors.length; i++) {
	                        if (inserBefore != null) {
	                        	ctConstructors[i].insertBefore(inserBefore);
	                        }
	                        if (insertAfter != null) {
	                        	ctConstructors[i].insertAfter(insertAfter);
	                        }
	                    }
	                } else {
	                	CtConstructor ctConstructor = ctClass.getConstructor(constructor);
	                    if (inserBefore != null) {
	                    	ctConstructor.insertBefore(inserBefore);
	                    }
	                    if (insertAfter != null) {
	                    	ctConstructor.insertAfter(insertAfter);
	                    }
	                }
                }

                return ctClass.toBytecode();
            } catch (Exception e) {
e.printStackTrace(System.err);
                throw (IllegalClassFormatException) new IllegalClassFormatException().initCause(e);
            }
        } else {
            return null;
        }
    }
}

/* */
