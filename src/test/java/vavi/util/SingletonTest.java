/*
 * Copyright (c) 2005 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util;


import junit.framework.TestCase;


/**
 * SingletonTest. 
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 050914 nsano initial version <br>
 */
@SuppressWarnings("unused") 
public class SingletonTest extends TestCase {

    static class A extends Singleton {
    }

    static class B extends A {
    }

    /** */
    public void test1() throws Exception {

        A a = A.getInstance(A.class);
        B b = B.getInstance(B.class);
    }

    static class C extends Singleton {
        private C() {
        }
    }

    /** */
    public void test2() throws Exception {
        try {
            C c = C.getInstance(C.class);
            fail();
        } catch (IllegalStateException e) {
        }
    }

    static class D extends Singleton {
        protected D() {
        }
    }

    /** */
    public void test3() throws Exception {

        D d = D.getInstance(D.class);
    }

    static class E extends Singleton {
    }

    /** */
    public void test4() throws Exception {
        try {
            E e = E.getInstance(E.class);
            e = new E();
            fail();
        } catch (IllegalStateException e) {
        }
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

    /** */
    public void test5() throws Exception {
        F f = F.getInstance(F.class);
    }

    /** */
    public void test6() throws Exception {
        G g = G.getInstance(G.class);
    }

    /** */
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
