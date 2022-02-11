package me.xpyex.plugin.flywithfood.bukkit.implementations.energys;

import me.xpyex.plugin.flywithfood.bukkit.FlyWithFood;
import me.xpyex.plugin.flywithfood.common.implementations.FWFUser;
import me.xpyex.plugin.flywithfood.common.implementations.flyenergy.EnergyManager;
import me.xpyex.plugin.flywithfood.common.implementations.flyenergy.energys.ExpLevelEnergy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BukkitExpLevel implements ExpLevelEnergy {
    @Override
    public @NotNull String getName() {
        return "ExpLevel";
    }

    @Override
    public void cost(@NotNull FWFUser user, @NotNull Number value) {
        Player target = (Player) user.getPlayer();
        Bukkit.getScheduler().runTask(FlyWithFood.INSTANCE, () ->
                target.setLevel(Math.max(target.getLevel() - value.intValue(), 0))
        );
    }

    @Override
    public @NotNull Integer getNow(FWFUser user) {
        Player target = (Player) user.getPlayer();
        return target.getLevel();
    }

    @Override
    public void register() {
        EnergyManager.registerEnergy(getName(), this);
    }
}
