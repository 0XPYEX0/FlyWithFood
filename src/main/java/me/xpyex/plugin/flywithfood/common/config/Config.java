package me.xpyex.plugin.flywithfood.common.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Config {
    public static final String PLUGIN_VERSION = "1.2.6";
    public static final int CONFIG_VERSION = 4;
    public static JSONObject getNewConfig() {
        JSONObject outJson = new JSONObject();
        outJson.put("FoodCost", 4);
        outJson.put("FoodDisable", 6);
        outJson.put("CheckSeconds", 1);
        outJson.put("ConfigVersion", CONFIG_VERSION);
        outJson.put("Language", "Chinese");

        JSONObject rawMsgConfig = new JSONObject();
        rawMsgConfig.put("Enable", true);
        rawMsgConfig.put("On", "&b[飞行]&a你已经成功开启飞行！请注意你的饥饿值");
        rawMsgConfig.put("Off", "&b[飞行]&a你已经关闭飞行！");
        rawMsgConfig.put("CannotEnable", "&b[飞行]&c饥饿值不足，无法开启飞行!");
        rawMsgConfig.put("CannotFly", "&b[飞行]&c你的饥饿值不足，已经自动关闭飞行");
        rawMsgConfig.put("HasEffect", "&9你无法在拥有饱和效果的情况下飞行");

        JSONObject titleMsgConfig = new JSONObject();
        titleMsgConfig.put("Enable", true);
        titleMsgConfig.put("On", "&b[飞行]\\n&a你已经成功开启飞行！请注意你的饥饿值");
        titleMsgConfig.put("Off", "&b[飞行]\\n&a你已经关闭飞行！");
        titleMsgConfig.put("CannotEnable", "&b[飞行]\\n&c饥饿值不足，无法开启飞行!");
        titleMsgConfig.put("CannotFly", "&b[飞行]\\n&c你的饥饿值不足，已经自动关闭飞行");
        titleMsgConfig.put("HasEffect", "&9你无法在拥有饱和效果的情况下飞行");

        JSONObject actionMsgConfig = new JSONObject();
        actionMsgConfig.put("Enable", false);
        actionMsgConfig.put("On", "&a你已经成功开启飞行！请注意你的饥饿值");
        actionMsgConfig.put("Off", "&a你已经关闭飞行！");
        actionMsgConfig.put("CannotEnable", "&c饥饿值不足，无法开启飞行!");
        actionMsgConfig.put("CannotFly", "&c你的饥饿值不足，已经自动关闭飞行");
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
        adminHelpMsg.add("&afly.nohunger &f- &e允许玩家飞行时不消耗饥饿值");
        adminHelpMsg.add("&afly.other &f- &e允许玩家开启或关闭他人的飞行");
        adminHelpMsg.add("&afly.admin &f- &e可收到权限列表");
        JSONArray endHelpMsg = new JSONArray();
        helpMsgList.put("Start", startHelpMsg);
        helpMsgList.put("Fly", flyHelpMsg);
        helpMsgList.put("Other", otherHelpMsg);
        helpMsgList.put("Admin", adminHelpMsg);
        helpMsgList.put("End", endHelpMsg);

        JSONObject languages = new JSONObject();
        languages.put("RawMsg", rawMsgConfig);
        languages.put("TitleMsg", titleMsgConfig);
        languages.put("ActionMsg", actionMsgConfig);
        languages.put("HelpMsgList", helpMsgList);
        languages.put("NoPermission", "&c你没有权限");
        languages.put("DisableInThisWorld", "&c这个世界不允许使用这个命令");
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

        JSONObject NoCostFoodWhitelist = new JSONObject();
        NoCostFoodWhitelist.put("Enable", false);
        NoCostFoodWhitelist.put("Worlds", worlds);
        outJson.put("NoCostFoodWhitelist", NoCostFoodWhitelist);

        return outJson;
    }
}