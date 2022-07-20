package me.xpyex.plugin.flywithfood.bukkit.implementations.energys;

import me.xpyex.plugin.flywithfood.bukkit.FlyWithFood;
import me.xpyex.plugin.flywithfood.common.implementations.FWFUser;
import me.xpyex.plugin.flywithfood.common.implementations.flyenergy.energys.FoodEnergy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BukkitFood extends FoodEnergy {
    @Override
    public void cost(@NotNull FWFUser user, @NotNull Number value) {
        if (value.doubleValue() == 0) {
            return;
        }
        Player target = user.getPlayer();
        Bukkit.getScheduler().runTask(FlyWithFood.INSTANCE, () ->
                target.setFoodLevel(Math.max(target.getFoodLevel() - value.intValue(), 0))
        );
    }

    @Override
    public @NotNull Integer getNow(FWFUser user) {
        Player target = user.getPlayer();
        return target.getFoodLevel();
    }
}
