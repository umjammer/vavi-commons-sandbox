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
 * Swing �R���|�[�l���g���t�B�[���h�Ƃ��Ď��� bean �ɑ΂���
 * POJO �Ƃ� BeanUtils �I�ȑ�����s���N���X�ł��B
 * Swing �R���|�[�l���g�� java �̌^�̃}�b�s���O�͈ȉ��Ɏ����܂��B
 * 
 * TODO �}�b�s���O�͒�`�t�@�C��
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 040830 nsano initial version <br>
 */
public class SwingBeanUtils extends AdvancedBeanUtils {

    /** */
    private static Log logger = LogFactory.getLog(SwingBeanUtils.class);

    /**
     * <pre>
     * 
     *  JTextField	setText, getText
     *  JLabel		n/a, getText
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
