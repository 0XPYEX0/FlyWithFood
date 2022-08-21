package me.xpyex.plugin.flywithfood.sponge7.energies;

import me.xpyex.plugin.flywithfood.common.flyenergy.energies.FoodEnergy;
import me.xpyex.plugin.flywithfood.common.implementation.FWFUser;
import me.xpyex.plugin.flywithfood.sponge7.FlyWithFoodSponge7;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;

public class SpongeFood extends FoodEnergy {
    @Override
    public void cost(@NotNull FWFUser user, @NotNull Number value) {
        if (value.doubleValue() == 0) {
            return;
        }
        Task.Builder scheduler = Task.builder();
        scheduler.execute(() -> {
            Player target = user.getPlayer();
            target.offer(Keys.FOOD_LEVEL, Math.max((getNow(user) - value.intValue()), 0));
        }).submit(FlyWithFoodSponge7.getInstance());
        
    }
    
    @Override
    public @NotNull Integer getNow(@NotNull FWFUser user) {
        return user.<Player>getPlayer().get(Keys.FOOD_LEVEL).orElse(0);
        //
    }
}
