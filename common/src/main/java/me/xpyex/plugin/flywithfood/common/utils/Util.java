package me.xpyex.plugin.flywithfood.common.utils;

public class Util {
    /**
     * 检查填入的值是否为空，其中之一为空则返回true <br> 非null.
     *
     * @param objects 需要被检查的对象
     * @return 是否存在空值
     */
    public static boolean checkEmpty(Object... objects) {
        for (Object o : objects) {
            if (o == null)
                return true;

            if (o instanceof String)
                if (((String) o).trim().isEmpty())
                    return true;

            if (o instanceof String[])
                for (String s : ((String[]) o))
                    if (s.trim().isEmpty())
                        return true;
            /*
            危险代码，尚未测试

            if (o instanceof Iterator) {
                while (((Iterator<?>) o).hasNext()) {
                    boolean result = checkEmpty(((Iterator<?>) o).next());
                    if (result)
                        return true;
                }
            }

             */
        }
        return false;
    }

    /**
     * 检查传入的值是否为null，其中之一为null则返回true
     *
     * @param objects 需要被检查的对象
     * @return 是否存在null
     */
    public static boolean checkNull(Object... objects) {
        for (Object o : objects) {
            if (o == null)
                return true;
        }
        return false;
    }
}
