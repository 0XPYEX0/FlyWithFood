package me.xpyex.plugin.flywithfood.bukkit.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FWFEvent extends Event {

    HandlerList handlerList = new HandlerList();

    public FWFEvent() {
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

}
