package com.tobiplayer3.limitedplaytime.database;

import com.tobiplayer3.limitedplaytime.LimitedPlaytime;
import com.tobiplayer3.limitedplaytime.Playtime;
import com.tobiplayer3.limitedplaytime.PlaytimeManager;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;

public class DatabaseScheduler extends BukkitRunnable {

    private final PlaytimeManager playtimeManager = PlaytimeManager.getManager();
    private final LimitedPlaytime limitedPlaytime = LimitedPlaytime.getInstance();

    @Override
    public void run() {

        for (Map.Entry<UUID, Playtime> playtimeEntry : playtimeManager.getPlaytimes().entrySet()) {
            limitedPlaytime.getDB().savePlayer(playtimeEntry.getKey(), playtimeEntry.getValue());
        }

    }

}
