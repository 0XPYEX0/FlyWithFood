package me.xpyex.plugin.flywithfood.common;

import me.xpyex.plugin.flywithfood.common.api.FlyWithFoodAPI;
import me.xpyex.plugin.flywithfood.common.config.FWFConfig;
import me.xpyex.plugin.flywithfood.common.implementation.FWFLogger;
import me.xpyex.plugin.flywithfood.common.utils.Util;

public class FlyWithFood {
    private static FlyWithFood INSTANCE;
    private static final String PLUGIN_NOT_LOADED_MSG = "插件尚未加载完成 || Plugin is invalid";
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
        getAPI().runTaskAsync(getAPI()::register_bStats);

        FWFConfig.reload();

        FlyWithFood.getInstance().getAPI().startCheck();

        FlyWithFood.getLogger().info("已成功加载");
    }
    
    public static FWFLogger getLogger() {
        return FlyWithFood.getInstance().getAPI().getLogger();
        //
    }
}
