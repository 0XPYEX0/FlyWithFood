package me.xpyex.plugin.flywithfood.bukkit.energy;

import me.xpyex.plugin.flywithfood.bukkit.event.EnergyCostEvent;
import me.xpyex.plugin.flywithfood.common.FlyWithFood;
import me.xpyex.plugin.flywithfood.common.flyenergy.energies.DurabilityEnergy;
import me.xpyex.plugin.flywithfood.common.implementation.FWFUser;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BukkitDurability extends DurabilityEnergy {
    @Override
    public void cost(@NotNull FWFUser user, @NotNull Number value) {
        if (value.intValue() == 0) {
            return;
        }
        EnergyCostEvent event = new EnergyCostEvent(user, value);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }
        Player target = user.getPlayer();
        if (target.getInventory().getChestplate().getType() != Material.ELYTRA) {
            return;
        }
        FlyWithFood.getInstance().getAPI().runTask(() ->
            target.getInventory().getChestplate().setDurability((short) (getNow(user).shortValue() - value.shortValue()))
        );
    }

    @Override
    public @NotNull Number getNow(@NotNull FWFUser user) {
        if (user.<Player>getPlayer().getInventory().getChestplate().getType() != Material.ELYTRA) {
            return -1;
        }
        return user.<Player>getPlayer().getInventory().getChestplate().getDurability();
    }
}
