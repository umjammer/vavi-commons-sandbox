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
 * �p�b�P�[�W���Ⴄ�T�u�N���X�̓N���X�A�f�t�H���g�R���X�g���N�^�Ƃ��� public �łȂ���΂Ȃ�܂���B
 * </p>
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 030821 nsano initial version <br>
 */
public abstract class Singleton {

    /**
     * �T�u�N���X������悤�ɂ��邽�߁A�R���X�g���N�^�� protected �ɂ���
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
     * @return ���̃N���X�̗B��̃C���X�^���X
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
