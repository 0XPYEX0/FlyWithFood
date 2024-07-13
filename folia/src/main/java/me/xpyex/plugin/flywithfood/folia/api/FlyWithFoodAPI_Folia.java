package me.xpyex.plugin.flywithfood.folia.api;

import java.util.concurrent.TimeUnit;
import me.xpyex.plugin.flywithfood.bukkit.FlyWithFoodBukkit;
import me.xpyex.plugin.flywithfood.bukkit.api.FlyWithFoodAPIBK;
import org.bukkit.Bukkit;

public class FlyWithFoodAPI_Folia extends FlyWithFoodAPIBK {
    @Override
    public void stopTasks() {
        Bukkit.getAsyncScheduler().cancelTasks(FlyWithFoodBukkit.getInstance());
        Bukkit.getGlobalRegionScheduler().cancelTasks(FlyWithFoodBukkit.getInstance());
    }

    @Override
    public void runTask(Runnable r) {
        Bukkit.getGlobalRegionScheduler().run(FlyWithFoodBukkit.getInstance(), (task) -> r.run());
    }

    @Override
    public void runTaskAsync(Runnable r) {
        Bukkit.getAsyncScheduler().runNow(FlyWithFoodBukkit.getInstance(), (task) -> r.run());
    }

    @Override
    public void runTaskTimerAsync(Runnable r, long waitSeconds, long periodSeconds) {
        Bukkit.getAsyncScheduler().runAtFixedRate(FlyWithFoodBukkit.getInstance(), (task) -> r.run(), waitSeconds, periodSeconds, TimeUnit.SECONDS);
    }
}
