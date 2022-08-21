package me.xpyex.plugin.flywithfood.common.implementation;

import me.xpyex.plugin.flywithfood.common.FlyWithFood;
import me.xpyex.plugin.flywithfood.common.config.FWFConfig;
import me.xpyex.plugin.flywithfood.common.config.FWFInfo;
import me.xpyex.plugin.flywithfood.common.utils.GsonUtil;

public interface FWFUser extends FWFSender {

    public static FWFUser of(String name) {
        return FlyWithFood.getInstance().getAPI().getUser(name);
        //
    }

    public <T> T getPlayer();

    public boolean hasSaturationEff();

    public default FWFInfo getInfo() {
        double cost = FWFConfig.CONFIG.cost; //每秒消耗的数值，可为饥饿值或经验值
        double disable = FWFConfig.CONFIG.disable; //消耗至多少关闭飞行
        String mode = FWFConfig.CONFIG.mode;  //消耗什么数值
        for (String groupName : GsonUtil.getKeysOfJsonObject(FWFConfig.CONFIG.groups)) {
            if (hasPermission("fly.groups." + groupName)) {
                cost = FWFConfig.CONFIG.groups.get(groupName).getAsJsonObject().get("Cost").getAsDouble();
                disable = FWFConfig.CONFIG.groups.get(groupName).getAsJsonObject().get("Disable").getAsDouble();
                mode = FWFConfig.CONFIG.groups.get(groupName).getAsJsonObject().get("CostMode").getAsString();  //重新分配分组中的值
                break;
            }
        }
        return new FWFInfo(cost, disable, mode);
    }

    public default void cost(double value) {
        getInfo().getEnergy().cost(this, value);
        //
    }

    public default Number getNow() {
        return getInfo().getEnergy().getNow(this);
        //
    }

    public void sendTitle(String title, String subTitle);

    public void sendActionBar(String actionBar);

    public void enableFly();

    public void disableFly();

    public void protectFromFall();

    public boolean canFly();

    public String getWorldName();

    public default boolean inNoFunction() {
        return FWFConfig.CONFIG.functionWL.get("Enabled").getAsBoolean() && !GsonUtil.jsonArrayContains(FWFConfig.CONFIG.functionWL.get("Worlds").getAsJsonArray(), getWorldName());
        //
    }
    
    public default boolean inNoCost() {
        return FWFConfig.CONFIG.noCostWL.get("Enabled").getAsBoolean() && GsonUtil.jsonArrayContains(FWFConfig.CONFIG.functionWL.get("Worlds").getAsJsonArray(), getWorldName());
    }
    
    public String getGameModeName();
    
    public boolean isFlying();
    
    public default boolean needCheck() {
        if (this.hasNoCostPerm()) return false;  //如果玩家有权限无视扣除，不处理
        
        if (this.inNoFunction()) return false;  //如果玩家所在的世界不启用插件功能，不处理
        
        if (this.inNoCost()) return false;  //如果玩家所在的世界不扣除点数，不处理
        
        if (!this.isFlying()) return false;  //如果玩家不在飞行，不处理
        
        if ("CREATIVE, SPECTATOR".contains(this.getGameModeName())) return false;  //如果玩家是创造/旁观者模式，不处理
        
        return true;
    }
}
