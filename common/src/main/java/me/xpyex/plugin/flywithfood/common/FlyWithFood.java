package me.xpyex.plugin.flywithfood.common;

import me.xpyex.plugin.flywithfood.common.implementation.FWFLogger;

public class FlyWithFood {
    public static FWFLogger LOGGER;
    public static String SERVER_TYPE;

    public void onEnable(FWFLogger logger) {
        LOGGER = logger;
    }
}
