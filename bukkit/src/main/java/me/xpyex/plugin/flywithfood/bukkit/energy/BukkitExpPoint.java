package me.xpyex.plugin.flywithfood.bukkit.energy;

import me.xpyex.plugin.flywithfood.common.FlyWithFood;
import me.xpyex.plugin.flywithfood.common.flyenergy.energies.ExpPointEnergy;
import me.xpyex.plugin.flywithfood.common.implementation.FWFUser;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BukkitExpPoint extends ExpPointEnergy {
    @Override
    public void cost(@NotNull FWFUser user, @NotNull Number value) {
        FlyWithFood.getInstance().getAPI().runTask(() -> {
            FlyWithFood.getInstance().getAPI().setTotalExperience(user.<Player>getPlayer(), getNow(user).intValue() - value.intValue());
            //
        });
    }

    @Override
    public @NotNull Number getNow(@NotNull FWFUser user) {
        return FlyWithFood.getInstance().getAPI().getTotalExperience(user.<Player>getPlayer());
        //
    }
}
