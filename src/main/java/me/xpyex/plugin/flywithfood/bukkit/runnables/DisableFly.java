package me.xpyex.plugin.flywithfood.bukkit.runnables;

import me.xpyex.plugin.flywithfood.bukkit.FlyWithFood;
import me.xpyex.plugin.flywithfood.bukkit.events.FWFDisableFlyEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class DisableFly extends BukkitRunnable {
    private FWFDisableFlyEvent event;
    private Player player;

    public DisableFly(FWFDisableFlyEvent event) {
        this.event = event;
        this.player = event.getPlayer();
    }

    public DisableFly(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        if (event != null) {
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return;
            }
        }
        player.setFlying(false);
        player.setAllowFlight(false);
    }

    public void start() {
        this.runTask(FlyWithFood.INSTANCE);
    }
}
