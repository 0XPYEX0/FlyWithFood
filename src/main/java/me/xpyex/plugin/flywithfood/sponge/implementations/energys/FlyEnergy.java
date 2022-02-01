package me.xpyex.plugin.flywithfood.sponge.implementations.energys;

import org.spongepowered.api.entity.living.player.Player;
import org.jetbrains.annotations.NotNull;

public interface FlyEnergy {

    @NotNull String getName();

    void cost(@NotNull Player target, @NotNull Number value);

    @NotNull Number getNow(Player target);

    void register();
}
