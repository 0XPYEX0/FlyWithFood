package me.xpyex.plugin.flywithfood.bukkit.implementations.energys;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class EXPLevelEnergy implements FlyEnergy {
    @Override
    public @NotNull String getName() {
        return "EXPLevel";
    }

    @Override
    public void cost(@NotNull Player target, @NotNull Number value) {
        target.setLevel(Math.max(target.getLevel() - value.intValue(), 0));
    }

    @Override
    public @NotNull Integer getNow(Player target) {
        return target.getLevel();
    }

    @Override
    public void register() {
        EnergyManager.registerEnergy(getName(), this);
    }
}
