package me.xpyex.plugin.flywithfood.sponge.config;

import com.google.gson.JsonObject;
import me.xpyex.plugin.flywithfood.common.config.FWFConfig;

public class SpongeConfig extends FWFConfig {
    public SpongeConfig(JsonObject config) {
        super(config);
    }

    @Override
    public boolean reload() {
        return HandleConfig.reloadConfig();
        //
    }
}
