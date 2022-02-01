package me.xpyex.plugin.flywithfood.bukkit.implementations.energys;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FoodEnergy implements FlyEnergy{
    @Override
    public @NotNull String getName() {
        return "Food";
    }

    @Override
    public void cost(@NotNull Player target, @NotNull Number value) {
        target.setFoodLevel(Math.max((target.getFoodLevel() - value.intValue()), 0));
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
