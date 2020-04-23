package com.tobiplayer3.limitedplaytime;

import com.tobiplayer3.limitedplaytime.commands.PlaytimeCommand;
import com.tobiplayer3.limitedplaytime.database.Database;
import com.tobiplayer3.limitedplaytime.database.DatabaseScheduler;
import com.tobiplayer3.limitedplaytime.database.MySQL;
import com.tobiplayer3.limitedplaytime.database.SQLite;
import com.tobiplayer3.limitedplaytime.listeners.JoinListener;
import com.tobiplayer3.limitedplaytime.listeners.QuitListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class LimitedPlaytime extends JavaPlugin {

    private static LimitedPlaytime instance;
    private PlaytimeManager playtimeManager;
    private MessageManager messageManager;

    private Database database;

    private Integer autoSaveInterval;
    private String language;

    private String viewPlaytimePermission;
    private String viewOthersPlaytimePermission;
    private String editPlaytimePermission;

    @Override
    public void onEnable() {
        getLogger().info("LimitedPlaytime loading...");

        getDataFolder().mkdirs();

        setInstance(this);

        playtimeManager = PlaytimeManager.getManager();
        messageManager = MessageManager.getManager();

        loadConfig();

        registerEvents();
        registerCommands();

        new PlaytimeScheduler().runTaskTimerAsynchronously(this, 0, 1);
        // save to database every autoSaveInterval seconds
        new DatabaseScheduler().runTaskTimerAsynchronously(this, 0, autoSaveInterval);

        getLogger().info("LimitedPlaytime loaded successfuly.");

        List<UUID> players = new ArrayList<>();
        for(Player p : Bukkit.getOnlinePlayers()){
            players.add(p.getUniqueId());
        }
        playtimeManager.loadPlayers(players);
    }

    private void loadConfig() {
        saveDefaultConfig();

        boolean mysqlEnabled = getConfig().getBoolean("mysql.enabled", false);
        String mysqlHost = getConfig().getString("mysql.host", "");
        String mysqlDatabase = getConfig().getString("mysql.database", "");
        String mysqlUsername = getConfig().getString("mysql.username", "");
        String mysqlPassword = getConfig().getString("mysql.password", "");
        int mysqlPort = getConfig().getInt("mysql.port", 3306);

        if (mysqlEnabled) {
            database = new MySQL(mysqlHost, mysqlPort, mysqlDatabase, mysqlUsername, mysqlPassword);
        } else {
            database = new SQLite();
        }

        autoSaveInterval = getConfig().getInt("autosave_interval", 20 * 60 * 10);
        language = getConfig().getString("language", "en");
        playtimeManager.setPlaytimeStacking(getConfig().getBoolean("stack_playtime", false));
        playtimeManager.setDefaultMaxPlaytime(getConfig().getInt("default_playtime"));

        Map<String, Integer> maxPlaytimes = new ConcurrentHashMap<>();

        List<Map<?, ?>> maxPlaytimeList = getConfig().getMapList("permission_playtime");
        for(Map<?, ?> map : maxPlaytimeList){
            Map<String, Integer> permissionMap = (Map<String, Integer>) map;
            for(Map.Entry<String, Integer> permission : permissionMap.entrySet()){
                maxPlaytimes.put(permission.getKey(), permission.getValue());
            }
        }
        playtimeManager.setMaxPlaytimes(maxPlaytimes);
        playtimeManager.setNotifySteps(getConfig().getIntegerList("notify_steps"));

        viewPlaytimePermission = getConfig().getString("view_playtime_permission", "");
        viewOthersPlaytimePermission = getConfig().getString("view_others_playtime_permission", "limitedplaytime.viewothers");
        editPlaytimePermission = getConfig().getString("edit_playtime_permission", "limitedplaytime.edit");

        new File(getDataFolder() + "/lang").mkdirs();

        String[] languages = {"en", "de"};
        for (String lang : languages) {
            saveResource("lang/" + lang + ".yml", true);
        }

        messageManager.loadMessages();
    }

    private void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new JoinListener(), this);
        pm.registerEvents(new QuitListener(), this);
    }

    private void registerCommands() {
        getCommand("playtime").setExecutor(new PlaytimeCommand());
        getCommand("playtime").setTabCompleter(new PlaytimeCommand());
    }

    public Database getDatabase() {
        return database;
    }

    private static void setInstance(LimitedPlaytime limitedPlaytime) {
        instance = limitedPlaytime;
    }

    public static LimitedPlaytime getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        getLogger().info("LimitedPlaytime unloading...");

        playtimeManager.unloadPlayers(playtimeManager.getPlayers());

        getLogger().info("LimitedPlaytime unloaded successfuly.");
    }

    public String getViewOthersPlaytimePermission() {
        return viewOthersPlaytimePermission;
    }

    public String getViewPlaytimePermission() {
        return viewPlaytimePermission;
    }

    public String getEditPlaytimePermission() {
        return editPlaytimePermission;
    }

    public String getLanguage() {
        return language;
    }
}
