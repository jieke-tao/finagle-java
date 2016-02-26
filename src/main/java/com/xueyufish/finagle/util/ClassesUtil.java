package com.xueyufish.finagle.util;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.Map;

public class ClassesUtil {

    private static final Charset UTF_8 = Charset.forName("UTF8");

    public static <T> T hash2Bean(Map<byte[], byte[]> infoMap, Class<T> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        try {
            Object o = clazz.newInstance();
            for (Field field : fields) {
                String name = field.getName();
                byte[] value = infoMap.get(name.getBytes(UTF_8));
                if (value != null) {
                    Object v = primitiveConverter(field.getType(), new String(value));
                    field.setAccessible(true);
                    field.set(o, v);
                }
            }
            return (T) o;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T map2Bean(Map<?, ?> infoMap, Class<T> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        try {
            Object o = clazz.newInstance();
            for (Field field : fields) {
                String name = field.getName();
                Object value = infoMap.get(name);
                if (value != null) {
                    Object v = primitiveConverter(field.getType(), value.toString());
                    field.setAccessible(true);
                    field.set(o, v);
                }
            }
            return (T) o;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object primitiveConverter(Class<?> clazz, String value) {
        value = value == null || value.equals("") ? null : value;
        if ((Boolean.class == clazz) || (Boolean.TYPE == clazz)) {
            return value != null && Boolean.parseBoolean(value);
        }
        if (Byte.class == clazz) {
            return value == null ? null : Byte.parseByte(value);
        }
        if (Byte.TYPE == clazz) {
            return value == null ? 0x0 : Byte.parseByte(value);
        }
        if (Short.class == clazz) {
            return value == null ? null : Short.parseShort(value);
        }
        if (Short.TYPE == clazz) {
            return value == null ? 0 : Short.parseShort(value);
        }
        if (Integer.class == clazz) {
            return value == null ? null : Integer.parseInt(value);
        }
        if (Integer.TYPE == clazz) {
            return value == null ? 0 : Integer.parseInt(value);
        }
        if (Long.class == clazz) {
            return value == null ? null : Long.parseLong(value);
        }
        if (Long.TYPE == clazz) {
            return value == null ? 0L : Long.parseLong(value);
        }
        if (Float.class == clazz) {
            return value == null ? null : Float.parseFloat(value);
        }
        if (Float.TYPE == clazz) {
            return value == null ? 0F : Float.parseFloat(value);
        }
        if (Double.class == clazz) {
            return value == null ? null : Double.parseDouble(value);
        }
        if (Double.TYPE == clazz) {
            return value == null ? 0D : Double.parseDouble(value);
        }
        return value;
    }

    public static boolean isPrimitive(Class<?> clazz) {
        if ((Boolean.class == clazz) || (Boolean.TYPE == clazz)) {
            return true;
        }
        if ((Byte.class == clazz) || (Byte.TYPE == clazz)) {
            return true;
        }
        if ((Short.class == clazz) || (Short.TYPE == clazz)) {
            return true;
        }
        if ((Integer.class == clazz) || (Integer.TYPE == clazz)) {
            return true;
        }
        if ((Long.class == clazz) || (Long.TYPE == clazz)) {
            return true;
        }
        if ((Float.class == clazz) || (Float.TYPE == clazz)) {
            return true;
        }
        if ((Double.class == clazz) || (Double.TYPE == clazz)) {
            return true;
        }
        return false;
    }
}
