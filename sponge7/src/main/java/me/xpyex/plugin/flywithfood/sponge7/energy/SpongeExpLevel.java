package me.xpyex.plugin.flywithfood.sponge7.energy;

import me.xpyex.plugin.flywithfood.common.flyenergy.energies.ExpLevelEnergy;
import me.xpyex.plugin.flywithfood.common.implementation.FWFUser;
import org.jetbrains.annotations.NotNull;

public class SpongeExpLevel extends ExpLevelEnergy {
    @Override
    public void cost(@NotNull FWFUser user, @NotNull Number value) {
    
    }
    
    @Override
    public @NotNull Number getNow(@NotNull FWFUser user) {
        return null;
    }
}
