package me.xpyex.plugin.flywithfood.common.config;

import com.alibaba.fastjson.JSONObject;

public class FWFConfig {
    public final JSONObject config;
    public final JSONObject groups;
    public final int version;
    public final double cost;
    public final double disable;
    public final JSONObject languages;
    public final String language;
    public final String mode;
    public final JSONObject functionWL;
    public final JSONObject noCostWL;
    public final int howLongCheck;
    public final boolean isChinese;
    public final boolean isEnglish;

    public FWFConfig(JSONObject config) {
        this.config = config;
        this.groups = this.config.getJSONObject("Groups");  //分组
        this.version = this.config.getInteger("ConfigVersion");  //配置文件的版本
        this.cost = this.config.getDouble("Cost"); //每秒消耗的数值，可为饥饿值或经验值
        this.disable = this.config.getDouble("Disable"); //消耗至多少关闭飞行
        this.languages = this.config.getJSONObject("Languages");
        this.language = this.config.getString("Language");
        this.mode = this.config.getString("CostMode");
        this.functionWL = this.config.getJSONObject("FunctionsWhitelist");
        this.noCostWL = this.config.getJSONObject("NoCostWhitelist");
        this.howLongCheck = this.config.getInteger("CheckSeconds");
        this.isChinese = this.language.equalsIgnoreCase("Chinese");
        this.isEnglish = this.language.equalsIgnoreCase("English");
    }
}
