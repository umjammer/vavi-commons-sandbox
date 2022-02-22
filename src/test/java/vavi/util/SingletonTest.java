/*
 * Copyright (c) 2005 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;


/**
 * SingletonTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 050914 nsano initial version <br>
 */
@SuppressWarnings("unused")
class SingletonTest {

    static class A extends Singleton {
    }

    static class B extends A {
    }

    @Test
    public void test1() throws Exception {

        A a = A.getInstance(A.class);
        B b = B.getInstance(B.class);
    }

    static class C extends Singleton {
        private C() {
        }
    }

    @Test
    public void test2() throws Exception {
        assertThrows(IllegalStateException.class, () -> {
            C c = C.getInstance(C.class);
        });
    }

    static class D extends Singleton {
        protected D() {
        }
    }

    @Test
    public void test3() throws Exception {

        D d = D.getInstance(D.class);
    }

    static class E extends Singleton {
    }

    @Test
    public void test4() throws Exception {
        assertThrows(IllegalStateException.class, () -> {
            E e = E.getInstance(E.class);
            e = new E();
        });
    }

    interface X<T> {
        T get();
    }

    static class F extends Singleton implements X<Object> {
        protected F() {
        }
        public Object get() {
            return null;
        }
    }

    @Test
    public void test5() throws Exception {
        F f = F.getInstance(F.class);
    }

    @Test
    public void test6() throws Exception {
        G g = G.getInstance(G.class);
    }

    @Test
    public void test7() throws Exception {
        H h = H.getInstance(H.class);
    }
}

class G extends Singleton {
    protected G() {
    }
}

interface Y<T> {
    T get();
}

class H extends Singleton implements Y<Object> {
    protected H() {
    }
    public Object get() {
        return null;
    }
}

/* */
