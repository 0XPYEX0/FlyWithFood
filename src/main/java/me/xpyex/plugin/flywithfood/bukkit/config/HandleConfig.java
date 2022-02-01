package me.xpyex.plugin.flywithfood.bukkit.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import me.xpyex.plugin.flywithfood.bukkit.FlyWithFood;
import me.xpyex.plugin.flywithfood.bukkit.events.FWFReloadConfigEvent;
import me.xpyex.plugin.flywithfood.bukkit.implementations.energys.EnergyManager;
import me.xpyex.plugin.flywithfood.bukkit.reflections.NMSAll;
import me.xpyex.plugin.flywithfood.bukkit.utils.VersionUtil;
import me.xpyex.plugin.flywithfood.common.config.ConfigUtil;
import me.xpyex.plugin.flywithfood.common.config.FWFConfig;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;

public class HandleConfig {
    public static FWFConfig config;
    private final static File ROOT = new File("plugins/" + FlyWithFood.INSTANCE.getName());
    private final static File CONFIG_FILE = new File("plugins/" + FlyWithFood.INSTANCE.getName() + "/config.json");
    private final static File HOW_TO_CONFIG_FILE_CH = new File("plugins/" + FlyWithFood.INSTANCE.getName() + "/HowToConfig-CH.txt");
    private final static File HOW_TO_CONFIG_FILE_EN = new File("plugins/" + FlyWithFood.INSTANCE.getName() + "/HowToConfig-EN.txt");
    private final static File BAK_FOLDER = new File("plugins/" + FlyWithFood.INSTANCE.getName() + "/bakConfig");
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
            StringBuilder configText = new StringBuilder();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    new FileInputStream(CONFIG_FILE), StandardCharsets.UTF_8));  //Scanner会分割空格，导致消息缺失部分
            String line;
            while ((line = in.readLine()) != null) {
                if (line.contains("//")) {
                    line = line.split("//")[0];  //不读取注释
                }
                configText.append(line);
            }
            in.close();
            config = new FWFConfig(JSON.parseObject(configText.toString()));

            if (!EnergyManager.hasEnergy(config.mode)) {
                FlyWithFood.LOGGER.severe("CostMode错误！CostMode只应为 " + Arrays.toString(EnergyManager.getEnergys()) + " 中的一种");
                FlyWithFood.LOGGER.severe("Wrong!! CostMode does not exists! You can use these: " + Arrays.toString(EnergyManager.getEnergys()));
                return false;
            }

            enableRawMsg = config.languages.getJSONObject("RawMsg").getBoolean("Enable");
            enableTitle = config.languages.getJSONObject("TitleMsg").getBoolean("Enable");
            enableAction = config.languages.getJSONObject("ActionMsg").getBoolean("Enable");
            if (enableTitle) {
                try {
                    Player.class.getMethod("sendTitle", String.class, String.class, int.class, int.class, int.class);
                    new String("检查是否支持Title信息的方法(非常粗暴");
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
                    FlyWithFood.LOGGER.info("当前服务端NMS版本: " + NMSAll.nmsVer);
                    FlyWithFood.LOGGER.warning(" ");
                    FlyWithFood.LOGGER.warning("Your server does not support sending Action Messages directly!");
                    FlyWithFood.LOGGER.warning("FlyWithFood will try to use NMS to send Action Messages.");
                    FlyWithFood.LOGGER.info("The NMS Version of your server: " + NMSAll.nmsVer);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return config != null;
    }

    public static void createConfigFile() throws Exception {
        createConfigFile(getNewConfig());
    }

    public static void createConfigFile(JSONObject json) throws Exception {
        if (!ROOT.exists()) {
            ROOT.mkdirs();
        }
        CONFIG_FILE.createNewFile();

        PrintWriter out = new PrintWriter(CONFIG_FILE, "UTF-8");
        out.write(JSON.toJSONString(json, true));
        out.flush();
        out.close();
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
            File targetFile = new File("plugins/" + FlyWithFood.INSTANCE.getName() + "/bakConfig/config_" + time + ".json");
            CONFIG_FILE.renameTo(targetFile);
            JSONObject newJO = getNewConfig();
            for (String value : config.languages.keySet()) {
                newJO.getJSONObject("Languages").put(value, config.languages.get(value));
            }
            for (String value : config.config.keySet()) {
                if (value.equals("Languages")) {  //在上文处理了
                    continue;
                }
                if (value.contains("Food")) {
                    newJO.put(value.replace("Food", ""), config.config.get(value));  //更名
                    continue;
                }
                newJO.put(value, config.config.get(value));
            }
            newJO.put("ConfigVersion", VersionUtil.getPluginConfigVersion());
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
        JSONObject oldConfig = (JSONObject) config.config.clone();
        config = null;
        enableRawMsg = false;
        enableAction = false;
        enableTitle = false;
        Bukkit.getScheduler().cancelTasks(FlyWithFood.INSTANCE);
        FWFReloadConfigEvent event = new FWFReloadConfigEvent(oldConfig);
        Bukkit.getPluginManager().callEvent(event);
        loadConfig();
        return config != null;
    }

    public static JSONObject getNewConfig() {
        return ConfigUtil.getNewConfig();
    }
}