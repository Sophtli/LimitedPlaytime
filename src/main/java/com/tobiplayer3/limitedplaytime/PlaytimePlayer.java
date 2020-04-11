package com.tobiplayer3.limitedplaytime;

import java.time.LocalDate;
import java.util.UUID;

public class PlaytimePlayer {

    private Integer playtime;
    private LocalDate lastLogin;
    private UUID uuid;

    public PlaytimePlayer(UUID uuid, Integer playtime, LocalDate lastLogin){
        this.playtime = playtime;
        this.lastLogin = lastLogin;
        this.uuid = uuid;
    }

    public UUID getUUID() {
        return uuid;
    }

    public LocalDate getLastLogin() {
        return lastLogin;
    }

    public Integer getPlaytime() {
        return playtime;
    }

    public void setLastLogin(LocalDate lastLogin) {
        this.lastLogin = lastLogin;
    }

    public void setPlaytime(Integer playtime) {
        this.playtime = playtime;
    }
}
