package me.xpyex.plugin.flywithfood.bukkit.commands;

import me.xpyex.plugin.flywithfood.bukkit.implementations.BukkitSender;
import me.xpyex.plugin.flywithfood.common.commands.FWFCmd;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class FlyCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        FWFCmd.onCmd(new BukkitSender(sender), label, args);
        return true;
    }
}