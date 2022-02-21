package me.xpyex.plugin.flywithfood.sponge.config;

import com.alibaba.fastjson.JSONObject;
import me.xpyex.plugin.flywithfood.common.config.FWFConfig;

public class SpongeConfig extends FWFConfig {
    public SpongeConfig(JSONObject config) {
        super(config);
    }

    @Override
    public boolean reload() {
        return HandleConfig.reloadConfig();
    }
}
