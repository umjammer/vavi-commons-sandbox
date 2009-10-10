/*
 * Copyright (c) 2005 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.util;

import java.lang.instrument.Instrumentation;
import java.util.Enumeration;
import java.util.Properties;


/**
 * VaviInstrumentation.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 050320 nsano initial version <br>
 */
public class VaviInstrumentation {

    /**
     * "/VaviInstrumentation.properties" に
     * "ctf." で始まる名前のプロパティに {@link VaviClassFileTransformer} を実装したクラスを指定します。
     * "." 以降は識別子として利用されます。
     */
    public static void premain(String agentArgs, Instrumentation instrumentation) {
        try {
            Properties props = new Properties();
            props.load(VaviInstrumentation.class.getResourceAsStream("/VaviInstrumentation.properties"));
            Enumeration<?> e = props.propertyNames();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                if (key.startsWith("ctf.")) {
                    String id = key.substring(4);
                    String cftClassName = props.getProperty(key);
                    Class<?> ctfClass = Class.forName(cftClassName);
                    VaviClassFileTransformer vctf = (VaviClassFileTransformer) ctfClass.newInstance();
                    vctf.setKey(id);
                    instrumentation.addTransformer(vctf);
                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
}

/* */
