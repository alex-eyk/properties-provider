package com.ximand.properties.mapper;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public final class StringToRightTypeMapperImpl implements StringToRightTypeMapper {

    private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER_MAP = new HashMap<Class<?>, Class<?>>() {{
        put(boolean.class, Boolean.class);
        put(byte.class, Byte.class);
        put(char.class, Character.class);
        put(double.class, Double.class);
        put(float.class, Float.class);
        put(int.class, Integer.class);
        put(long.class, Long.class);
        put(short.class, Short.class);
        put(void.class, Void.class);
    }};

    @Override
    public Object map(String from, Class<?> toClass) {
        toClass = getWrapped(toClass);
        try {
            Method method = toClass.getMethod("valueOf", String.class);
            return method.invoke(null, from);
        } catch (Exception e) {
            return from;
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> Class<T> getWrapped(Class<T> c) {
        return c.isPrimitive() ? (Class<T>) PRIMITIVE_TO_WRAPPER_MAP.get(c) : c;
    }

}
