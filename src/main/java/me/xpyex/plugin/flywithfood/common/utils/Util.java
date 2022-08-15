package me.xpyex.plugin.flywithfood.common.utils;

public class Util {
    public static boolean checkNull(Object... objects) {
        for (Object o : objects) {
            if (o == null)
                return true;

            if (o instanceof String)
                if (((String) o).trim().isEmpty())
                    return true;
        }
        return false;
    }
}
