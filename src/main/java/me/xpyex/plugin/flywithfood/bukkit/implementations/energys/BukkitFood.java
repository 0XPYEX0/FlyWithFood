package me.xpyex.plugin.flywithfood.bukkit.implementations.energys;

import me.xpyex.plugin.flywithfood.bukkit.FlyWithFood;
import me.xpyex.plugin.flywithfood.common.implementations.FWFUser;
import me.xpyex.plugin.flywithfood.common.implementations.flyenergy.EnergyManager;
import me.xpyex.plugin.flywithfood.common.implementations.flyenergy.energys.FoodEnergy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BukkitFood implements FoodEnergy {
    @Override
    public @NotNull String getName() {
        return "Food";
    }

    @Override
    public void cost(@NotNull FWFUser user, @NotNull Number value) {
        Player target = (Player) user.getPlayer();
        Bukkit.getScheduler().runTask(FlyWithFood.INSTANCE, () ->
                target.setFoodLevel(Math.max(target.getFoodLevel() - value.intValue(), 0))
        );
    }

    @Override
    public @NotNull Integer getNow(FWFUser user) {
        Player target = (Player) user.getPlayer();
        return target.getFoodLevel();
    }

    @Override
    public void register() {
        EnergyManager.registerEnergy(getName(), this);
        //
    }
}
