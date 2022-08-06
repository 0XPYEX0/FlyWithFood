package me.xpyex.plugin.flywithfood.common.utils;

import java.lang.reflect.Method;

public class ReflectUtil {
    public static boolean methodExists(Class<?> clazz, String methodName) {
        if (clazz == null || methodName == null || methodName.isEmpty()) {
            return false;
        }
        try {
            for (Method method : clazz.getMethods()) {
                if (method.getName().equals(methodName)) {
                    return true;
                }
            }
        } catch (Exception ignored) { }
        return false;
    }

    public static boolean classExists(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ReflectiveOperationException ignored) { }
        return false;
    }

    public static boolean methodExists(Class<?> clazz, String methodName, Class<?>... params) {
        if (clazz == null || methodName == null || methodName.isEmpty()) {
            return false;
        }
        try {
            clazz.getMethod(methodName, params);
            return true;
        } catch (ReflectiveOperationException ignored) { }
        return false;
    }
}
