package me.xpyex.plugin.flywithfood.sponge.implementations.energys;

import java.math.BigDecimal;
import me.xpyex.plugin.flywithfood.sponge.FlyWithFood;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.cause.EventContextKeys;

public class MoneyEnergy implements FlyEnergy {
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
    public void cost(@NotNull Player target, @NotNull Number value) {
        FlyWithFood.ECONOMY_SERVICE.getOrCreateAccount(target.getUniqueId()).get().setBalance(FlyWithFood.ECONOMY_SERVICE.getDefaultCurrency(), new BigDecimal(getNow(target).doubleValue() - value.doubleValue()), cause);
    }

    @Override
    public @NotNull Number getNow(Player target) {
        return FlyWithFood.ECONOMY_SERVICE.getOrCreateAccount(target.getUniqueId()).get().getBalance(FlyWithFood.ECONOMY_SERVICE.getDefaultCurrency());
    }

    @Override
    public void register() {
        EnergyManager.registerEnergy(getName(), this);
    }
}
