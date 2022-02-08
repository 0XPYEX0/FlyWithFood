package me.xpyex.plugin.flywithfood.bukkit.implementations.energys;

import me.xpyex.plugin.flywithfood.bukkit.FlyWithFood;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class EXPLevelEnergy implements FlyEnergy {
    @Override
    public @NotNull String getName() {
        return "ExpLevel";
    }

    @Override
    public void cost(@NotNull Player target, @NotNull Number value) {
        Bukkit.getScheduler().runTask(FlyWithFood.INSTANCE, () ->
                target.setLevel(Math.max(target.getLevel() - value.intValue(), 0))
        );
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
