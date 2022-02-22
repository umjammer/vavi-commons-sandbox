/*
 * Copyright (c) 2005 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


/**
 * Singleton2Test.
 * <p>
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 050914 nsano initial version <br>
 */
@SuppressWarnings("unused")
class Singleton2Test {

    public static class A extends Singleton {
    }

    public static class B extends A {
    }

    @Test
    void test1() throws Exception {

        A a = A.getInstance(A.class);
        B b = B.getInstance(B.class);
    }

    public static class C extends Singleton {
    }

    @Test
    void test2() throws Exception {
        C c = C.getInstance(C.class);
    }

    public static class D extends Singleton {
        public D() {
        }
    }

    @Test
    void test3() throws Exception {

        D d = D.getInstance(D.class);
    }

    public static class E extends Singleton {
    }

    @Test
    void test4() throws Exception {
        E e = E.getInstance(E.class);
        assertTrue(true);
        try {
            e = new E();
            fail();
        } catch (IllegalStateException f) {
        }
    }

    interface X<T> {
        T get();
    }

    public static class F extends Singleton implements X<Object> {
        public F() {
        }
        public Object get() {
            return null;
        }
    }

    @Test
    void test5() throws Exception {
        F f = F.getInstance(F.class);
    }
}

/* */
