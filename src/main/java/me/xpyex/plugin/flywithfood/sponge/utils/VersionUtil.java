package me.xpyex.plugin.flywithfood.sponge.utils;

import me.xpyex.plugin.flywithfood.sponge.config.HandleConfig;
import me.xpyex.plugin.flywithfood.common.version.Versions;

public class VersionUtil {
    public static int getLocalConfigVersion() {
        return HandleConfig.config.containsKey("ConfigVersion") ? HandleConfig.config.getInteger("ConfigVersion") : 0;
    }
    public static int getPluginConfigVersion() {
        return Versions.configVersion;
    }
}
