/*
 * Copyright (c) 2003 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util;

import java.util.HashMap;
import java.util.Map;



/**
 * Singleton.
 * <p>
 * パッケージが違うサブクラスはクラス、デフォルトコンストラクタともに public でなければなりません。
 * </p>
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 030821 nsano initial version <br>
 */
public abstract class Singleton {

    /**
     * サブクラスを作れるようにするため、コンストラクタを protected にする
     */
    protected Singleton() {
Debug.println("class: " + this.getClass());
        if (singletons.get(this.getClass()) != null) {
            throw new IllegalStateException("duplication, maybe bad constractor modifire");
        }
    }

    /** */
    protected static Map<Class<? extends Singleton>, Singleton> singletons = new HashMap<Class<? extends Singleton>, Singleton>();

    /**
     * @return このクラスの唯一のインスタンス
     */
    @SuppressWarnings("unchecked")
    public static <T extends Singleton> T getInstance(Class<T> clazz) {
        try {
            T instance = (T) singletons.get(clazz);
            if (instance == null) {
                synchronized (Singleton.class) {
                    if (instance == null) {
                        instance = getInstanceInternal(clazz);
                    }
                }
            }
            return instance;
        } catch (Exception e) {
            throw (RuntimeException) new IllegalStateException().initCause(e);
        }
    }

    /** */
    private static synchronized <T extends Singleton> T getInstanceInternal(Class<T> clazz) throws Exception {
        T instance = clazz.newInstance();
        singletons.put(clazz, instance);
        return instance;
    }
}

/* */
