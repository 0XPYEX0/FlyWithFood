package me.xpyex.plugin.flywithfood.bukkit.tasks;

import me.xpyex.plugin.flywithfood.bukkit.FlyWithFoodBukkit;
import me.xpyex.plugin.flywithfood.common.FlyWithFood;
import me.xpyex.plugin.flywithfood.common.implementation.FWFUser;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ProtectFromFall extends BukkitRunnable {
    private final Player player;

    public ProtectFromFall(Player player) {
        this.player = player;
        //
    }

    public ProtectFromFall(FWFUser user) {
        this(user.<Player>getPlayer());
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
                        (FlyWithFood.getInstance().getAPI().getServerMainVer() >= 9 && player.isGliding())
        ) {
            cancel();
            return;
        }
        player.setFallDistance(0f);
    }

    public void start() {
        this.runTaskTimer(FlyWithFoodBukkit.getInstance(), 4L, 4L);
        //
    }
}
