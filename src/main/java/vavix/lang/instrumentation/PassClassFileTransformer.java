/*
 * Copyright (c) 2005 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.lang.instrumentation;

import java.io.ByteArrayInputStream;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;


/**
 * PassClassFileTransformer.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 110823 nsano initial version <br>
 */
public class PassClassFileTransformer implements VaviClassFileTransformer {

    /** */
    private static Pattern pattern; 

    /** */
    private static final String prefix = PassClassFileTransformer.class.getName();

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
     * vavix.lang.instrumentation.PassClassFileTransformer.${key}.pattern ... class name matcher in regex
     * </pre>
     */
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        ClassPool classPool = ClassPool.getDefault();

        if (pattern == null) {
            Properties props = System.getProperties();
            pattern = Pattern.compile(props.getProperty(prefix + "." + key + "." + "pattern"));
System.err.println("PassClassFileTransformer::transform: pattern: " + pattern.pattern());
        }

        Matcher matcher = pattern.matcher(className);
        if (matcher.matches()) {
            try {
                ByteArrayInputStream stream = new ByteArrayInputStream(classfileBuffer);
                CtClass ctClass = classPool.makeClass(stream);

                CtMethod[] ctMethods = ctClass.getDeclaredMethods();
                for (int i = 0; i < ctMethods.length; i++) {
                    String key = getKey(ctClass, ctMethods[i]);
                    ctMethods[i].insertBefore("{" +
                                              "    if (!vavix.lang.instrumentation.PassClassFileTransformer.signatures.contains(\"" + key + "\")) {" +
                                              "        System.err.println(\"" + key + "\");" +
                                              "        vavix.lang.instrumentation.PassClassFileTransformer.signatures.add(\"" + key + "\");" +
                                              "    }" + 
                                              "}");
                }

                return ctClass.toBytecode();
            } catch (Exception e) {
System.err.println("PassClassFileTransformer::transform: " + className + ": " + e);
                return null;
            }
        } else {
            return null;
        }
    }
    
    /** */
    public static Set<String> signatures = new HashSet<>();
    
    /** */
    public static String getKey(CtClass ctClass, CtMethod ctMethod) {
        return normalize(ctClass.getName()) + "#" + ctMethod.getName() + ctMethod.getSignature();
    }
    
    static String normalize(String name) {
        if (name.indexOf('.') > 0 && name.lastIndexOf('$') > 0) {
            String packageName = name.substring(0, name.lastIndexOf('.'));
            String simpleName = name.substring(name.lastIndexOf('$') + 1);
            return packageName + '.' + simpleName;
        } else {
            return name;
        }
    }
}

/* */
