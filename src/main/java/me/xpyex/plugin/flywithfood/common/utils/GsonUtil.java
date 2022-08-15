package me.xpyex.plugin.flywithfood.common.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.util.HashSet;

public class GsonUtil {
    public static final boolean IS_OLD_GSON;

    static {
        boolean result;
        try {
            new JsonArray().contains(new JsonPrimitive("CheckIsOld"));
            result = false;
        } catch (NoSuchMethodError ignored) {
            result = true;
        }
        IS_OLD_GSON = result;  //因为上面用final声明，所以需要用临时变量过渡，否则通不过编译
    }

    /**
     * 获取JsonObject中的所有Key
     *
     * @param target 目标JsonObject
     * @return 所有Key组成的数组
     */
    public static String[] getKeysOfJsonObject(JsonObject target) {
        HashSet<String> set = new HashSet<>();
        target.entrySet().forEach(E -> set.add(E.getKey()));
        return set.toArray(new String[0]);
    }

    /**
     * 检查JsonArray中是否包含某个内容
     * 为了兼容旧版本Gson
     * Gson狗都不用！焯！！！
     *
     * @param target  目标JsonArray
     * @param content 字符串内容
     * @return 是否包含
     */
    public static boolean jsonArrayContains(JsonArray target, String content) {
        if (IS_OLD_GSON) {
            for (JsonElement e : target) {
                if (e.isJsonPrimitive() && ((JsonPrimitive) e).isString()) {
                    if (e.getAsString().equals(content)) {
                        return true;
                    }
                }
            }
            return false;
        }

        return target.contains(new JsonPrimitive(content));
    }
}
