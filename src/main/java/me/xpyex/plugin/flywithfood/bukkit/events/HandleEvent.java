package me.xpyex.plugin.flywithfood.bukkit.events;

import me.xpyex.plugin.flywithfood.bukkit.FlyWithFood;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class HandleEvent implements Listener {
    @EventHandler
    public void onPlayerDmg(EntityDamageEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) {
            return;
        }
        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) {
            return;
        }
        if (FlyWithFood.antiFallDamage.contains((Player) event.getEntity())) {
            event.setCancelled(true);
            FlyWithFood.antiFallDamage.remove((Player) event.getEntity());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        FlyWithFood.antiFallDamage.remove(event.getPlayer());
    }
}
