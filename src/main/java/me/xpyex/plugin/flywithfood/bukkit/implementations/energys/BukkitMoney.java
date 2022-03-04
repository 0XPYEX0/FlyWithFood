package me.xpyex.plugin.flywithfood.bukkit.implementations.energys;

import me.xpyex.plugin.flywithfood.bukkit.FlyWithFood;
import me.xpyex.plugin.flywithfood.common.implementations.FWFUser;
import me.xpyex.plugin.flywithfood.common.implementations.flyenergy.EnergyManager;
import me.xpyex.plugin.flywithfood.common.implementations.flyenergy.energys.MoneyEnergy;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BukkitMoney implements MoneyEnergy {
    @Override
    public @NotNull String getName() {
        return "Money";
    }

    @Override
    public void cost(@NotNull FWFUser user, @NotNull Number value) {
        if (FlyWithFood.econ != null) {
            Player target = (Player) user.getPlayer();
            FlyWithFood.econ.depositPlayer(target, -value.doubleValue());  //才发现这玩意叫存款，，我还以为是设置金额
        }
    }

    @Override
    public @NotNull Double getNow(FWFUser user) {
        Player target = (Player) user.getPlayer();
        return FlyWithFood.econ.getBalance(target);
    }

    @Override
    public void register() {
        EnergyManager.registerEnergy(getName(), this);
        ///
    }
}
