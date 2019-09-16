package com.tobiplayer3.limitedplaytime;

import java.util.GregorianCalendar;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlaytimeManager {

    private static PlaytimeManager manager;

    private Map<UUID, PlaytimePlayer> players = new ConcurrentHashMap<UUID, PlaytimePlayer>();

    public static PlaytimeManager getManager() {
        if(manager == null){
            manager = new PlaytimeManager();
        }
        return manager;
    }

    public PlaytimePlayer getPlayer(UUID uuid) {
        return players.get(uuid);
    }

    public void loadPlayer(UUID uuid){
        PlaytimePlayer playtimePlayer = new PlaytimePlayer(123, GregorianCalendar.getInstance().getTime());
        players.put(uuid, playtimePlayer);
    }

}
