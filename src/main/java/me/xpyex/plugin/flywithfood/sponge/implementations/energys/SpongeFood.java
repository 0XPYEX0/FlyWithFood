package me.xpyex.plugin.flywithfood.sponge.implementations.energys;

import me.xpyex.plugin.flywithfood.common.implementations.FWFUser;
import me.xpyex.plugin.flywithfood.common.implementations.flyenergy.energys.FoodEnergy;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;

public class SpongeFood extends FoodEnergy {
    @Override
    public void cost(@NotNull FWFUser user, @NotNull Number value) {
        if (value.doubleValue() == 0) {
            return;
        }
        Player target = user.getPlayer();
        target.offer(Keys.FOOD_LEVEL, Math.max((getNow(user) - value.intValue()), 0));
    }

    @Override
    public @NotNull Integer getNow(FWFUser user) {
        Player target = user.getPlayer();
        return target.get(Keys.FOOD_LEVEL).get();
    }
}
