package li.sopht.limitedplaytime;

import li.sopht.limitedplaytime.events.PlaytimeChangeEvent;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public class Playtime {
    @NotNull
    private final UUID uuid;
    private int timeRemaining;
    @NotNull
    private LocalDate lastLogin;

    public Playtime(@NotNull UUID uuid, int playtime, @NotNull LocalDate lastLogin) {
        this.uuid = uuid;
        this.timeRemaining = playtime;
        this.lastLogin = lastLogin;
    }

    @NotNull
    public LocalDate getLastLogin() {
        return lastLogin;
    }

    public int getTimeRemaining() {
        return timeRemaining;
    }

    public void setLastLogin(@NotNull LocalDate lastLogin) {
        this.lastLogin = lastLogin;
    }

    public void setTimeRemaining(int timeRemaining) {
        PlaytimeChangeEvent playtimeChangeEvent = new PlaytimeChangeEvent(uuid, timeRemaining, this.timeRemaining);
        Bukkit.getPluginManager().callEvent(playtimeChangeEvent);
        if (playtimeChangeEvent.isCancelled()) return;

        this.timeRemaining = playtimeChangeEvent.getNewPlaytime();
    }

    @NotNull
    public UUID getUUID() {
        return uuid;
    }
}
