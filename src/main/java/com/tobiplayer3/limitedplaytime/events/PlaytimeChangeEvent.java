package com.tobiplayer3.limitedplaytime.events;

import com.tobiplayer3.limitedplaytime.Playtime;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class PlaytimeChangeEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private boolean cancelled;

    private final UUID uuid;
    private Integer newPlaytime;
    private final Integer oldPlaytime;

    public PlaytimeChangeEvent(UUID uuid, Integer newPlaytime, Integer oldPlaytime) {
        this.uuid = uuid;
        this.oldPlaytime = oldPlaytime;
        this.newPlaytime = newPlaytime;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public UUID getUUID() {
        return uuid;
    }

    public Integer getNewPlaytime() {
        return newPlaytime;
    }

    public void setNewPlaytime(Integer newPlaytime) {
        this.newPlaytime = newPlaytime;
    }

    public Integer getOldPlaytime() {
        return oldPlaytime;
    }
}
