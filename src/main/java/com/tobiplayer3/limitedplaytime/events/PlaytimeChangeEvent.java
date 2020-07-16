package com.tobiplayer3.limitedplaytime.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlaytimeChangeEvent extends Event implements Cancellable {

    @Override
    public HandlerList getHandlers() {
        return null;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean b) {

    }
}
