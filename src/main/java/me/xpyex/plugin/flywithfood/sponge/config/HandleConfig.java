package me.xpyex.plugin.flywithfood.sponge.config;

import me.xpyex.plugin.flywithfood.common.config.Config;
import me.xpyex.plugin.flywithfood.sponge.utils.VersionUtil;
import me.xpyex.plugin.flywithfood.sponge.FlyWithFood;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.spongepowered.api.effect.Viewer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.title.Title;

import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class HandleConfig {
    public static JSONObject config;
    static File root = new File("config/FlyWithFood/");
    static File configFile = new File("config/FlyWithFood/config.json");
    static File howToConfigFile = new File("config/FlyWithFood/HowToConfig.txt");
    static File bakFolder = new File("config/FlyWithFood/bakConfig");
    public static boolean enableRawMsg;
    public static boolean enableTitle;
    public static boolean enableAction;
    public static boolean functionWL;
    public static boolean noCostWL;



    public static boolean loadConfig() {
        try {
            if (!root.exists()) {
                FlyWithFood.logger.info("第一次加载？正在生成配置文件!");
                createConfigFile();
                createHowToConfigFile();
            }
            if (!configFile.exists()) {
                FlyWithFood.logger.info("配置文件丢失？正在生成配置文件!");
                createConfigFile();
            }
            if (!howToConfigFile.exists()) {
                FlyWithFood.logger.info("教程文件丢失？正在生成教程文件!");
                createHowToConfigFile();
            }
            Scanner in = new Scanner(configFile, "UTF-8");
            StringBuilder configText = new StringBuilder();
            while (in.hasNext()) {
                configText.append(in.next());
            }
            in.close();
            config = JSON.parseObject(configText.toString());

            enableRawMsg = config.getJSONObject("Languages").getJSONObject("RawMsg").getBoolean("Enable");
            enableTitle = config.getJSONObject("Languages").getJSONObject("TitleMsg").getBoolean("Enable");
            enableAction = config.getJSONObject("Languages").getJSONObject("ActionMsg").getBoolean("Enable");


            boolean supportTitleMsg = true;
            try {
                Viewer.class.getMethod("sendTitle", Title.class);
                new String("检查是否支持Title信息的方法(非常粗暴");
            } catch (Throwable ignored) {
                supportTitleMsg = false;
            }
            if (enableTitle && !supportTitleMsg) {
                FlyWithFood.logger.warn("你的服务器不支持发送Title信息!");
                FlyWithFood.logger.warn("请在配置中禁用Title信息!");
                enableTitle = false;
            }

            boolean supportActionMsg = true;
            try {
                Title.builder().actionBar(Text.of("检查是否支持Action信息的方法(非常粗暴"));
            } catch (Throwable ignored) {
                supportActionMsg = false;
            }
            if (enableAction && !supportActionMsg) {
                FlyWithFood.logger.warn("你的服务器不支持发送Action信息!");
                FlyWithFood.logger.warn("请在配置中禁用Action信息!");
                enableAction = false;
            }
            FlyWithFood.startCheck();
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
        if (!root.exists()) {
            root.mkdirs();
        }
        configFile.createNewFile();

        PrintWriter out = new PrintWriter(configFile, "UTF-8");
        out.write(JSON.toJSONString(json, true));
        out.flush();
        out.close();
    }

    public static void updateConfigFile() {
        try {
            if (!HandleConfig.configFile.exists()) {
                createConfigFile();
                return;
            }
            if (!bakFolder.exists()) {
                bakFolder.mkdirs();
            }
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
            String time = format.format(new Date());
            File targetFile = new File("config/FlyWithFood/bakConfig/config_" + time + ".json");
            configFile.renameTo(targetFile);
            JSONObject newJO = getNewConfig();
            for (String value : config.keySet()) {
                newJO.put(value, config.get(value));
            }
            if (!newJO.getJSONObject("Languages").containsKey("DisableInThisWorld")) {
                newJO.getJSONObject("Languages").put("DisableInThisWorld", "&c这个世界不允许使用这个命令");
            }
            if (!newJO.getJSONObject("Languages").containsKey("NoPermission")) {
                newJO.getJSONObject("Languages").put("NoPermission", "&c你没有权限");
            }
            newJO.put("ConfigVersion", VersionUtil.getPluginConfigVersion());
            createConfigFile(newJO);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createHowToConfigFile() throws Exception {
        howToConfigFile.createNewFile();

        PrintWriter out = new PrintWriter(howToConfigFile, "UTF-8");
        out.println("ConfigVersion: 配置文件版本，更新用，请勿自行调节！");
        out.println("FoodCost: 每秒消耗的饥饿值,每一格为2点");
        out.println("FoodDisable: 饥饿值小于该值则关闭飞行");
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
        out.println("HelpMsgType: 当执行/fly等命令时展示的样式，共2种");
        out.close();
    }

    public static boolean reloadConfig() {
        config = null;
        enableRawMsg = false;
        enableAction = false;
        enableTitle = false;
        FlyWithFood.cancelTasks();
        loadConfig();
        return config != null;
    }

    public static JSONObject getNewConfig() {
        return Config.getNewConfig();
    }
}
