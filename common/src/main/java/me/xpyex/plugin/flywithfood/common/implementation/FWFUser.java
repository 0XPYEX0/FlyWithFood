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

    public abstract <T> T getPlayer();

    public abstract boolean hasSaturationEff();

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
}
