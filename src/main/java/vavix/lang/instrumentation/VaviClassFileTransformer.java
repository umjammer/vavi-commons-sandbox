/*
 * Copyright (c) 2006 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.lang.instrumentation;

import java.lang.instrument.ClassFileTransformer;


/**
 * VaviClassFileTransformer. 
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 060809 nsano initial version <br>
 */
public interface VaviClassFileTransformer extends ClassFileTransformer {
    /** */
    void setKey(String key);
    /** */
    String getKey();
}

/* */
