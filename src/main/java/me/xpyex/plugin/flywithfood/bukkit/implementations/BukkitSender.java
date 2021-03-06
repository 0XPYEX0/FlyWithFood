package me.xpyex.plugin.flywithfood.bukkit.implementations;

import me.xpyex.plugin.flywithfood.bukkit.utils.Utils;
import me.xpyex.plugin.flywithfood.common.implementations.FWFSender;
import me.xpyex.plugin.flywithfood.common.types.FWFMsgType;
import org.bukkit.command.CommandSender;

public class BukkitSender implements FWFSender {
    private final CommandSender sender;

    public BukkitSender(CommandSender sender) {
        this.sender = sender;
        //
    }

    @Override
    public boolean hasNoCostPerm() {
        return (sender.hasPermission("fly.nohunger") || sender.hasPermission("fly.nocost"));
    }

    @Override
    public boolean hasPermission(String perm) {  //新版在common包下处理命令，该方法用于判断权限.common包下无法使用BK/Sponge方法
        return sender.hasPermission(perm);
        //
    }

    @Override
    public String getName() {
        return sender.getName();
        //
    }

    @Override
    public boolean isBukkit() {
        return true;
        //
    }

    @Override
    public boolean isSponge() {
        return false;
        //
    }

    @Override
    public void autoSendMsg(String... msg) {
        Utils.autoSendMsg(sender, msg);
        //
    }

    @Override
    public void sendFWFMsg(FWFMsgType msg) {
        Utils.sendFWFMsg(sender, msg);
        //
    }

    @Override
    public boolean equals(FWFSender o) {
        if (this == o) {
            return true;
        }
        if (null == o) {
            return false;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        return this.sender.getName().equals(((BukkitSender) o).sender.getName());
    }

    @Override
    public void sendMessage(String s) {
        autoSendMsg(s);
        //我为什么写这个来着？？
    }
}
