/*
 * Copyright (c) 2004 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import vavi.util.StringUtil;


/**
 * AdvancedBeanUtils.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 040830 nsano initial version <br>
 */
public class AdvancedBeanUtils {

    /** */
    private static Log logger = LogFactory.getLog(AdvancedBeanUtils.class);

    /** */
    protected static boolean isAccessibleField(String key, Object bean) {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            // logger.debug(key + ", " + field.getName());
            if (field.getName().equals(key)) {
                if ((field.getModifiers() & (Modifier.STATIC | Modifier.FINAL)) != 0) {
logger.debug("Wrong modifier, ignore: " + StringUtil.toBits(field.getModifiers()));
                    return false;
                } else {
                    return true;
                }
            }
        }

logger.debug("No such field: " + key);
        return false;
    }

    /**
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static void copyProperties(Map<String, Object> dest, Object bean) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        if (dest == null || bean == null) {
            return;
        }

        Map<?, ?> fields = BeanUtils.describe(bean);
        Iterator<?> i = fields.keySet().iterator();
        while (i.hasNext()) {
            String key = (String) i.next();
            if (isAccessibleField(key, bean)) {
                Method method = null;
                String methodName = null;
                try {
                    methodName = "get" + StringUtil.capitalize(key);
                    method = bean.getClass().getMethod(methodName, (Class[]) null);
                } catch (NoSuchMethodException e) {
                    methodName = "is" + StringUtil.capitalize(key);
                    method = bean.getClass().getMethod(methodName, (Class[]) null);
                }
                Object value = method.invoke(bean, (Object[]) null);
                dest.put(key, value);
// logger.debug("OK: " + key + ", " + fields.get(key) + ", " + value);
            } else {
logger.debug("wrong field: " + key);
                continue;
            }
        }
    }
}

/* */
