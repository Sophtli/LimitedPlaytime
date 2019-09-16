package com.tobiplayer3.limitedplaytime;

import java.util.Date;
import java.util.UUID;

public class PlaytimePlayer {

    private Integer playtime;
    private Date lastLogin;

    public PlaytimePlayer(Integer playtime, Date lastLogin){
        this.playtime = playtime;
        this.lastLogin = lastLogin;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public Integer getPlaytime() {
        return playtime;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public void setPlaytime(Integer playtime) {
        this.playtime = playtime;
    }
}
