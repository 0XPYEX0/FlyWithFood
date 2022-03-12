package me.xpyex.plugin.flywithfood.bukkit.config;

import com.google.gson.JsonObject;
import me.xpyex.plugin.flywithfood.common.config.FWFConfig;

public class BukkitConfig extends FWFConfig {
    public BukkitConfig(JsonObject config) {
        super(config);
    }

    @Override
    public boolean reload() {
        return HandleConfig.reloadConfig();
    }
}
