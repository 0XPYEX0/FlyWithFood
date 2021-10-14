package me.xpyex.plugin.flywithfood.bukkit.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class FWFDisableFlyEvent extends FWFEvent implements Cancellable {
    Player player;
    public Player getPlayer() {
        return this.player;
    }
    public FWFDisableFlyEvent(Player player) {
        this.player = player;
    }
    boolean cancel;

    @Override
    public boolean isCancelled() {
        return this.cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
