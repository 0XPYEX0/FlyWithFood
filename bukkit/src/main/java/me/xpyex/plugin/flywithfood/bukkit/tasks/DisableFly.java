package me.xpyex.plugin.flywithfood.bukkit.tasks;

import me.xpyex.plugin.flywithfood.bukkit.FlyWithFoodBukkit;
import me.xpyex.plugin.flywithfood.common.implementation.FWFUser;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * 在主线程内禁用玩家飞行，保证安全
 */
public class DisableFly extends BukkitRunnable {
    private final Player player;

    public DisableFly(Player player) {
        this.player = player;
        //
    }

    public DisableFly(FWFUser user) {
        this.player = user.getPlayer();
        //
    }

    @Override
    public void run() {
        player.setFlying(false);
        player.setAllowFlight(false);
    }

    public void start() {
        this.runTask(FlyWithFoodBukkit.getInstance());
        //
    }
}
