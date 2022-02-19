package me.xpyex.plugin.flywithfood.bukkit.implementations;

import me.xpyex.plugin.flywithfood.bukkit.config.HandleConfig;
import me.xpyex.plugin.flywithfood.bukkit.events.FWFDisableFlyEvent;
import me.xpyex.plugin.flywithfood.bukkit.runnables.DisableFly;
import me.xpyex.plugin.flywithfood.bukkit.runnables.FallRunnable;
import me.xpyex.plugin.flywithfood.bukkit.utils.Utils;
import me.xpyex.plugin.flywithfood.common.implementations.FWFInfo;
import me.xpyex.plugin.flywithfood.common.implementations.FWFUser;
import me.xpyex.plugin.flywithfood.common.types.FWFMsgType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class BukkitUser implements FWFUser {
    private final Player player;

    public BukkitUser(Player p) {
        this.player = p;
    }

    @Override
    public void autoSendMsg(String... msg) {
        Utils.autoSendMsg(player, msg);
        //
    }

    @Override
    public boolean hasPermission(String perm) {  //新版在common包下处理命令，该方法用于判断权限.common包下无法使用BK/Sponge方法
        return player.hasPermission(perm);
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean isPlayer() {
        return false;
    }

    @Override
    public void sendFWFMsg(FWFMsgType msg) {
        Utils.sendFWFMsg(player, msg);
        //
    }

    @Override
    public boolean hasSaturationEff() {
        return player.hasPotionEffect(PotionEffectType.SATURATION);
        //
    }

    @Override
    public void cost(double value) {
        this.getInfo().getEnergy().cost(this, value);
        //
    }

    @Override
    public void disableFly() {
        new DisableFly(player).start();
        //
    }

    public void disableFly(FWFDisableFlyEvent reason) {
        new DisableFly(reason).start();
        //
    }

    @Override
    public Player getPlayer() {
        return this.player;
        //
    }

    @Override
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

    @Override
    public boolean hasNoCostPerm() {
        return (player.hasPermission("fly.nohunger") || player.hasPermission("fly.nocost"));
    }

    public Number getNow() {
        return getInfo().getEnergy().getNow(this);
    }

    @Override
    public boolean inNoCost() {  //玩家所在的世界是否不消耗
        return HandleConfig.noCostWL && HandleConfig.config.noCostWL.getJSONArray("Worlds").contains(player.getLocation().getWorld().getName());
    }

    @Override
    public boolean inNoFunction() {  //玩家所在的世界是否未启用插件
        return HandleConfig.functionWL && !HandleConfig.config.functionWL.getJSONArray("Worlds").contains(player.getLocation().getWorld().getName());
    }

    @Override
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
        if (hasNoCostPerm()) {  //若玩家拥有权限无视消耗，则没有处理的必要
            return false;
        }
        return true;
    }

    @Override
    public void protectFromFall() {
        new FallRunnable(player).start();
        //
    }
}
