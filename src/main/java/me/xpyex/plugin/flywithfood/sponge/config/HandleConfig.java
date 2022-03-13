package me.xpyex.plugin.flywithfood.sponge.config;

import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import me.xpyex.plugin.flywithfood.common.config.ConfigUtil;
import me.xpyex.plugin.flywithfood.common.implementations.flyenergy.EnergyManager;
import me.xpyex.plugin.flywithfood.sponge.FlyWithFood;
import org.spongepowered.api.effect.Viewer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.title.Title;

public class HandleConfig {
    private static final File ROOT = new File("config/FlyWithFood/");
    private static final File CONFIG_FILE = new File(ROOT, "config.json");
    private static final File HOW_TO_CONFIG_FILE = new File(ROOT, "HowToConfig.txt");
    private static final File BAK_FOLDER = new File(ROOT, "bakConfig");
    public static boolean enableRawMsg;
    public static boolean enableTitle;
    public static boolean enableAction;
    public static boolean functionWL;
    public static boolean noCostWL;



    public static boolean loadConfig() {
        try {
            if (!ROOT.exists()) {
                FlyWithFood.LOGGER.info("第一次加载？正在生成配置文件!");
                createConfigFile();
                createHowToConfigFile();
            }
            if (!CONFIG_FILE.exists()) {
                FlyWithFood.LOGGER.info("配置文件丢失？正在生成配置文件!");
                createConfigFile();
            }
            if (!HOW_TO_CONFIG_FILE.exists()) {
                FlyWithFood.LOGGER.info("教程文件丢失？正在生成教程文件!");
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
            in.close();
            ConfigUtil.CONFIG = new SpongeConfig(ConfigUtil.GSON.fromJson(configText.toString(), JsonObject.class));

            if (!EnergyManager.hasEnergy(ConfigUtil.CONFIG.mode)) {
                FlyWithFood.LOGGER.error("CostMode错误！CostMode只应为 " + Arrays.toString(EnergyManager.getEnergys()) + " 中的一种");
                FlyWithFood.LOGGER.error("Error!! CostMode does not exists! You can use these: " + Arrays.toString(EnergyManager.getEnergys()));
                return false;
            }

            functionWL = ConfigUtil.CONFIG.functionWL.get("Enable").getAsBoolean();
            noCostWL = ConfigUtil.CONFIG.noCostWL.get("Enable").getAsBoolean();  //重载的时候用

            enableRawMsg = ConfigUtil.CONFIG.languages.get("RawMsg").getAsJsonObject().get("Enable").getAsBoolean();
            enableTitle = ConfigUtil.CONFIG.languages.get("TitleMsg").getAsJsonObject().get("Enable").getAsBoolean();
            enableAction = ConfigUtil.CONFIG.languages.get("ActionMsg").getAsJsonObject().get("Enable").getAsBoolean();


            boolean supportTitleMsg = true;
            try {
                Viewer.class.getMethod("sendTitle", Title.class);
                /*
                 * 检查是否支持Title信息的方法(非常粗暴
                 * 反正也开源了，不需要用new String做注释了
                 * 早期用new String是因为没开源，可能有人反编译读我代码看不明白
                 */
            } catch (Throwable ignored) {
                supportTitleMsg = false;
            }
            if (enableTitle && !supportTitleMsg) {
                FlyWithFood.LOGGER.warn("你的服务器不支持发送Title信息!");
                FlyWithFood.LOGGER.warn("请在配置中禁用Title信息!");
                enableTitle = false;
            }

            boolean supportActionMsg = true;
            try {
                Title.builder().actionBar(Text.of("检查是否支持Action信息的方法(非常粗暴"));
            } catch (Throwable ignored) {
                supportActionMsg = false;
            }
            if (enableAction && !supportActionMsg) {
                FlyWithFood.LOGGER.warn("你的服务器不支持发送Action信息!");
                FlyWithFood.LOGGER.warn("请在配置中禁用Action信息!");
                enableAction = false;
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

        PrintWriter out = new PrintWriter(CONFIG_FILE, "UTF-8");
        out.write(ConfigUtil.GSON.toJson(json));
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
            File targetFile = new File(BAK_FOLDER, "config_" + time + ".json");
            CONFIG_FILE.renameTo(targetFile);
            JsonObject newJO = getNewConfig();
            for (String value : ConfigUtil.getJsonObjectKeys(ConfigUtil.CONFIG.languages)) {
                newJO.get("Languages").getAsJsonObject().add(value, ConfigUtil.CONFIG.languages.get(value));
            }
            for (String value : ConfigUtil.getJsonObjectKeys(ConfigUtil.CONFIG.config)) {
                if (value.equals("Languages")) continue;
                if (value.contains("Food")) {
                    newJO.add(value.replace("Food", ""), ConfigUtil.CONFIG.config.get(value));
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
        HOW_TO_CONFIG_FILE.createNewFile();

        PrintWriter out = new PrintWriter(HOW_TO_CONFIG_FILE, "UTF-8");
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
        ConfigUtil.CONFIG = null;
        enableRawMsg = false;
        enableAction = false;
        enableTitle = false;
        FlyWithFood.cancelTasks();
        return loadConfig();
    }

    public static JsonObject getNewConfig() {
        return ConfigUtil.getNewConfig();
    }
}
