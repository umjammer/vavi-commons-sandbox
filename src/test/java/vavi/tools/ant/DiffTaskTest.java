/*
 * Copyright (c) 2019 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.tools.ant;

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
    void test() {
        fail("Not yet implemented");
    }

    //----

    /** */
    public static void main(String[] args) {
        DiffTask task = new DiffTask();
        task.setSrcfile(args[0]);
        task.setTargetfile(args[1]);
        task.execute();
    }
}

/* */
