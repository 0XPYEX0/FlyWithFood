package me.xpyex.plugin.flywithfood.bukkit.utils;

import me.xpyex.plugin.flywithfood.bukkit.config.HandleConfig;
import me.xpyex.plugin.flywithfood.common.config.ConfigUtil;

public class VersionUtil {
    public static int getLocalConfigVersion() {
        return HandleConfig.config.config.containsKey("ConfigVersion") ? HandleConfig.config.version : 0;
    }
    public static int getPluginConfigVersion() {
        return ConfigUtil.CONFIG_VERSION;
    }
}
