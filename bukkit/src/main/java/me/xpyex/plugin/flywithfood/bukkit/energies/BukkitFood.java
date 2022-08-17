package me.xpyex.plugin.flywithfood.bukkit.energies;

import me.xpyex.plugin.flywithfood.common.flyenergy.energies.FoodEnergy;
import me.xpyex.plugin.flywithfood.common.implementation.FWFUser;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BukkitFood extends FoodEnergy {
    @Override
    public void cost(FWFUser user, double value) {

    }

    @Override
    public @NotNull Number getNow(FWFUser user) {
        return user.<Player>getPlayer().getFoodLevel();
        //
    }
}
