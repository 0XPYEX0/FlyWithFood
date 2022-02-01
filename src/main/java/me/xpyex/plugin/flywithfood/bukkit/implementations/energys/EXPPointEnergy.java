package me.xpyex.plugin.flywithfood.bukkit.implementations.energys;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class EXPPointEnergy implements FlyEnergy {
    @Override
    public @NotNull String getName() {
        return "EXPPoint";
    }

    @Override
    public void cost(@NotNull Player target, @NotNull Number value) {
        target.setTotalExperience(Math.max(target.getTotalExperience() - value.intValue(), 0));
    }

    @Override
    public @NotNull Integer getNow(Player target) {
        return target.getTotalExperience();
    }

    @Override
    public void register() {
        EnergyManager.registerEnergy(getName(), this);
    }
}
