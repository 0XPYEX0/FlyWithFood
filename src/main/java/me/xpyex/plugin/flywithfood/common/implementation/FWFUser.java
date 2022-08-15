package me.xpyex.plugin.flywithfood.common.implementation;

import me.xpyex.plugin.flywithfood.common.config.FWFConfig;
import me.xpyex.plugin.flywithfood.common.config.FWFInfo;
import me.xpyex.plugin.flywithfood.common.utils.GsonUtil;

public interface FWFUser extends FWFSender {
    FWFInfo[] info = new FWFInfo[1];

    public <T> T getPlayer();

    public boolean hasSaturationEff();

    public default FWFInfo getInfo() {
        if (info[0] == null) {
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
            info[0] = new FWFInfo(cost, disable, mode);
        }
        return info[0];
    }

    public default void cost(double value) {

    }
}
