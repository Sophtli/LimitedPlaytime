package li.sopht.limitedplaytime.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PlaytimeChangeEvent extends Event implements Cancellable {
    public enum Source {
        DEFAULT,
        COMMAND
    }

    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private boolean cancelled;

    @NotNull
    private final UUID uuid;
    private int newPlaytime;
    private final int oldPlaytime;
    @NotNull
    private final Source source;

    public PlaytimeChangeEvent(@NotNull UUID uuid, int newPlaytime, int oldPlaytime) {
        this.uuid = uuid;
        this.oldPlaytime = oldPlaytime;
        this.newPlaytime = newPlaytime;
        this.source = Source.DEFAULT;
    }

    public PlaytimeChangeEvent(@NotNull UUID uuid, int newPlaytime, int oldPlaytime, @NotNull Source source) {
        this.uuid = uuid;
        this.oldPlaytime = oldPlaytime;
        this.newPlaytime = newPlaytime;
        this.source = source;
    }

    @Override
    @NotNull
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    @NotNull
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

    @NotNull
    public UUID getUUID() {
        return uuid;
    }

    public int getNewPlaytime() {
        return newPlaytime;
    }

    public void setNewPlaytime(Integer newPlaytime) {
        this.newPlaytime = newPlaytime;
    }

    public int getOldPlaytime() {
        return oldPlaytime;
    }
}
