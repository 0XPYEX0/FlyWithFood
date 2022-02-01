package me.xpyex.plugin.flywithfood.sponge.implementations.energys;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;

public class FoodEnergy implements FlyEnergy {
    @Override
    public @NotNull String getName() {
        return "Food";
    }

    @Override
    public void cost(@NotNull Player target, @NotNull Number value) {
        target.offer(Keys.FOOD_LEVEL, Math.max((getNow(target) - value.intValue()), 0));
    }

    @Override
    public @NotNull Integer getNow(Player target) {
        return target.get(Keys.FOOD_LEVEL).get();
    }

    @Override
    public void register() {
        EnergyManager.registerEnergy(getName(), this);
    }
}
