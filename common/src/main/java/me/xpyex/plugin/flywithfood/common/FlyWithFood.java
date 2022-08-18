package me.xpyex.plugin.flywithfood.common;

import me.xpyex.plugin.flywithfood.common.api.FlyWithFoodAPI;
import me.xpyex.plugin.flywithfood.common.implementation.FWFLogger;
import me.xpyex.plugin.flywithfood.common.utils.Util;

public class FlyWithFood {
    public static FWFLogger LOGGER;
    private final FlyWithFoodAPI api;
    public static FlyWithFood INSTANCE;

    public FlyWithFood(FlyWithFoodAPI api) {
        INSTANCE = this;
        this.api = api;
        LOGGER = api.getLogger();
    }

    public FlyWithFoodAPI getAPI() {
        if (Util.checkNull(api)) throw new IllegalStateException("插件尚未加载完成");

        return api;
    }

    public static FlyWithFood getInstance() {
        if (Util.checkNull(INSTANCE)) throw new IllegalStateException("插件尚未加载完成");

        return INSTANCE;
    }

    public static void enable() {
        LOGGER.info(" ");

        LOGGER.info("你使用的服务端核心: " + getInstance().getAPI().getServerSoftware());
        LOGGER.info("You are using the server software: " + getInstance().getAPI().getServerSoftware());
        LOGGER.info(" ");

        LOGGER.info("感谢使用FlyWithFood.");
        LOGGER.info("本项目在GitHub开源: https://github.com/0XPYEX0/FlyWithFood");
        LOGGER.info("本项目在Gitee开源: https://gitee.com/xpyex/FlyWithFood");
        LOGGER.info(" ");
        LOGGER.info("Thank you for using FlyWithFood");
        LOGGER.info("The plugin has been open source in GitHub: https://github.com/0XPYEX0/FlyWithFood");
        LOGGER.info("Have a nice trip with this plugin  :)");
        LOGGER.info(" ");
    }
}
