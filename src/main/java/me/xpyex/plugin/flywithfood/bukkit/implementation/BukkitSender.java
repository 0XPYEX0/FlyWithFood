package me.xpyex.plugin.flywithfood.bukkit.implementation;

import me.xpyex.plugin.flywithfood.common.implementation.FWFSender;
import me.xpyex.plugin.flywithfood.common.utils.ReflectUtil;
import org.bukkit.command.CommandSender;

public class BukkitSender implements FWFSender {
    private final CommandSender sender;

    public BukkitSender(CommandSender sender) {
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

    @Override
    public boolean equals(Object o)  {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (ReflectUtil.isAssignable(o.getClass(), this.getClass())) {
            return ((FWFSender) o).getName().equals(this.getName());
        }
        return false;
    }
}
