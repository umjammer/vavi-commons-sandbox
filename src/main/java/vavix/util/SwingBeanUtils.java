/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.util;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Swing コンポーネントをフィールドとして持つ bean に対して
 * POJO との BeanUtils 的な操作を行うクラスです。
 * Swing コンポーネントと java の型のマッピングは以下に示します。
 *
 * TODO マッピングは定義ファイル
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 040830 nsano initial version <br>
 */
@Deprecated
public class SwingBeanUtils extends AdvancedBeanUtils {

    /** */
    private static Log logger = LogFactory.getLog(SwingBeanUtils.class);

    /**
     * <pre>
     *
     *  JTextField  setText, getText
     *  JLabel      n/a, getText
     *
     * </pre>
     *
     * @param swingBean
     * @param bean
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static void copyToSwingBean(Object swingBean, Object bean) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        if (swingBean == null || bean == null) {
            return;
        }
    }

    /**
     *
     * @param bean
     * @param swingBean
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public static void copyFromSwingBean(Object bean, Object swingBean) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        if (swingBean == null || bean == null) {
            return;
        }
    }

    /**
     * @param readOnly
     */
    public static void setReadOnly(boolean readOnly) {
        // TODO Auto-generated method stub
logger.debug("");
    }
}

/* */
