package com.tobiplayer3.limitedplaytime;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class LimitedPlaytime extends JavaPlugin {

    @Override
    public void onEnable() {
        BukkitTask playtimeScheduler = new PlaytimeScheduler().runTaskTimerAsynchronously(this, 0, 1);
    }

    @Override
    public void onDisable() {

    }

}
