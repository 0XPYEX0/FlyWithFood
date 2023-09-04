package me.xpyex.plugin.flywithfood.bukkit.energy;

import me.xpyex.plugin.flywithfood.bukkit.event.EnergyCostEvent;
import me.xpyex.plugin.flywithfood.common.FlyWithFood;
import me.xpyex.plugin.flywithfood.common.flyenergy.energies.ExpLevelEnergy;
import me.xpyex.plugin.flywithfood.common.implementation.FWFUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.jetbrains.annotations.NotNull;

public class BukkitExpLevel extends ExpLevelEnergy {
    @Override
    public void cost(@NotNull FWFUser user, @NotNull Number value) {
        if (value.intValue() == 0) {
            return;
        }
        {
            EnergyCostEvent event = new EnergyCostEvent(user, value);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return;
            }
        }
        Player target = user.getPlayer();
        FlyWithFood.getInstance().getAPI().runTask(() -> {
            PlayerLevelChangeEvent event = new PlayerLevelChangeEvent(target, target.getLevel(), Math.max(target.getLevel() - value.intValue(), 0));
            Bukkit.getPluginManager().callEvent(event);  //有变化call一下
            target.setLevel(event.getNewLevel());
        });
    }

    @Override
    public @NotNull Number getNow(@NotNull FWFUser user) {
        return user.<Player>getPlayer().getLevel();
        //
    }
}
