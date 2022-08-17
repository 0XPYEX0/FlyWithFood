package me.xpyex.plugin.flywithfood.common.utils;

import java.util.Arrays;
import me.xpyex.plugin.flywithfood.common.config.FWFConfig;
import me.xpyex.plugin.flywithfood.common.flyenergy.EnergyManager;
import me.xpyex.plugin.flywithfood.common.implementation.FWFSender;

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

    public static String formatMsg(FWFSender target, String msg) {
        if (target.hasPermission("op")) {
            return msg.replace("%mode%", "点数");
        }
        String mode = FWFConfig.CONFIG.mode;  //消耗什么数值
        for (String groupName : GsonUtil.getKeysOfJsonObject(FWFConfig.CONFIG.groups)) {
            if (target.hasPermission("fly.groups." + groupName)) {
                mode = FWFConfig.CONFIG.groups.get(groupName).getAsJsonObject().get("CostMode").getAsString();  //重新分配分组中的值
                break;
            }
        }
        if (!FWFConfig.CONFIG.languages.get("Modes").getAsJsonObject().has(mode)) {
            throw new IllegalStateException(target.getName() + " 的CostMode出现错误！无法正确替换信息！请检查配置文件！CostMode只应出现" + Arrays.toString(EnergyManager.getEnergies()));
        }
        return msg.replace("%mode%", FWFConfig.CONFIG.languages.get("Modes").getAsJsonObject().get(mode).getAsString());
    }

}
