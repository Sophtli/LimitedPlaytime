package com.tobiplayer3.limitedplaytime.database;

import com.tobiplayer3.limitedplaytime.LimitedPlaytime;
import com.tobiplayer3.limitedplaytime.PlaytimeManager;
import org.bukkit.scheduler.BukkitRunnable;

public class DatabaseScheduler extends BukkitRunnable {

    private PlaytimeManager playtimeManager = PlaytimeManager.getManager();
    private LimitedPlaytime limitedPlaytime = LimitedPlaytime.getInstance();

    @Override
    public void run() {

            limitedPlaytime.getDatabase().savePlayers(playtimeManager.getPlayers());

    }

}
