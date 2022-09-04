package me.xpyex.plugin.flywithfood.bukkit.implementation;

import me.xpyex.plugin.flywithfood.common.implementation.AbstractFWFSender;
import me.xpyex.plugin.flywithfood.common.utils.Util;
import org.bukkit.command.CommandSender;

public class BukkitSender extends AbstractFWFSender {
    private final CommandSender sender;

    public BukkitSender(CommandSender sender) throws IllegalArgumentException {
        if (Util.checkNull(sender)) throw new IllegalArgumentException("sender为空，无法创建FWFSender实例");

        this.sender = sender;
        //
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getSender() {
        return (T) sender;
        //
    }

    @Override
    public boolean hasPermission(String perm) {
        return sender.hasPermission(perm);
        //
    }

    @Override
    public void sendMessage(String... messages) {
        sender.sendMessage(messages);
        //
    }

    @Override
    public String getName() {
        return sender.getName();
        //
    }
}
