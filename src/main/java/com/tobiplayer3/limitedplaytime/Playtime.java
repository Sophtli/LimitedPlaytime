package com.tobiplayer3.limitedplaytime;

import com.tobiplayer3.limitedplaytime.events.PlaytimeChangeEvent;
import org.bukkit.Bukkit;

import java.time.LocalDate;
import java.util.UUID;

public class Playtime {

    private final UUID uuid;
    private Integer timeRemaining;
    private LocalDate lastLogin;

    public Playtime(UUID uuid, Integer playtime, LocalDate lastLogin){
        this.uuid = uuid;
        this.timeRemaining = playtime;
        this.lastLogin = lastLogin;
    }

    public LocalDate getLastLogin() {
        return lastLogin;
    }

    public Integer getTimeRemaining() {
        return timeRemaining;
    }

    public void setLastLogin(LocalDate lastLogin) {
        this.lastLogin = lastLogin;
    }

    public void setTimeRemaining(Integer timeRemaining) {
        PlaytimeChangeEvent playtimeChangeEvent = new PlaytimeChangeEvent(uuid, timeRemaining, this.timeRemaining);
        Bukkit.getPluginManager().callEvent(playtimeChangeEvent);
        if(playtimeChangeEvent.isCancelled()) return;

        this.timeRemaining = playtimeChangeEvent.getNewPlaytime();
    }

    public UUID getUUID() {
        return uuid;
    }
}
