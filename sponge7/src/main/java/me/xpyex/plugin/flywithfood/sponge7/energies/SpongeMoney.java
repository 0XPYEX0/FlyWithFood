package me.xpyex.plugin.flywithfood.sponge7.energies;

import java.math.BigDecimal;
import me.xpyex.plugin.flywithfood.common.flyenergy.energies.MoneyEnergy;
import me.xpyex.plugin.flywithfood.common.implementation.FWFUser;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;

public class SpongeMoney extends MoneyEnergy {
    private final static Cause CAUSE = Cause.of(EventContext.builder()
                                                    .add(EventContextKeys.PLUGIN,
                                                        Sponge.getPluginManager().getPlugin("flywithfood-sponge").get()
                                                    ).build(),
        Sponge.getPluginManager().getPlugin("flywithfood-sponge")
    );
    
    @SuppressWarnings("all")  //这个Optional内必非空，故忽略该常量所有警告
    private static final EconomyService ECONOMY_SERVICE = Sponge.getServiceManager().provide(EconomyService.class).get();
    
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
}
