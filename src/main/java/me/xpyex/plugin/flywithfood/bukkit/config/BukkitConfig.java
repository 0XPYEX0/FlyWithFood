package me.xpyex.plugin.flywithfood.bukkit.config;

import com.alibaba.fastjson.JSONObject;
import me.xpyex.plugin.flywithfood.common.config.FWFConfig;

public class BukkitConfig extends FWFConfig {
    public BukkitConfig(JSONObject config) {
        super(config);
    }

    @Override
    public boolean reload() {
        return HandleConfig.reloadConfig();
    }
}
