package me.xpyex.plugin.flywithfood.common.flyenergy;

import me.xpyex.plugin.flywithfood.common.implementation.FWFUser;
import org.jetbrains.annotations.NotNull;

public interface FlyEnergy {
    public @NotNull String getName();

    public default void register() {
        EnergyManager.registerEnergy(getName(), this);
        //
    }

    public void cost(FWFUser user, double value);

    public @NotNull Number getNow(FWFUser user);
}
