package me.xpyex.plugin.flywithfood.bukkit.energies;

import me.xpyex.plugin.flywithfood.common.FlyWithFood;
import me.xpyex.plugin.flywithfood.common.flyenergy.energies.FoodEnergy;
import me.xpyex.plugin.flywithfood.common.implementation.FWFUser;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BukkitFood extends FoodEnergy {
    @Override
    public void cost(@NotNull FWFUser user, @NotNull Number value) {
        if (value.intValue() == 0) {
            return;
        }
        Player target = user.getPlayer();
        FlyWithFood.getInstance().getAPI().runTask(() ->
                target.setFoodLevel(Math.max(getNow(user) - value.intValue(), 0))
        );
    }

    @Override
    public @NotNull Integer getNow(@NotNull FWFUser user) {
        return user.<Player>getPlayer().getFoodLevel();
        //
    }
}
