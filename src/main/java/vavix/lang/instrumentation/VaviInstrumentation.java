/*
 * Copyright (c) 2005 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.lang.instrumentation;

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
     * System Properties に
     * "ctf." で始まる名前のプロパティに {@link VaviClassFileTransformer} を実装したクラスを指定します。
     * "." 以降は識別子として利用されます。
     */
    public static void premain(String agentArgs, Instrumentation instrumentation) {
        try {
            Properties props = System.getProperties();
            Enumeration<?> e = props.propertyNames();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
//System.err.println("VaviInstrumentation::premain: key: " + key);
                if (key.startsWith("ctf.")) {
                    String id = key.substring(4);
                    String cftClassName = props.getProperty(key);
//System.err.println("VaviInstrumentation::premain: cftClassName: " + cftClassName);
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
