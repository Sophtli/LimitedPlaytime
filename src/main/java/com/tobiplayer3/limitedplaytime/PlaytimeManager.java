package com.tobiplayer3.limitedplaytime;

import org.bukkit.scheduler.BukkitRunnable;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlaytimeManager {

    private static PlaytimeManager manager = null;
    private final LimitedPlaytime limitedPlaytime = LimitedPlaytime.getInstance();

    private final Map<UUID, PlaytimePlayer> players = new ConcurrentHashMap<UUID, PlaytimePlayer>();

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
        PlaytimePlayer playtimePlayer = limitedPlaytime.getDatabase().loadPlayer(uuid);
        if(playtimePlayer == null){
            playtimePlayer = new PlaytimePlayer(uuid,123, LocalDate.now());
        }
        players.put(uuid, playtimePlayer);
    }

    public void unloadPlayer(UUID uuid){
        new BukkitRunnable() {
            @Override
            public void run() {
                limitedPlaytime.getDatabase().savePlayer(getPlayer(uuid));
                players.remove(uuid);
            }
        }.runTaskAsynchronously(limitedPlaytime);
    }

}
