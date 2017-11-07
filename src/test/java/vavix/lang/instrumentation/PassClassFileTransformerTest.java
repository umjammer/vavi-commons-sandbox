/*
 * Copyright (c) 2011 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.lang.instrumentation;

import org.junit.Test;

import static org.junit.Assert.*;

import vavix.lang.instrumentation.PassClassFileTransformer;


/**
 * PassClassFileTransformerTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2011/08/23 umjammer initial version <br>
 */
public class PassClassFileTransformerTest {

    @Test
    public void test01() {
        assertEquals("jp.noids.image.scaling.gui.l", PassClassFileTransformer.normalize("jp.noids.image.scaling.gui.MugenSampleDialog$N$l"));
    }
}

/* */
