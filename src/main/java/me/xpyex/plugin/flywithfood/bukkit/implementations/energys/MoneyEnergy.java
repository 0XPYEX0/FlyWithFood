package me.xpyex.plugin.flywithfood.bukkit.implementations.energys;

import me.xpyex.plugin.flywithfood.bukkit.FlyWithFood;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MoneyEnergy implements FlyEnergy {
    @Override
    public @NotNull String getName() {
        return "Money";
    }

    @Override
    public void cost(@NotNull Player target, @NotNull Number value) {
        if (FlyWithFood.econ != null) {
            FlyWithFood.econ.depositPlayer(target, FlyWithFood.econ.getBalance(target) - value.doubleValue());
        }
    }

    @Override
    public @NotNull Number getNow(Player target) {
        return FlyWithFood.econ.getBalance(target);
    }

    @Override
    public void register() {
        EnergyManager.registerEnergy(getName(), this);
    }
}
