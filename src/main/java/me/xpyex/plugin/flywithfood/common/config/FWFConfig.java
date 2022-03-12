package me.xpyex.plugin.flywithfood.common.config;

import com.google.gson.JsonObject;

public class FWFConfig {
    public final JsonObject config;
    public final JsonObject groups;
    public final int version;
    public final double cost;
    public final double disable;
    public final JsonObject languages;
    public final String language;
    public final String mode;
    public final JsonObject functionWL;
    public final JsonObject noCostWL;
    public final int howLongCheck;
    public final boolean isChinese;
    public final boolean isEnglish;

    public FWFConfig(JsonObject config) {
        this.config = config;
        this.groups = this.config.get("Groups").getAsJsonObject();  //分组
        this.version = this.config.get("ConfigVersion").getAsInt();  //配置文件的版本
        this.cost = this.config.get("Cost").getAsDouble(); //每秒消耗的数值，可为饥饿值或经验值
        this.disable = this.config.get("Disable").getAsDouble(); //消耗至多少关闭飞行
        this.languages = this.config.get("Languages").getAsJsonObject();
        this.language = this.config.get("Language").getAsString();
        this.mode = this.config.get("CostMode").getAsString();
        this.functionWL = this.config.get("FunctionsWhitelist").getAsJsonObject();
        this.noCostWL = this.config.get("NoCostWhitelist").getAsJsonObject();
        this.howLongCheck = this.config.get("CheckSeconds").getAsInt();
        this.isChinese = this.language.equalsIgnoreCase("Chinese");
        this.isEnglish = this.language.equalsIgnoreCase("English");
    }

    public boolean reload() {
        throw new IllegalStateException("非Bukkit或Sponge加载？？请联系开发者处理 QQ1723275529" +
                "\n" +
                "ERROR! Please ask the author for help.");
    }
}
