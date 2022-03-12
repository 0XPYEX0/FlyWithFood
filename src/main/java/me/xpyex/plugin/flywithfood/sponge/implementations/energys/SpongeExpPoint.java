package me.xpyex.plugin.flywithfood.sponge.implementations.energys;

import me.xpyex.plugin.flywithfood.common.implementations.FWFUser;
import me.xpyex.plugin.flywithfood.common.implementations.flyenergy.EnergyManager;
import me.xpyex.plugin.flywithfood.common.implementations.flyenergy.energys.ExpPointEnergy;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;

public class SpongeExpPoint implements ExpPointEnergy {
    @Override
    public @NotNull String getName() {
        return "ExpPoint";
    }

    @Override
    public void cost(@NotNull FWFUser user, @NotNull Number value) {
        Player target = (Player) user.getPlayer();
        target.offer(Keys.EXPERIENCE_SINCE_LEVEL, Math.max(getNow(user) - value.intValue(), 0));
    }

    @Override
    public @NotNull Integer getNow(FWFUser user) {
        Player target = (Player) user.getPlayer();
        return target.get(Keys.EXPERIENCE_LEVEL).get();
    }

    @Override
    public void register() {
        EnergyManager.registerEnergy(getName(), this);
        //
    }
}
