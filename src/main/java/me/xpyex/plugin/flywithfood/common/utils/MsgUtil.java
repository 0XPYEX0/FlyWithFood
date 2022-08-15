package me.xpyex.plugin.flywithfood.common.utils;

public class MsgUtil {
    private static final String[] CHECK_STRING = "1234567890abcdefklonmr".split("");

    public static String getColorMsg(String msg) {
        if (msg == null) {
            throw new NullPointerException("获取彩色字符串出现空，请联系开发者 QQ1723275529");
        }
        if (msg.isEmpty()) {
            return msg;
        }
        for (String s : CHECK_STRING) {
            msg = msg.replace("&" + s.toLowerCase(), "§" + s.toLowerCase());
            msg = msg.replace("&" + s.toUpperCase(), "§" + s.toUpperCase());
        }
        return msg;
    }
}
