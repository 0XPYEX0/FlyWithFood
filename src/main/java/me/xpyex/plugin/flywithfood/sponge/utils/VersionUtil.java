package me.xpyex.plugin.flywithfood.sponge.utils;

import me.xpyex.plugin.flywithfood.common.config.Config;
import me.xpyex.plugin.flywithfood.sponge.config.HandleConfig;

public class VersionUtil {
    public static int getLocalConfigVersion() {
        return HandleConfig.config.containsKey("ConfigVersion") ? HandleConfig.config.getInteger("ConfigVersion") : 0;
    }
    public static int getPluginConfigVersion() {
        return Config.CONFIG_VERSION;
    }
}
