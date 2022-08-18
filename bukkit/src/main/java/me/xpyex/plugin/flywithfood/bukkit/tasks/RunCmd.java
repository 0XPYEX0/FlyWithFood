package me.xpyex.plugin.flywithfood.bukkit.tasks;

import me.xpyex.plugin.flywithfood.bukkit.FlyWithFoodBukkit;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

public class RunCmd extends BukkitRunnable {
    private final CommandSender sender;
    private final String cmd;

    public RunCmd(CommandSender sender, String cmd) {
        this.sender = sender;
        this.cmd = cmd;
    }

    @Override
    public void run() {
        Bukkit.dispatchCommand(sender, cmd);
        //
    }

    public void start() {
        this.runTask(FlyWithFoodBukkit.BUKKIT_INSTANCE);
        //
    }
}
