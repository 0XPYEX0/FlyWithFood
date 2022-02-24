package me.xpyex.plugin.flywithfood.common.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class ConfigUtil {
    public static final int CONFIG_VERSION = 5;
    public static FWFConfig CONFIG;
    public static JSONObject getNewConfig() {
        JSONObject outJson = new JSONObject();
        outJson.put("Cost", 4);
        outJson.put("Disable", 6);
        outJson.put("CheckSeconds", 1);
        outJson.put("ConfigVersion", CONFIG_VERSION);
        outJson.put("CostMode", "Food");
        outJson.put("Language", "Chinese");

        JSONObject rawMsgConfig = new JSONObject();
        rawMsgConfig.put("Enable", true);
        rawMsgConfig.put("On", "&b[飞行]&a你已经成功开启飞行！请注意你的%mode%");
        rawMsgConfig.put("Off", "&b[飞行]&a你已经关闭飞行！");
        rawMsgConfig.put("CannotEnable", "&b[飞行]&c%mode%不足，无法开启飞行!");
        rawMsgConfig.put("CannotFly", "&b[飞行]&c你的%mode%不足，已经自动关闭飞行");
        rawMsgConfig.put("HasEffect", "&9你无法在拥有饱和效果的情况下飞行");

        JSONObject titleMsgConfig = new JSONObject();
        titleMsgConfig.put("Enable", true);
        titleMsgConfig.put("On", "&b[飞行]\\n&a你已经成功开启飞行！请注意你的%mode%");
        titleMsgConfig.put("Off", "&b[飞行]\\n&a你已经关闭飞行！");
        titleMsgConfig.put("CannotEnable", "&b[飞行]\\n&c%mode%不足，无法开启飞行!");
        titleMsgConfig.put("CannotFly", "&b[飞行]\\n&c你的%mode%不足，已经自动关闭飞行");
        titleMsgConfig.put("HasEffect", "&9你无法在拥有饱和效果的情况下飞行");

        JSONObject actionMsgConfig = new JSONObject();
        actionMsgConfig.put("Enable", false);
        actionMsgConfig.put("On", "&a你已经成功开启飞行！请注意你的%mode%");
        actionMsgConfig.put("Off", "&a你已经关闭飞行！");
        actionMsgConfig.put("CannotEnable", "&c%mode%不足，无法开启飞行!");
        actionMsgConfig.put("CannotFly", "&c你的%mode%不足，已经自动关闭飞行");
        actionMsgConfig.put("HasEffect", "&9你无法在拥有饱和效果的情况下飞行");

        JSONObject helpMsgList = new JSONObject();
        JSONArray startHelpMsg = new JSONArray();
        startHelpMsg.add("&e你可以执行 &a/fly &e, &a/fwf &e或 &a/flywithfood &e来使用本插件");
        startHelpMsg.add("&9你目前可用的命令: ");
        JSONArray flyHelpMsg = new JSONArray();
        flyHelpMsg.add("&a%command% &b<on|off|toggle> &f- &e为你自己开启或关闭飞行");
        JSONArray otherHelpMsg = new JSONArray();
        otherHelpMsg.add("&a%command% &b<on|off|toggle> <在线玩家> &f- &e为指定玩家开启或关闭飞行");
        JSONArray adminHelpMsg = new JSONArray();
        adminHelpMsg.add("&a%command% &breload &f- &e重载配置");
        adminHelpMsg.add("&d以下为权限列表: ");
        adminHelpMsg.add("&afly.fly &f- &e允许玩家开启或关闭飞行");
        adminHelpMsg.add("&afly.nocost &f- &e允许玩家飞行时不消耗点数");
        adminHelpMsg.add("&afly.other &f- &e允许玩家开启或关闭他人的飞行");
        adminHelpMsg.add("&afly.admin &f- &e可收到权限列表");
        adminHelpMsg.add("&afly.groups.&2%GroupName% &f- &e将玩家归于配置文件的组内，%GroupName%为组名.如fly.groups.Group1，则将玩家归于Group1组");
        JSONArray endHelpMsg = new JSONArray();
        helpMsgList.put("Start", startHelpMsg);
        helpMsgList.put("Fly", flyHelpMsg);
        helpMsgList.put("Other", otherHelpMsg);
        helpMsgList.put("Admin", adminHelpMsg);
        helpMsgList.put("End", endHelpMsg);

        JSONObject modes = new JSONObject();
        modes.put("ExpLevel", "经验等级");
        modes.put("Food", "饥饿值");
        modes.put("ExpPoint", "经验值");
        modes.put("Money", "游戏币");

        JSONObject languages = new JSONObject();
        languages.put("RawMsg", rawMsgConfig);
        languages.put("TitleMsg", titleMsgConfig);
        languages.put("ActionMsg", actionMsgConfig);
        languages.put("HelpMsgList", helpMsgList);
        languages.put("NoPermission", "&c你没有权限");
        languages.put("DisableInThisWorld", "&c这个世界不允许使用这个命令");
        languages.put("PlayerOnly", "&c该命令仅允许玩家执行");
        languages.put("PlayerNotOnline", "&c玩家不存在");
        languages.put("Modes", modes);
        outJson.put("Languages", languages);

        JSONArray worlds = new JSONArray();
        worlds.add("world");
        worlds.add("world_nether");
        worlds.add("world_the_end");
        worlds.add("spawnworld");

        JSONObject FunctionsWhitelist = new JSONObject();
        FunctionsWhitelist.put("Enable", false);
        FunctionsWhitelist.put("Worlds", worlds);
        outJson.put("FunctionsWhitelist", FunctionsWhitelist);

        JSONObject NoCostWhitelist = new JSONObject();
        NoCostWhitelist.put("Enable", false);
        NoCostWhitelist.put("Worlds", worlds);
        outJson.put("NoCostWhitelist", NoCostWhitelist);

        JSONObject Groups = new JSONObject();
        JSONObject G1 = new JSONObject();
        G1.put("Cost", 2);
        G1.put("Disable", 6);
        G1.put("CostMode", "Food");
        Groups.put("Group1", G1);
        JSONObject G2 = new JSONObject();
        G2.put("Cost", 1);
        G2.put("Disable", 4);
        G2.put("CostMode", "ExpLevel");
        Groups.put("Group2", G2);
        JSONObject G3 = new JSONObject();
        G3.put("Cost", 1);
        G3.put("Disable", 4);
        G3.put("CostMode", "ExpPoint");
        Groups.put("Players_3", G3);
        outJson.put("Groups", Groups);

        return outJson;
    }

    public static int getLocalConfigVersion() {
        return CONFIG.config.containsKey("ConfigVersion") ? CONFIG.version : 0;
    }

    public static int getPluginConfigVersion() {
        return CONFIG_VERSION;
    }

    public static boolean needUpdate() {
        return getPluginConfigVersion() != getLocalConfigVersion();
    }
}