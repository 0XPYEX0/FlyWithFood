package me.xpyex.plugin.flywithfood.common.implementations.flyenergy;

import me.xpyex.plugin.flywithfood.common.implementations.FWFUser;
import org.jetbrains.annotations.NotNull;

public interface FlyEnergy {

    @NotNull String getName();

    void cost(@NotNull FWFUser target, @NotNull Number value);

    @NotNull Number getNow(FWFUser target);

    void register();
}
