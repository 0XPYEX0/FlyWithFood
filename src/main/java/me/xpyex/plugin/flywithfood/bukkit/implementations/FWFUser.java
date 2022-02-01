package me.xpyex.plugin.flywithfood.bukkit.implementations;

import me.xpyex.plugin.flywithfood.bukkit.config.HandleConfig;
import me.xpyex.plugin.flywithfood.bukkit.events.FWFDisableFlyEvent;
import me.xpyex.plugin.flywithfood.bukkit.runnables.DisableFly;
import me.xpyex.plugin.flywithfood.bukkit.runnables.FallRunnable;
import me.xpyex.plugin.flywithfood.bukkit.utils.Utils;
import me.xpyex.plugin.flywithfood.common.types.FWFMsgType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class FWFUser {
    private final Player player;

    public FWFUser(Player p) {
        this.player = p;
    }

    public void sendFWFMsg(FWFMsgType msg) {
        Utils.sendFWFMsg(player, msg);
    }

    public boolean hasSaturationEff() {
        return player.hasPotionEffect(PotionEffectType.SATURATION);
    }

    public void cost(double value) {
        this.getInfo().getEnergy().cost(player, value);
    }

    public void disableFly() {
        new DisableFly(player).start();
    }

    public void disableFly(FWFDisableFlyEvent reason) {
        new DisableFly(reason).start();
    }

    public Player getPlayer() {
        return this.player;
    }

    public FWFInfo getInfo() {
        double cost = HandleConfig.config.cost; //每秒消耗的数值，可为饥饿值或经验值
        double disable = HandleConfig.config.disable; //消耗至多少关闭飞行
        String mode = HandleConfig.config.mode;  //消耗什么数值
        for (String groupName : HandleConfig.config.groups.keySet()) {
            if (player.hasPermission("fly.groups." + groupName)) {
                cost = HandleConfig.config.groups.getJSONObject(groupName).getDouble("Cost");
                disable = HandleConfig.config.groups.getJSONObject(groupName).getDouble("Disable");
                mode = HandleConfig.config.groups.getJSONObject(groupName).getString("CostMode");  //重新分配分组中的值
                break;
            }
        }
        return new FWFInfo(cost, disable, mode);
    }

    public boolean nocost() {
        return (player.hasPermission("fly.nohunger") || player.hasPermission("fly.nocost"));
    }

    public Number getNow() {
        return getInfo().getEnergy().getNow(player);
    }

    public boolean inNoCost() {  //玩家所在的世界是否不消耗
        boolean NoCostWLEnable = HandleConfig.config.noCostWL.getBoolean("Enable");
        return NoCostWLEnable && HandleConfig.config.noCostWL.getJSONArray("Worlds").contains(player.getLocation().getWorld().getName());
    }

    public boolean inNoFunction() {  //玩家所在的世界是否未启用插件
        boolean FunctionWLEnable = HandleConfig.config.functionWL.getBoolean("Enable");
        return FunctionWLEnable && !HandleConfig.config.functionWL.getJSONArray("Worlds").contains(player.getLocation().getWorld().getName());
    }

    public boolean needCheck() {
        if (inNoFunction()) {
            return false;  //如果这个世界并未启用插件，则没有处理的必要
        }
        if (inNoCost()) {
            return false;  //如果这个世界不需要消耗，则没有处理的必要
        }
        if (!player.isFlying()) {  //玩家不在飞行则没有处理他的必要
            return false;
        }
        if ("CREATIVE, SPECTATOR".contains(player.getGameMode().toString())) {  //1.7没有旁观者模式，创造模式与旁观者模式没有处理的必要
            return false;
        }
        if (nocost()) {  //若玩家拥有权限无视消耗，则没有处理的必要
            return false;
        }
        return true;
    }

    public void protectFromFall() {
        new FallRunnable(player).start();
    }
}
