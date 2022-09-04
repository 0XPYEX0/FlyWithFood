package me.xpyex.plugin.flywithfood.sponge7.implementation;

import me.xpyex.plugin.flywithfood.common.implementation.AbstractFWFSender;
import me.xpyex.plugin.flywithfood.common.utils.Util;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;

public class SpongeSender extends AbstractFWFSender {
    private final CommandSource sender;

    public SpongeSender(CommandSource sender) throws IllegalArgumentException {
        if (Util.checkNull(sender)) throw new IllegalArgumentException("sender为空，无法创建FWFSender实例");

        this.sender = sender;
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
        for (String s : messages) {
            sender.sendMessage(Text.of(s));
        }
    }

    @Override
    public String getName() {
        return sender.getName();
        //
    }
}
