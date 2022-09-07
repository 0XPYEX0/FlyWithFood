package me.xpyex.plugin.flywithfood.common;

import me.xpyex.plugin.flywithfood.common.api.FlyWithFoodAPI;
import me.xpyex.plugin.flywithfood.common.config.FWFConfig;
import me.xpyex.plugin.flywithfood.common.implementation.FWFLogger;
import me.xpyex.plugin.flywithfood.common.utils.Util;

public class FlyWithFood {
    private static FlyWithFood INSTANCE;
    public static final String PLUGIN_NOT_LOADED_MSG = "插件尚未加载完成 || Plugin is invalid";
    private final FlyWithFoodAPI API;

    public FlyWithFood(FlyWithFoodAPI API) {
        INSTANCE = this;
        this.API = API;
    }

    public FlyWithFoodAPI getAPI() {
        if (Util.checkNull(API)) throw new IllegalStateException(PLUGIN_NOT_LOADED_MSG);

        return API;
    }

    public static FlyWithFood getInstance() {
        if (Util.checkNull(INSTANCE)) throw new IllegalStateException(PLUGIN_NOT_LOADED_MSG);

        return INSTANCE;
    }

    public void enable() {
        if (Util.checkNull(INSTANCE, API)) throw new IllegalStateException(PLUGIN_NOT_LOADED_MSG);

        getLogger().info(" ");

        if (getAPI().getServerSoftware().startsWith("Unknown-")) {
            getLogger().error("无法自动解析服务端版本，使用默认版本信息",
                "Failed to get Server Version... Using default version info");
        }
        getLogger().info("你使用的服务端核心: " + getInstance().getAPI().getServerSoftware());
        getLogger().info("You are using the server software: " + getInstance().getAPI().getServerSoftware());
        getLogger().info(" ");

        getLogger().info("感谢使用FlyWithFood.");
        getLogger().info("本项目在GitHub开源: https://github.com/0XPYEX0/FlyWithFood");
        getLogger().info("本项目在Gitee开源: https://gitee.com/xpyex/FlyWithFood");
        getLogger().info(" ");
        getLogger().info("Thank you for using FlyWithFood");
        getLogger().info("The plugin has been open source in GitHub: https://github.com/0XPYEX0/FlyWithFood");
        getLogger().info("Have a nice trip with this plugin  :)");
        getLogger().info(" ");

        getAPI().registerEnergies();

        if (!FWFConfig.reload()) {
            getLogger().severe("载入配置文件出错!插件加载已终止,请检查配置文件，如无法解决请查看后台报错并报告开发者. QQ:1723275529");
            getLogger().severe("若确认是由配置文件错误导致加载出错，可在修改完毕后使用 /fly reload 重载以恢复");
            getLogger().severe(" ");
            getLogger().severe("ERROR!! The plugin loading has been terminated. Please check your config file.");
            getLogger().severe("If you can not solve this problem, please check error messages in console and open a Issue to my GitHub.");
            getLogger().severe("If you are sure that the config file has something wrong, you can use '/fly reload' after you fix that problem.");
            getLogger().severe(" ");
            return;
        }
        getLogger().info("成功加载配置文件");

        getLogger().info("正在尝试访问bStats");
        getAPI().runTaskAsync(getAPI()::register_bStats);

        getLogger().info("正在检查更新");
        getAPI().runTaskAsync(getAPI()::checkUpdate);

        getLogger().info("已成功加载");
    }
    
    public static FWFLogger getLogger() {
        return FlyWithFood.getInstance().getAPI().getLogger();
        //
    }

    public static void disable() {
        getInstance().getAPI().stopTasks();
        getLogger().info("已取消所有任务");
        getLogger().info("已卸载");

        INSTANCE = null;
    }
}
