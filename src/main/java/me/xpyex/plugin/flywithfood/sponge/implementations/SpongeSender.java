package me.xpyex.plugin.flywithfood.sponge.implementations;

import me.xpyex.plugin.flywithfood.common.implementations.FWFSender;
import me.xpyex.plugin.flywithfood.common.types.FWFMsgType;
import me.xpyex.plugin.flywithfood.sponge.utils.Utils;
import org.spongepowered.api.command.CommandSource;

public class SpongeSender implements FWFSender {
    private final CommandSource sender;

    public SpongeSender(CommandSource sender) {
        this.sender = sender;
    }

    @Override
    public void autoSendMsg(String... msg) {
        Utils.autoSendMsg(sender, msg);
        //
    }

    @Override
    public boolean hasPermission(String perm) {
        return sender.hasPermission(perm);
        //
    }

    @Override
    public String getName() {
        return sender.getName();
    }

    @Override
    public boolean isBukkit() {
        return false;
    }

    @Override
    public boolean isSponge() {
        return true;
    }

    @Override
    public void sendFWFMsg(FWFMsgType msg) {
        Utils.sendFWFMsg(sender, msg);
        //
    }

    @Override
    public boolean hasNoCostPerm() {
        return sender.hasPermission("fly.nohunger") || sender.hasPermission("fly.nocost");
    }
}
