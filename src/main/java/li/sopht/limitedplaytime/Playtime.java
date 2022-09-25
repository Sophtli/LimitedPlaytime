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

    /**
     * Gets the date of the last login.
     *
     * @return local date of the last login
     */
    @NotNull
    public LocalDate getLastLogin() {
        return lastLogin;
    }

    /**
     * Gets the remaining time.
     *
     * @return remaining time in ticks
     */
    public int getTimeRemaining() {
        return timeRemaining;
    }

    /**
     * Sets the date of the last login.
     *
     * @param lastLogin local date of the last login
     */
    public void setLastLogin(@NotNull LocalDate lastLogin) {
        this.lastLogin = lastLogin;
    }

    /**
     * Sets the remaining time.
     *
     * @param timeRemaining remaining time in ticks
     */
    public void setTimeRemaining(int timeRemaining) {
        PlaytimeChangeEvent playtimeChangeEvent = new PlaytimeChangeEvent(uuid, timeRemaining, this.timeRemaining);
        Bukkit.getPluginManager().callEvent(playtimeChangeEvent);
        if (playtimeChangeEvent.isCancelled()) return;

        this.timeRemaining = playtimeChangeEvent.getNewPlaytime();
    }

    /**
     * Gets the uuid of the player that belongs to this playtime.
     *
     * @return uuid of the related player
     */
    @NotNull
    public UUID getUUID() {
        return uuid;
    }
}
