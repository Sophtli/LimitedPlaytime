package com.tobiplayer3.limitedplaytime;

import com.tobiplayer3.limitedplaytime.events.PlaytimeChangeEvent;
import org.bukkit.Bukkit;

import java.time.LocalDate;

public class Playtime {

    private Integer timeRemaining;
    private LocalDate lastLogin;

    public Playtime(Integer playtime, LocalDate lastLogin){
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
        PlaytimeChangeEvent playtimeChangeEvent = new PlaytimeChangeEvent();
        Bukkit.getPluginManager().callEvent(playtimeChangeEvent);
        if(playtimeChangeEvent.isCancelled()) return;

        this.timeRemaining = timeRemaining;
    }
}
