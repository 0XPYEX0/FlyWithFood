package me.xpyex.plugin.flywithfood.bukkit.implementations;

import me.xpyex.plugin.flywithfood.bukkit.config.HandleConfig;
import me.xpyex.plugin.flywithfood.bukkit.runnables.DisableFly;
import me.xpyex.plugin.flywithfood.bukkit.runnables.EnableFly;
import me.xpyex.plugin.flywithfood.bukkit.runnables.FallRunnable;
import me.xpyex.plugin.flywithfood.common.config.ConfigUtil;
import me.xpyex.plugin.flywithfood.common.implementations.FWFInfo;
import me.xpyex.plugin.flywithfood.common.implementations.FWFUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class BukkitUser extends BukkitSender implements FWFUser {
    private final Player player;
    private FWFInfo info;

    public BukkitUser(Player p) {
        super(p);
        this.player = p;
    }

    public BukkitUser(String name) {
        super(Bukkit.getPlayerExact(name));
        Player player = Bukkit.getPlayerExact(name);
        if (player == null) {
            throw new NullPointerException("玩家" + name + "不存在");
        }
        this.player = player;
    }

    @Override
    public boolean canFly() {
        return player.getAllowFlight();
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

    @Override
    public void enableFly() {
        new EnableFly(player).start();
        //
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getPlayer() {
        return (T) this.player;
        //
    }

    @Override
    public FWFInfo getInfo() {
        if (info == null) {
            double cost = ConfigUtil.CONFIG.cost; //每秒消耗的数值，可为饥饿值或经验值
            double disable = ConfigUtil.CONFIG.disable; //消耗至多少关闭飞行
            String mode = ConfigUtil.CONFIG.mode;  //消耗什么数值
            for (String groupName : ConfigUtil.getKeysOfJsonObject(ConfigUtil.CONFIG.groups)) {
                if (player.hasPermission("fly.groups." + groupName)) {
                    cost = ConfigUtil.CONFIG.groups.get(groupName).getAsJsonObject().get("Cost").getAsDouble();
                    disable = ConfigUtil.CONFIG.groups.get(groupName).getAsJsonObject().get("Disable").getAsDouble();
                    mode = ConfigUtil.CONFIG.groups.get(groupName).getAsJsonObject().get("CostMode").getAsString();  //重新分配分组中的值
                    break;
                }
            }
            info = new FWFInfo(cost, disable, mode);
        }
        return info;
    }

    public Number getNow() {
        return getInfo().getEnergy().getNow(this);
        //
    }

    @Override
    public boolean inNoCost() {  //玩家所在的世界是否不消耗
        return HandleConfig.noCostWL && ConfigUtil.jsonArrayContains(ConfigUtil.CONFIG.noCostWL.get("Worlds").getAsJsonArray(), player.getLocation().getWorld().getName());
    }

    @Override
    public boolean inNoFunction() {  //玩家所在的世界是否未启用插件
        return HandleConfig.functionWL && !ConfigUtil.jsonArrayContains(ConfigUtil.CONFIG.functionWL.get("Worlds").getAsJsonArray(), player.getLocation().getWorld().getName());
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
