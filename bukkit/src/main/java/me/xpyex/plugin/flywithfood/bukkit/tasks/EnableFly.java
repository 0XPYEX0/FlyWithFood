package me.xpyex.plugin.flywithfood.bukkit.tasks;

import me.xpyex.plugin.flywithfood.bukkit.FlyWithFoodBukkit;
import me.xpyex.plugin.flywithfood.common.implementation.FWFUser;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * 在主线程内启用玩家飞行，保证安全
 */
public class EnableFly extends BukkitRunnable {
    private final Player player;

    public EnableFly(Player player) {
        this.player = player;
        //
    }

    public EnableFly(FWFUser user) {
        this.player = user.getPlayer();
        //
    }

    @Override
    public void run() {
        player.setAllowFlight(true);
        //
    }

    public void start() {
        this.runTask(FlyWithFoodBukkit.BUKKIT_INSTANCE);
        //
    }
}
