package me.xpyex.plugin.flywithfood.bukkit.config;

import com.google.gson.JsonObject;
import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import me.xpyex.plugin.flywithfood.bukkit.FlyWithFood;
import me.xpyex.plugin.flywithfood.bukkit.reflections.NMSAll;
import me.xpyex.plugin.flywithfood.common.config.ConfigUtil;
import me.xpyex.plugin.flywithfood.common.implementations.flyenergy.EnergyManager;
import me.xpyex.plugin.flywithfood.common.utils.FileUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class HandleConfig {
    private final static File ROOT = FlyWithFood.INSTANCE.getDataFolder();
    private final static File CONFIG_FILE = new File(ROOT, "config.json");
    private final static File HOW_TO_CONFIG_FILE_CH = new File(ROOT, "HowToConfig-CH.txt");
    private final static File HOW_TO_CONFIG_FILE_EN = new File(ROOT, "HowToConfig-EN.txt");
    private final static File BAK_FOLDER = new File(ROOT, "bakConfig");
    public static boolean enableRawMsg;
    public static boolean enableTitle;
    public static boolean enableAction;
    public static boolean functionWL;
    public static boolean noCostWL;
    public static boolean isOldVer = false;

    public static boolean loadConfig() {
        try {
            if (!ROOT.exists()) {
                FlyWithFood.LOGGER.info("第一次加载？正在生成配置文件!");
                FlyWithFood.LOGGER.info("Are you using this plugin for the first time? The configuration file is being generated for you!");
                FlyWithFood.LOGGER.info(" ");
                createConfigFile();
                createHowToConfigFile();
            }
            if (!CONFIG_FILE.exists()) {
                FlyWithFood.LOGGER.info("配置文件丢失？正在生成配置文件!");
                FlyWithFood.LOGGER.info("Lost your configuration file? The configuration file is being generated for you!");
                FlyWithFood.LOGGER.info(" ");
                createConfigFile();
            }
            if (!HOW_TO_CONFIG_FILE_CH.exists()) {
                FlyWithFood.LOGGER.info("教程文件丢失？正在生成教程文件!");
                FlyWithFood.LOGGER.info("Lost your tutorial files? The configuration file is being generated for you!");
                FlyWithFood.LOGGER.info(" ");
                createHowToConfigFile();
            }
            ConfigUtil.CONFIG = new BukkitConfig(ConfigUtil.GSON.fromJson(FileUtil.readFile(CONFIG_FILE), JsonObject.class));

            if (!EnergyManager.hasEnergy(ConfigUtil.CONFIG.mode)) {
                FlyWithFood.LOGGER.severe("CostMode错误！CostMode只应为 " + Arrays.toString(EnergyManager.getEnergys()) + " 中的一种. -> " + ConfigUtil.CONFIG.mode);
                FlyWithFood.LOGGER.severe("ERROR!! CostMode does not exists! You can use these: " + Arrays.toString(EnergyManager.getEnergys()) + ". -> " + ConfigUtil.CONFIG.mode);
                return false;
            }

            functionWL = ConfigUtil.CONFIG.functionWL.get("Enable").getAsBoolean();
            noCostWL = ConfigUtil.CONFIG.noCostWL.get("Enable").getAsBoolean();  //重载的时候用

            enableRawMsg = ConfigUtil.CONFIG.languages.get("RawMsg").getAsJsonObject().get("Enable").getAsBoolean();
            enableTitle = ConfigUtil.CONFIG.languages.get("TitleMsg").getAsJsonObject().get("Enable").getAsBoolean();
            enableAction = ConfigUtil.CONFIG.languages.get("ActionMsg").getAsJsonObject().get("Enable").getAsBoolean();
            if (enableTitle) {
                try {
                    Player.class.getMethod("sendTitle", String.class, String.class, int.class, int.class, int.class);
                    /*
                    * 检查是否支持Title信息的方法(非常粗暴
                    * 反正也开源了，不需要用new String做注释了
                    * 早期用new String是因为没开源，可能有人反编译读我代码看不明白
                    */
                } catch (Throwable ignored) {
                    try {
                        Player.class.getMethod("sendTitle", String.class, String.class);
                        isOldVer = true;
                    } catch (Throwable ignored2) {
                        FlyWithFood.LOGGER.warning("你的服务器不支持发送Title信息!");
                        FlyWithFood.LOGGER.warning("请在配置文件禁用Title信息");
                        FlyWithFood.LOGGER.warning(" ");
                        FlyWithFood.LOGGER.warning("Your server does not support sending Title Messages!");
                        FlyWithFood.LOGGER.warning("Please disable this function in config file.");
                        enableTitle = false;
                    }
                }
            }
            if (enableAction) {
                try {
                    ChatMessageType.valueOf("ACTION_BAR");
                    TextComponent.fromLegacyText("检查是否支持ActionBar的方法(非常粗暴");
                    Player.Spigot.class.getMethod("sendMessage", ChatMessageType.class, BaseComponent.class);
                } catch (Throwable ignored) {
                    NMSAll.shouldUseNMSAction = true;
                    FlyWithFood.LOGGER.warning("你的服务器不支持直接发送Action信息!");
                    FlyWithFood.LOGGER.warning("将尝试调用NMS以发送Action信息");
                    FlyWithFood.LOGGER.info("当前服务端NMS版本: " + NMSAll.NMS_VERSION);
                    FlyWithFood.LOGGER.warning(" ");
                    FlyWithFood.LOGGER.warning("Your server does not support sending Action Messages directly!");
                    FlyWithFood.LOGGER.warning("FlyWithFood will try to use NMS to send Action Messages.");
                    FlyWithFood.LOGGER.info("The NMS Version of your server: " + NMSAll.NMS_VERSION);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return ConfigUtil.CONFIG != null;
    }

    public static void createConfigFile() throws Exception {
        createConfigFile(getNewConfig());
    }

    public static void createConfigFile(JsonObject json) throws Exception {
        if (!ROOT.exists()) {
            ROOT.mkdirs();
        }
        CONFIG_FILE.createNewFile();

        FileUtil.writeFile(CONFIG_FILE, ConfigUtil.GSON.toJson(json));
    }

    public static void updateConfigFile() {
        try {
            if (!HandleConfig.CONFIG_FILE.exists()) {
                createConfigFile();
                return;
            }
            if (!BAK_FOLDER.exists()) {
                BAK_FOLDER.mkdirs();
            }
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
            String time = format.format(new Date());
            File targetFile = new File(BAK_FOLDER, "config_" + time + ".json");
            CONFIG_FILE.renameTo(targetFile);
            JsonObject newJO = getNewConfig();
            for (String value : ConfigUtil.getKeysOfJsonObject(ConfigUtil.CONFIG.languages)) {
                newJO.get("Languages").getAsJsonObject().add(value, ConfigUtil.CONFIG.languages.get(value));
            }
            for (String value : ConfigUtil.getKeysOfJsonObject(ConfigUtil.CONFIG.config)) {
                if (value.equals("Languages")) {  //在上文处理了
                    continue;
                }
                if (value.contains("Food")) {
                    newJO.add(value.replace("Food", ""), ConfigUtil.CONFIG.config.get(value));  //更名
                    continue;
                }
                newJO.add(value, ConfigUtil.CONFIG.config.get(value));
            }
            newJO.addProperty("ConfigVersion", ConfigUtil.getPluginConfigVersion());
            createConfigFile(newJO);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createHowToConfigFile() throws Exception {
        {
            HOW_TO_CONFIG_FILE_CH.createNewFile();

            PrintWriter out = new PrintWriter(HOW_TO_CONFIG_FILE_CH, "UTF-8");
            out.println("ConfigVersion: 配置文件版本，更新用，请勿自行调节！");
            out.println("FoodCost: 每个周期消耗的饥饿值,每一格为2点");
            out.println("FoodDisable: 饥饿值小于该值则关闭飞行");
            out.println("CheckSeconds: 每X秒检查一次饥饿值");
            out.println("语言文件按需调节");
            out.println("\\n表换行,仅Title信息可换行");
            out.println("RawMsg: 普通文本消息,显示在左下角聊天框");
            out.println("TitleMsg: Title信息,显示在屏幕中央");
            out.println("ActionMsg: Action信息,显示在快捷物品栏上方");
            out.println("NoPermission: 当玩家无权执行命令时的消息");
            out.println("DisableInThisWorld: 当FunctionsWhitelist被开启，且玩家所在世界在列表中，阻止玩家执行命令时的消息");
            out.println("信息留空则不发送该条");
            out.println("FunctionsWhitelist: Enable为是否开启功能白名单，若开启则只允许在下方列表所列世界内使用本插件功能");
            out.println("NoFoodCostWhitelist: Enable为是否开启消耗饥饿值白名单，若开启则在下方列表所列世界内不会被扣除饥饿值");
            out.flush();
            out.close();
        }
        {
            HOW_TO_CONFIG_FILE_EN.createNewFile();
            PrintWriter out = new PrintWriter(HOW_TO_CONFIG_FILE_EN, "UTF-8");
            out.println("ConfigVersion: Config File Version, DO NOT CHANGE THIS!!!");
            out.println("FoodCost: How much saturation that players cost every period");
            out.println("FoodDisable: If saturation of player less than this, plugin will disable his flight");
            out.println("CheckSeconds: Check saturation of everyone per X seconds");
            out.println(" ");
            out.println("Languages:");
            out.println("\\n means to next line, Only applicable in Title Messages");
            out.println("RawMsg: Normal Text Messages, displayed in the lower left corner");
            out.println("TitleMsg: Title Messages, display in the center of the screen");
            out.println("ActionMsg: Action Messages, displayed above the quick inventory");
            out.println("NoPermission: When a player does not have permission to use command, plugin will send this to him");
            out.println("DisableInThisWorld: When 'FunctionsWhitelist' is been enabled, and a player try to use our command in the world, plugin will send this to him");
            out.println("FunctionsWhitelist: ");
            out.flush();
            out.close();
        }
    }

    public static boolean reloadConfig() {
        ConfigUtil.CONFIG = null;
        enableRawMsg = false;
        enableAction = false;
        enableTitle = false;
        Bukkit.getScheduler().cancelTasks(FlyWithFood.INSTANCE);
        boolean result = loadConfig();
        if (result) Bukkit.getScheduler().runTaskLater(FlyWithFood.INSTANCE, FlyWithFood::startCheck, 1L);
        return result;
    }

    public static JsonObject getNewConfig() {
        return ConfigUtil.getNewConfig();
        //
    }
}