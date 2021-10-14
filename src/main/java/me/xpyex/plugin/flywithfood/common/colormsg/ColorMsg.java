package me.xpyex.plugin.flywithfood.common.colormsg;

public class ColorMsg {
    public static String getColorMsg(String msg) {
        if (msg == null) {
            throw new NullPointerException("获取彩色字符串出现空，请联系开发者 QQ1723275529");
        }
        msg = msg.replace("&0", "§0");
        msg = msg.replace("&1", "§1");
        msg = msg.replace("&2", "§2");
        msg = msg.replace("&3", "§3");
        msg = msg.replace("&4", "§4");
        msg = msg.replace("&5", "§5");
        msg = msg.replace("&6", "§6");
        msg = msg.replace("&7", "§7");
        msg = msg.replace("&8", "§8");
        msg = msg.replace("&9", "§9");
        msg = msg.replace("&a", "§a");
        msg = msg.replace("&b", "§b");
        msg = msg.replace("&c", "§c");
        msg = msg.replace("&d", "§d");
        msg = msg.replace("&e", "§e");
        msg = msg.replace("&f", "§f");
        msg = msg.replace("&o", "§o");
        msg = msg.replace("&k", "§k");
        msg = msg.replace("&l", "§l");
        msg = msg.replace("&m", "§m");
        msg = msg.replace("&n", "§n");
        msg = msg.replace("&r", "§r");
        return msg;
    }
}
