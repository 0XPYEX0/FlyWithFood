package me.xpyex.plugin.flywithfood.bukkit.commands;

import me.xpyex.plugin.flywithfood.bukkit.implementations.BukkitSender;
import me.xpyex.plugin.flywithfood.bukkit.implementations.BukkitUser;
import me.xpyex.plugin.flywithfood.common.commands.FWFCmd;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        FWFCmd.onCmd((sender instanceof Player) ? new BukkitUser((Player) sender) : new BukkitSender(sender), label, args);
        return true;
    }
}