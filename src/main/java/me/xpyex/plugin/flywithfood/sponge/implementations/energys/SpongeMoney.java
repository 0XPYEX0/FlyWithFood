package me.xpyex.plugin.flywithfood.sponge.implementations.energys;

import java.math.BigDecimal;
import me.xpyex.plugin.flywithfood.common.implementations.FWFUser;
import me.xpyex.plugin.flywithfood.common.implementations.flyenergy.EnergyManager;
import me.xpyex.plugin.flywithfood.common.implementations.flyenergy.energys.MoneyEnergy;
import me.xpyex.plugin.flywithfood.sponge.FlyWithFood;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.cause.EventContextKeys;

public class SpongeMoney implements MoneyEnergy {
    private final static Cause cause = Cause.of(EventContext.builder()
            .add(EventContextKeys.PLUGIN,
                    Sponge.getPluginManager().getPlugin("flywithfood-sponge").get()
            ).build(),
            Sponge.getPluginManager().getPlugin("flywithfood-sponge")
    );

    @Override
    public @NotNull String getName() {
        return "Money";
    }

    @Override
    public void cost(@NotNull FWFUser user, @NotNull Number value) {
        Player target = (Player) user.getPlayer();
        FlyWithFood.ECONOMY_SERVICE.getOrCreateAccount(target.getUniqueId()).get().setBalance(FlyWithFood.ECONOMY_SERVICE.getDefaultCurrency(), new BigDecimal(getNow(user).doubleValue() - value.doubleValue()), cause);
    }

    @Override
    public @NotNull Number getNow(FWFUser user) {
        Player target = (Player) user.getPlayer();
        return FlyWithFood.ECONOMY_SERVICE.getOrCreateAccount(target.getUniqueId()).get().getBalance(FlyWithFood.ECONOMY_SERVICE.getDefaultCurrency());
    }

    @Override
    public void register() {
        EnergyManager.registerEnergy(getName(), this);
    }
}
