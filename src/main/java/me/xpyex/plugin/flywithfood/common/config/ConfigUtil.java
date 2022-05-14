package me.xpyex.plugin.flywithfood.common.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.util.HashSet;

public class ConfigUtil {
    public static final int CONFIG_VERSION = 5;
    public static FWFConfig CONFIG;
    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();
    public static boolean isOldGson;  //基元类型不为空，所以用非基元类型

    static {
        try {
            new JsonArray().contains(new JsonPrimitive("CheckIsOld"));
            isOldGson = false;
        } catch (NoSuchMethodError ignored) {
            isOldGson = true;
        }
    }

    /**
     * 获取全新的配置文件
     *
     * @return 新的配置文件
     */
    public static JsonObject getNewConfig() {
        JsonObject outJson = new JsonObject();
        outJson.addProperty("Cost", 4);
        outJson.addProperty("Disable", 6);
        outJson.addProperty("CheckSeconds", 1);
        outJson.addProperty("ConfigVersion", CONFIG_VERSION);
        outJson.addProperty("CostMode", "Food");
        outJson.addProperty("Language", "Chinese");

        JsonObject rawMsgConfig = new JsonObject();
        rawMsgConfig.addProperty("Enable", true);
        rawMsgConfig.addProperty("On", "&b[飞行]&a你已经成功开启飞行！请注意你的%mode%");
        rawMsgConfig.addProperty("Off", "&b[飞行]&a你已经关闭飞行！");
        rawMsgConfig.addProperty("CannotEnable", "&b[飞行]&c%mode%不足，无法开启飞行!");
        rawMsgConfig.addProperty("CannotFly", "&b[飞行]&c你的%mode%不足，已经自动关闭飞行");
        rawMsgConfig.addProperty("HasEffect", "&9你无法在拥有饱和效果的情况下飞行");

        JsonObject titleMsgConfig = new JsonObject();
        titleMsgConfig.addProperty("Enable", true);
        titleMsgConfig.addProperty("On", "&b[飞行]\\n&a你已经成功开启飞行！请注意你的%mode%");
        titleMsgConfig.addProperty("Off", "&b[飞行]\\n&a你已经关闭飞行！");
        titleMsgConfig.addProperty("CannotEnable", "&b[飞行]\\n&c%mode%不足，无法开启飞行!");
        titleMsgConfig.addProperty("CannotFly", "&b[飞行]\\n&c你的%mode%不足，已经自动关闭飞行");
        titleMsgConfig.addProperty("HasEffect", "&9你无法在拥有饱和效果的情况下飞行");

        JsonObject actionMsgConfig = new JsonObject();
        actionMsgConfig.addProperty("Enable", false);
        actionMsgConfig.addProperty("On", "&a你已经成功开启飞行！请注意你的%mode%");
        actionMsgConfig.addProperty("Off", "&a你已经关闭飞行！");
        actionMsgConfig.addProperty("CannotEnable", "&c%mode%不足，无法开启飞行!");
        actionMsgConfig.addProperty("CannotFly", "&c你的%mode%不足，已经自动关闭飞行");
        actionMsgConfig.addProperty("HasEffect", "&9你无法在拥有饱和效果的情况下飞行");

        JsonObject helpMsgList = new JsonObject();
        JsonArray startHelpMsg = new JsonArray();
        startHelpMsg.add(new JsonPrimitive("&e你可以执行 &a/fly &e, &a/fwf &e或 &a/flywithfood &e来使用本插件"));
        //旧版本Gson没有JsonArray.add(String)，但有JsonArray.add(JsonElement)
        startHelpMsg.add(new JsonPrimitive("&9你目前可用的命令: "));
        JsonArray flyHelpMsg = new JsonArray();
        flyHelpMsg.add(new JsonPrimitive("&a%command% &b<on|off|toggle> &f- &e为你自己开启或关闭飞行"));
        JsonArray otherHelpMsg = new JsonArray();
        otherHelpMsg.add(new JsonPrimitive("&a%command% &b<on|off|toggle> <在线玩家> &f- &e为指定玩家开启或关闭飞行"));
        JsonArray adminHelpMsg = new JsonArray();
        adminHelpMsg.add(new JsonPrimitive("&a%command% &breload &f- &e重载配置"));
        adminHelpMsg.add(new JsonPrimitive("&d以下为权限列表: "));
        adminHelpMsg.add(new JsonPrimitive("&afly.fly &f- &e允许玩家开启或关闭飞行"));
        adminHelpMsg.add(new JsonPrimitive("&afly.nocost &f- &e允许玩家飞行时不消耗点数"));
        adminHelpMsg.add(new JsonPrimitive("&afly.other &f- &e允许玩家开启或关闭他人的飞行"));
        adminHelpMsg.add(new JsonPrimitive("&afly.admin &f- &e可收到权限列表"));
        adminHelpMsg.add(new JsonPrimitive("&afly.groups.&2%GroupName% &f- &e将玩家归于配置文件的组内，%GroupName%为组名.如fly.groups.Group1，则将玩家归于Group1组"));
        JsonArray endHelpMsg = new JsonArray();
        helpMsgList.add("Start", startHelpMsg);
        helpMsgList.add("Fly", flyHelpMsg);
        helpMsgList.add("Other", otherHelpMsg);
        helpMsgList.add("Admin", adminHelpMsg);
        helpMsgList.add("End", endHelpMsg);

        JsonObject modes = new JsonObject();
        modes.addProperty("ExpLevel", "经验等级");
        modes.addProperty("Food", "饥饿值");
        modes.addProperty("ExpPoint", "经验值");
        modes.addProperty("Money", "游戏币");

        JsonObject languages = new JsonObject();
        languages.add("RawMsg", rawMsgConfig);
        languages.add("TitleMsg", titleMsgConfig);
        languages.add("ActionMsg", actionMsgConfig);
        languages.add("HelpMsgList", helpMsgList);
        languages.addProperty("NoPermission", "&c你没有权限");
        languages.addProperty("DisableInThisWorld", "&c这个世界不允许使用这个命令");
        languages.addProperty("PlayerOnly", "&c该命令仅允许玩家执行");
        languages.addProperty("PlayerNotOnline", "&c玩家不存在");
        languages.add("Modes", modes);
        outJson.add("Languages", languages);

        JsonArray worlds = new JsonArray();
        worlds.add(new JsonPrimitive("world"));
        worlds.add(new JsonPrimitive("world_nether"));
        worlds.add(new JsonPrimitive("world_the_end"));
        worlds.add(new JsonPrimitive("spawnworld"));

        JsonObject FunctionsWhitelist = new JsonObject();
        FunctionsWhitelist.addProperty("Enable", false);
        FunctionsWhitelist.add("Worlds", worlds);
        outJson.add("FunctionsWhitelist", FunctionsWhitelist);

        JsonObject NoCostWhitelist = new JsonObject();
        NoCostWhitelist.addProperty("Enable", false);
        NoCostWhitelist.add("Worlds", worlds);
        outJson.add("NoCostWhitelist", NoCostWhitelist);

        JsonObject Groups = new JsonObject();
        JsonObject G1 = new JsonObject();
        G1.addProperty("Cost", 2);
        G1.addProperty("Disable", 6);
        G1.addProperty("CostMode", "Food");
        Groups.add("Group1", G1);
        JsonObject G2 = new JsonObject();
        G2.addProperty("Cost", 1);
        G2.addProperty("Disable", 4);
        G2.addProperty("CostMode", "ExpLevel");
        Groups.add("Group2", G2);
        JsonObject G3 = new JsonObject();
        G3.addProperty("Cost", 1);
        G3.addProperty("Disable", 4);
        G3.addProperty("CostMode", "ExpPoint");
        Groups.add("Players_3", G3);
        outJson.add("Groups", Groups);

        return outJson;
    }

    /**
     * 获取本地的配置文件版本
     *
     * @return 配置文件版本
     */
    public static int getLocalConfigVersion() {
        return CONFIG.config.has("ConfigVersion") ? CONFIG.version : 0;
    }

    /**
     * 插件应使用的配置文件版本
     *
     * @return 新的配置文件版本
     */
    public static int getPluginConfigVersion() {
        return CONFIG_VERSION;
    }

    /**
     * 配置文件是否需要更新
     *
     * @return 是否需要更新
     */
    public static boolean needUpdate() {
        return getPluginConfigVersion() != getLocalConfigVersion();
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
        if (isOldGson) {
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