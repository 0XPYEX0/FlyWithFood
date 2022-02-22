package me.xpyex.plugin.flywithfood.bukkit.runnables;

import me.xpyex.plugin.flywithfood.bukkit.FlyWithFood;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class EnableFly extends BukkitRunnable {
    private final Player player;

    public EnableFly(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        player.setAllowFlight(true);
    }

    public void start() {
        this.runTask(FlyWithFood.INSTANCE);
    }
}
