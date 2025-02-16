/*
 * Copyright (c) 2019 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.tools.ant;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * DiffTaskTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2019/05/04 umjammer initial version <br>
 */
class DiffTaskTest {

    @Test
    @Disabled
    void test() {
        fail("Not yet implemented");
    }

    //----

    /** */
    public static void main(String[] args) {
        DiffTask task = new DiffTask();
        task.setSrcFile(args[0]);
        task.setTargetFile(args[1]);
        task.execute();
    }
}
