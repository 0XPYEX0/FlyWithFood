package me.xpyex.plugin.flywithfood.bukkit.events;

import org.bukkit.entity.Player;

public class FWFPlayerBeenDisableFlyEvent extends FWFDisableFlyEvent {
    public FWFPlayerBeenDisableFlyEvent(Player player) {
        super(player);
    }
}
