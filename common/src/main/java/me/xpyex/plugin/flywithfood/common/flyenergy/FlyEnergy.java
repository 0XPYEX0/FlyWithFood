package me.xpyex.plugin.flywithfood.common.flyenergy;

import me.xpyex.plugin.flywithfood.common.implementation.FWFUser;
import org.jetbrains.annotations.NotNull;

public abstract class FlyEnergy {
    public abstract @NotNull String getName();

    public final void register() {
        EnergyManager.registerEnergy(getName(), this);
        //
    }

    public abstract void cost(@NotNull FWFUser user, @NotNull Number value);

    public abstract @NotNull Number getNow(@NotNull FWFUser user);
}
