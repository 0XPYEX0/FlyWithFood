package me.xpyex.plugin.flywithfood.bukkit.runnables;

import me.xpyex.plugin.flywithfood.bukkit.FlyWithFood;
import me.xpyex.plugin.flywithfood.bukkit.config.HandleConfig;
import me.xpyex.plugin.flywithfood.common.config.ConfigUtil;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class FallRunnable extends BukkitRunnable {
    private final Player player;

    public FallRunnable(Player player) {
        this.player = player;
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
                        (!(ConfigUtil.isOldGson || HandleConfig.isOldVer) && player.isGliding())
        ) {
            cancel();
            return;
        }
        player.setFallDistance(0f);
    }

    public void start() {
        this.runTaskTimer(FlyWithFood.INSTANCE, 4L, 4L);
        //
    }
}
