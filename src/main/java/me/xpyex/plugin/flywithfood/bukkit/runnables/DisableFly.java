package me.xpyex.plugin.flywithfood.bukkit.runnables;

import me.xpyex.plugin.flywithfood.bukkit.FlyWithFood;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class DisableFly extends BukkitRunnable {
    private Player player;

    public DisableFly(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        player.setFlying(false);
        player.setAllowFlight(false);
    }

    public void start() {
        this.runTask(FlyWithFood.INSTANCE);
    }
}
