package com.tobiplayer3.limitedplaytime.api;

import com.tobiplayer3.limitedplaytime.PlaytimeManager;
import com.tobiplayer3.limitedplaytime.PlaytimePlayer;

import java.time.LocalDate;
import java.util.UUID;

public class LimitedPlaytimeAPI {

    private static final PlaytimeManager playtimeManager = PlaytimeManager.getManager();

    private LimitedPlaytimeAPI(){}

    public static Integer getPlaytime(UUID uuid){
        boolean loaded = true;
        if(!playtimeManager.isLoaded(uuid)){
            loaded = false;
            playtimeManager.loadPlayer(uuid);
        }

        PlaytimePlayer playtimePlayer = playtimeManager.getPlayer(uuid);
        if(playtimePlayer == null){
            return null;
        }

        if(!loaded){
            playtimeManager.unloadPlayer(uuid);
        }

        return playtimePlayer.getPlaytime();
    }

    public static void setPlaytime(UUID uuid, Integer playtime){
        boolean loaded = true;
        if(!playtimeManager.isLoaded(uuid)){
            loaded = false;
            playtimeManager.loadPlayer(uuid);
        }

        PlaytimePlayer playtimePlayer = playtimeManager.getPlayer(uuid);
        if(playtimePlayer == null){
            return;
        }
        playtimePlayer.setPlaytime(playtime);

        if(!loaded){
            playtimeManager.unloadPlayer(uuid);
        }
    }

    public static LocalDate getLastLogin(UUID uuid){
        boolean loaded = true;
        if(!playtimeManager.isLoaded(uuid)){
            loaded = false;
            playtimeManager.loadPlayer(uuid);
        }

        PlaytimePlayer playtimePlayer = playtimeManager.getPlayer(uuid);
        if(playtimePlayer == null){
            return null;
        }

        if(!loaded){
            playtimeManager.unloadPlayer(uuid);
        }

        return playtimePlayer.getLastLogin();
    }

    public static void setLastLogin(UUID uuid, LocalDate date){
        boolean loaded = true;
        if(!playtimeManager.isLoaded(uuid)){
            loaded = false;
            playtimeManager.loadPlayer(uuid);
        }

        PlaytimePlayer playtimePlayer = playtimeManager.getPlayer(uuid);
        if(playtimePlayer == null){
            return;
        }
        playtimePlayer.setLastLogin(date);

        if(!loaded){
            playtimeManager.unloadPlayer(uuid);
        }
    }
}
