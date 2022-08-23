package me.xpyex.plugin.flywithfood.common.utils;

public class Util {
    /**
     * 检查填入的值是否为空，其中之一为空则返回true
     *
     * @param objects 需要被检查的对象
     * @return 是否存在空值
     */
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
