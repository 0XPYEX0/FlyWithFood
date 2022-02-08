package me.xpyex.plugin.flywithfood.bukkit.implementations.energys;

import me.xpyex.plugin.flywithfood.bukkit.FlyWithFood;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FoodEnergy implements FlyEnergy {
    @Override
    public @NotNull String getName() {
        return "Food";
    }

    @Override
    public void cost(@NotNull Player target, @NotNull Number value) {
        Bukkit.getScheduler().runTask(FlyWithFood.INSTANCE, () ->
                target.setFoodLevel(Math.max(target.getFoodLevel() - value.intValue(), 0))
        );
    }

    @Override
    public @NotNull Integer getNow(Player target) {
        return target.getFoodLevel();
    }

    @Override
    public void register() {
        EnergyManager.registerEnergy(getName(), this);
    }
}
