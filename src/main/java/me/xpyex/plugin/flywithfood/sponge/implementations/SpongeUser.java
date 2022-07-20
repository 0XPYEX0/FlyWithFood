package me.xpyex.plugin.flywithfood.sponge.implementations;

import java.util.Optional;
import me.xpyex.plugin.flywithfood.common.config.ConfigUtil;
import me.xpyex.plugin.flywithfood.common.implementations.FWFInfo;
import me.xpyex.plugin.flywithfood.common.implementations.FWFUser;
import me.xpyex.plugin.flywithfood.sponge.config.HandleConfig;
import me.xpyex.plugin.flywithfood.sponge.tasks.DisableFly;
import me.xpyex.plugin.flywithfood.sponge.tasks.EnableFly;
import me.xpyex.plugin.flywithfood.sponge.tasks.FallProtector;
import me.xpyex.plugin.flywithfood.sponge.utils.Utils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.effect.potion.PotionEffectTypes;
import org.spongepowered.api.entity.living.player.Player;

public class SpongeUser extends SpongeSender implements FWFUser {
    private final Player player;

    public SpongeUser(Player p) {
        super(p);
        this.player = p;
    }

    public SpongeUser(String name) {
        super(Sponge.getServer().getPlayer(name).get());
        Optional<Player> player = Sponge.getServer().getPlayer(name);
        if (!player.isPresent()) {
            throw new NullPointerException("玩家" + name + "不存在");
        }
        this.player = player.get();
    }

    @Override
    public boolean hasSaturationEff() {
        return Utils.hasPotionEffect(player, PotionEffectTypes.SATURATION);
        //
    }

    public void cost(double value) {
        this.getInfo().getEnergy().cost(this, value);
        //
    }

    @Override
    public void disableFly() {
        new DisableFly(player).start();
    }

    @Override
    public void enableFly() {
        new EnableFly(player).start();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getPlayer() {
        return (T) this.player;
        //
    }

    @Override
    public FWFInfo getInfo() {
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
        return new FWFInfo(cost, disable, mode);
    }

    @Override
    public Number getNow() {
        return getInfo().getEnergy().getNow(this);
        //
    }

    @Override
    public boolean inNoCost() {  //玩家所在的世界是否不消耗
        return HandleConfig.noCostWL && ConfigUtil.jsonArrayContains(ConfigUtil.CONFIG.noCostWL.get("Worlds").getAsJsonArray(), player.getWorld().getName());
    }

    @Override
    public boolean inNoFunction() {  //玩家所在的世界是否未启用插件
        return HandleConfig.functionWL && !ConfigUtil.jsonArrayContains(ConfigUtil.CONFIG.functionWL.get("Worlds").getAsJsonArray(), player.getWorld().getName());
    }

    @Override
    public boolean needCheck() {
        if (inNoFunction()) {
            return false;  //如果这个世界并未启用插件，则没有处理的必要
        }
        if (inNoCost()) {
            return false;  //如果这个世界不需要消耗，则没有处理的必要
        }
        if (!player.get(Keys.IS_FLYING).orElse(false)) {  //玩家不在飞行则没有处理他的必要
            return false;
        }
        if ("CREATIVE, SPECTATOR".contains(player.get(Keys.GAME_MODE).get().getName())) {  //1.7没有旁观者模式，创造模式与旁观者模式没有处理的必要
            return false;
        }
        if (hasNoCostPerm()) {  //若玩家拥有权限无视消耗，则没有处理的必要
            return false;
        }
        return true;
    }

    @Override
    public void protectFromFall() {
        new FallProtector(player).start();
        //
    }

    @Override
    public boolean canFly() {
        return player.get(Keys.CAN_FLY).orElse(false);
    }
}
