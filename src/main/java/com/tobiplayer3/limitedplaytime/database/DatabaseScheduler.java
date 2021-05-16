package com.tobiplayer3.limitedplaytime.database;

import com.tobiplayer3.limitedplaytime.LimitedPlaytime;
import com.tobiplayer3.limitedplaytime.PlaytimeManager;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class DatabaseScheduler extends BukkitRunnable {
    private final LimitedPlaytime limitedPlaytime;
    private final PlaytimeManager playtimeManager;

    public DatabaseScheduler(LimitedPlaytime limitedPlaytime) {
        this.limitedPlaytime = limitedPlaytime;
        this.playtimeManager = limitedPlaytime.getPlaytimeManager();
    }

    @Override
    public void run() {
        limitedPlaytime.getDB().savePlayers(new ArrayList<>(playtimeManager.getPlaytimes().values()));
    }
}
