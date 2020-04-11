package com.tobiplayer3.limitedplaytime;

import com.tobiplayer3.limitedplaytime.database.Database;
import com.tobiplayer3.limitedplaytime.database.SQLite;
import com.tobiplayer3.limitedplaytime.listeners.JoinListener;
import com.tobiplayer3.limitedplaytime.listeners.QuitListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class LimitedPlaytime extends JavaPlugin {

    private static LimitedPlaytime instance;
    private PlaytimeManager playtimeManager;

    private Database database;

    @Override
    public void onEnable() {
        getLogger().info("LimitedPlaytime loading...");

        setInstance(this);

        playtimeManager = PlaytimeManager.getManager();
        database = new SQLite("limitedplaytime.db");

        registerEvents();

        // not sure if this should stay asynchronous, this way server lag won't stop the countdown
        new PlaytimeScheduler().runTaskTimerAsynchronously(this, 0, 1);

        getLogger().info("LimitedPlaytime loaded successfuly.");

        for(Player p : Bukkit.getOnlinePlayers()){
            playtimeManager.loadPlayer(p.getUniqueId());
        }
    }

    private void registerEvents(){
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new JoinListener(), this);
        pm.registerEvents(new QuitListener(), this);
    }

    public Database getDatabase() {
        return database;
    }

    private static void setInstance(LimitedPlaytime limitedPlaytime){
        instance = limitedPlaytime;
    }

    public static LimitedPlaytime getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        getLogger().info("LimitedPlaytime unloading...");
        // TODO: save data to db
        getLogger().info("LimitedPlaytime unloaded successfuly.");
    }

}
