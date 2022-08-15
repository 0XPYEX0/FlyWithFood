package me.xpyex.plugin.flywithfood.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ReflectUtil {
    private static final Map<String, Method> NO_ARGS_METHOD_CACHES = new HashMap<>();
    private static final Map<String, Method> ARGS_METHOD_CACHES = new HashMap<>();
    private static final Map<String, Field> FIELD_CACHES = new HashMap<>();

    /**
     * 执行target对象下的方法，并获取方法的返回值
     * @param target 目标对象
     * @param methodName 要执行的方法名
     * @param args 填入方法的参数
     * @return 方法的返回值
     */
    @SuppressWarnings("unchecked")
    public static <T> T runMethod(Object target, String methodName, Object... args) {
        if (Util.checkNull(target, methodName)) return null;

        try {

            if (!methodExists(target.getClass(), methodName)) return null;

            Method method = getMethod(target.getClass(), methodName);

            Object result = method.invoke(target, args);
            return (T) result;

        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 两个类是否可以互相转换
     * @param c1 类1
     * @param c2 类2
     * @return 是否能够转换
     */
    public static boolean isAssignable(Class<?> c1, Class<?> c2) {
        if (Util.checkNull(c1, c2)) return false;

        return c1.isAssignableFrom(c2) || c2.isAssignableFrom(c1);
    }

    /**
     * 获取类下指定名称的方法
     * @param c 目标类
     * @param methodName 方法名
     * @return 指定方法
     */
    public static Method getMethod(Class<?> c, String methodName) {
        if (Util.checkNull(c, methodName)) return null;

        if (!NO_ARGS_METHOD_CACHES.containsKey(c.getName() + "." + methodName + "()")) {
            try {
                Method method = c.getMethod(methodName);
                NO_ARGS_METHOD_CACHES.put(c.getName() + "." + methodName + "()", method);
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        }

        return NO_ARGS_METHOD_CACHES.get(c.getName() + "." + methodName + "()");
    }

    /**
     * 方法是否存在
     * @param c 目标类
     * @param methodName 方法名
     * @return 是否存在
     */
    public static boolean methodExists(Class<?> c, String methodName) {
        return getMethod(c, methodName) != null;
        //
    }

    /**
     * 获取类下指定方法
     * @param c 目标类
     * @param methodName 方法名
     * @param types 参数类型
     * @return 指定方法
     */
    public static Method getMethod(Class<?> c, String methodName, Class<?>... types) {
        if (!methodExists(c, methodName)) return null;

        if (!ARGS_METHOD_CACHES.containsKey(c.getName() + "." + methodName + "()")) {
            try {
                Method method = c.getMethod(methodName, types);
                ARGS_METHOD_CACHES.put(c.getName() + "." + methodName + "()", method);
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        }

        return ARGS_METHOD_CACHES.get(c.getName() + "." + methodName + "()");
    }

    /**
     * 检查方法是否存在
     * @param c 目标类
     * @param methodName 方法名
     * @param types 参数类型
     * @return 是否存在
     */
    public static boolean methodExists(Class<?> c, String methodName, Class<?>... types) {
        if (!methodExists(c, methodName))  return false;

        return getMethod(c, methodName, types) != null;
    }

    /**
     * 获取指定字段
     * @param target 目标对象
     * @param fieldName 字段名
     * @return 指定字段
     */
    @SuppressWarnings("unchecked")
    public static <T> T getField(Object target, String fieldName) {
        if (Util.checkNull(target, fieldName)) return null;

        if (!FIELD_CACHES.containsKey(target.getClass().getName() + "." + fieldName)) {
            try {
                FIELD_CACHES.put(target.getClass().getName() + "." + fieldName, target.getClass().getDeclaredField(fieldName));
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        }
        try {
            FIELD_CACHES.get(target.getClass().getName() + "." + fieldName).setAccessible(true);
            return (T) FIELD_CACHES.get(target.getClass().getName() + "." + fieldName).get(target);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 检查字段是否存在
     * @param c 目标类
     * @param fieldName 字段名
     * @return 是否存在
     */
    public static boolean fieldExists(Class<?> c, String fieldName) {
        return getField(c, fieldName) != null;
        //
    }
}
