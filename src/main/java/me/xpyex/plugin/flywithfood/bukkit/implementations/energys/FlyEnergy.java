package me.xpyex.plugin.flywithfood.bukkit.implementations.energys;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface FlyEnergy {

    @NotNull String getName();

    void cost(@NotNull Player target, @NotNull Number value);

    @NotNull Number getNow(Player target);

    void register();
}
