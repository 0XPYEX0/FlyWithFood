package me.xpyex.plugin.flywithfood.sponge7.energies;

import java.math.BigDecimal;
import me.xpyex.plugin.flywithfood.common.flyenergy.energies.MoneyEnergy;
import me.xpyex.plugin.flywithfood.common.implementation.FWFUser;
import me.xpyex.plugin.flywithfood.sponge7.FlyWithFoodSponge7;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;

public class SpongeMoney extends MoneyEnergy {
    private final static Cause CAUSE = Cause.of(EventContext.builder()
                                                    .add(EventContextKeys.PLUGIN,
                                                        FlyWithFoodSponge7.getPlugin()
                                                    ).build(),
        FlyWithFoodSponge7.getPlugin()
    );
    private static EconomyService ECONOMY_SERVICE;
    
    @Override
    public void cost(@NotNull FWFUser user, @NotNull Number value) {
        UniqueAccount account = ECONOMY_SERVICE.getOrCreateAccount(user.<Player>getPlayer().getUniqueId()).get();
        account.deposit(ECONOMY_SERVICE.getDefaultCurrency(), BigDecimal.valueOf(value.doubleValue()), CAUSE);
    }
    
    @Override
    public @NotNull Number getNow(@NotNull FWFUser user) {
        return ECONOMY_SERVICE.getOrCreateAccount(user.<Player>getPlayer().getUniqueId()).get().getBalance(ECONOMY_SERVICE.getDefaultCurrency());
        //
    }

    public static void setEconomyService(EconomyService economyService) {
        ECONOMY_SERVICE = economyService;
        //
    }
}
