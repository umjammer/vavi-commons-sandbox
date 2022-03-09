/*
 * Copyright (c) 2019 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.util.dynamicmorphism;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Morphable.
 *
 * @param <B> before
 * @param <A> after
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2019/06/09 umjammer initial version <br>
 */
public abstract class Morphable<B, A> {

    private B object;

    protected B get() {
        return object;
    }

    @SuppressWarnings("unchecked")
    public A morph(B object) {
        this.object = object;

        Class<?> clazz = getClass();
        Class<?>[] interfaces = getInterfaces(clazz);
        if (interfaces.length == 0) {
            throw new IllegalArgumentException(clazz.getName());
        }

        InvocationHandler invocationHandler = (proxy, method, args) -> {
            Method m = clazz.getMethod(method.getName(), resolveArgTypes(args));
            Mapped mapping = m.getAnnotation(Mapped.class);
            if (mapping != null) {
                return Morphable.invoke(Morphable.this, m, args);
            } else {
                return Morphable.invoke(object, method.getName(), args);
            }
        };
        return (A) Proxy.newProxyInstance(object.getClass().getClassLoader(), interfaces, invocationHandler);
    }

    private static Class<?>[] getInterfaces(Class<?> clazz) {
        List<Class<?>> interfaces = new ArrayList<>();
        if (clazz.isInterface()) {
            interfaces.add(clazz);
        }
        interfaces.addAll(Arrays.asList(clazz.getInterfaces()));
        return interfaces.toArray(new Class<?>[interfaces.size()]);
    }

    public static <T> T invoke(Object target, String methodName, Object... args) {
        try {
            return invoke(target, target.getClass().getMethod(methodName, resolveArgTypes(args)), args);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T invoke(Object target, Method method, Object... args) {
        try {
            return (T) method.invoke(target, args);
        } catch (IllegalAccessException | InvocationTargetException | SecurityException e) {
            throw new IllegalStateException(e);
        }
    }

    private static Class<?>[] resolveArgTypes(Object... args) {
        if (args == null) return new Class[0];
        List<Class<?>> argTypes = new ArrayList<>();
        for (Object arg : args) {
            argTypes.add(arg.getClass());
        }
        return argTypes.toArray(new Class<?>[argTypes.size()]);
    }
}

/* */
