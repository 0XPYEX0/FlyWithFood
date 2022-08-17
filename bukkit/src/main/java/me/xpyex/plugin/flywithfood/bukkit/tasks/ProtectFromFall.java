package me.xpyex.plugin.flywithfood.bukkit.tasks;

import me.xpyex.plugin.flywithfood.bukkit.FlyWithFoodBukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ProtectFromFall extends BukkitRunnable {
    private final Player player;

    public ProtectFromFall(Player player) {
        this.player = player;
        //
    }

    @Override
    public void run() {
        if (
                !player.isOnline()
                        ||
                        player.getAllowFlight()
                        ||
                        player.isOnGround()
                        ||
                        (FlyWithFoodBukkit.SERVER_MAIN_VERSION >= 9 && player.isGliding())
        ) {
            cancel();
            return;
        }
        player.setFallDistance(0f);
    }

    public void start() {
        this.runTaskTimer(FlyWithFoodBukkit.INSTANCE, 4L, 4L);
        //
    }
}
