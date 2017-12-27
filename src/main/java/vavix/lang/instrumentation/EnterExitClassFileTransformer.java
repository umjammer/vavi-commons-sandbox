/*
 * Copyright (c) 2005 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.lang.instrumentation;

import java.io.ByteArrayInputStream;
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
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 050320 nsano initial version <br>
 */
public class EnterExitClassFileTransformer implements VaviClassFileTransformer {

    /** */
    private static Pattern pattern;

    /** */
    private static final String prefix = EnterExitClassFileTransformer.class.getName();

    /** */
    private String key;

    /** */
    public String getKey() {
        return key;
    }

    /** */
    public void setKey(String key) {
        this.key = key;
//System.err.println("EnterExitClassFileTransformer::setKey: key: " + key);
    }

    /**
     * <pre>
     * vavix.lang.instrumentation.EnterExitClassFileTransformer.${key}.pattern ... class name matcher in regex
     * </pre>
     */
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        ClassPool classPool = ClassPool.getDefault();

        if (pattern == null) {
            Properties props = System.getProperties();
            pattern = Pattern.compile(props.getProperty(prefix + "." + key + "." + "pattern"));
System.err.println("EnterExitClassFileTransformer::transform: pattern: " + pattern.pattern());
        }

//System.err.println("EnterExitClassFileTransformer::transform: format: " + className);
        Matcher matcher = pattern.matcher(className);
        if (matcher.matches()) {
//System.err.println("EnterExitClassFileTransformer::transform: format: " + className);
            try {
                ByteArrayInputStream stream = new ByteArrayInputStream(classfileBuffer);
                CtClass ctClass = classPool.makeClass(stream);

                CtMethod[] ctMethods = ctClass.getDeclaredMethods();
                for (int i = 0; i < ctMethods.length; i++) {
                    ctMethods[i].insertBefore("{System.err.println(\"Enter " + ctClass.getName() + "#" + ctMethods[i].getName() + ctMethods[i].getSignature() + "\");}");
                    ctMethods[i].insertAfter("{System.err.println(\"Exit " + ctClass.getName() + "#" + ctMethods[i].getName() + ctMethods[i].getSignature() + "\");}");
                }

                return ctClass.toBytecode();
            } catch (Exception e) {
//e.printStackTrace(System.err);
System.err.println("EnterExitClassFileTransformer::transform: " + className + ": " + e);
//                throw (IllegalClassFormatException) new IllegalClassFormatException().initCause(e);
                return null;
            }
        } else {
//System.err.println("ignore: " + className);
            return null;
        }
    }
}

/* */
