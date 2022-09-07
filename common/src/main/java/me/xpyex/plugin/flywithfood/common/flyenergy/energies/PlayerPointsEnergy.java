package me.xpyex.plugin.flywithfood.common.flyenergy.energies;

import me.xpyex.plugin.flywithfood.common.flyenergy.FlyEnergy;
import org.jetbrains.annotations.NotNull;

public abstract class PlayerPointsEnergy extends FlyEnergy {
    @Override
    public @NotNull String getName() {
        return "PlayerPoints";
        //
    }
}
