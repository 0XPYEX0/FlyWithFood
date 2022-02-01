package me.xpyex.plugin.flywithfood.bukkit.events;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import me.xpyex.plugin.flywithfood.bukkit.config.HandleConfig;
import org.bukkit.event.HandlerList;

public class FWFReloadConfigEvent extends FWFEvent {
    HandlerList handlerList = new HandlerList();
    JSONObject oldJSON;

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public FWFReloadConfigEvent(JSONObject oldJSON) {
        this.oldJSON = oldJSON;
    }

    public JSONObject getOldJSON() {
        return this.oldJSON;
    }

    public String getOldJSONText() {
        return JSON.toJSONString(this.oldJSON);
    }

    public String getOldJSONTextPretty() {
        return JSON.toJSONString(this.oldJSON, true);
    }

    public JSONObject getNewJSON() {
        return HandleConfig.config.config;
    }

    public String getNewJSONText() {
        return JSON.toJSONString(HandleConfig.config.config);
    }

    public String getNewJSONTextPretty() {
        return JSON.toJSONString(HandleConfig.config, true);
    }
}
