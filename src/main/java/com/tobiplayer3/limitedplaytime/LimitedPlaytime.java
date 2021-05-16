package com.tobiplayer3.limitedplaytime;

import com.tobiplayer3.limitedplaytime.commands.Command;
import com.tobiplayer3.limitedplaytime.commands.CommandManager;
import com.tobiplayer3.limitedplaytime.commands.PlaytimeCommand;
import com.tobiplayer3.limitedplaytime.commands.StringParameter;
import com.tobiplayer3.limitedplaytime.database.Database;
import com.tobiplayer3.limitedplaytime.database.DatabaseScheduler;
import com.tobiplayer3.limitedplaytime.database.MySQL;
import com.tobiplayer3.limitedplaytime.database.SQLite;
import com.tobiplayer3.limitedplaytime.listeners.JoinListener;
import com.tobiplayer3.limitedplaytime.listeners.QuitListener;
import kr.entree.spigradle.annotations.PluginMain;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PluginMain
public class LimitedPlaytime extends JavaPlugin {
    private PlaytimeManager playtimeManager;
    private MessageManager messageManager;
    private CommandManager commandManager;
    private Utils utils;

    private Database database;
    private PlaytimeConfig playtimeConfig;

    public static final String PREFIX = "[LimitedPlaytime]";

    @Override
    public void onEnable() {
        getLogger().info("LimitedPlaytime loading...");
        playtimeConfig = new PlaytimeConfig();

        getDataFolder().mkdirs();

        playtimeManager = new PlaytimeManager(this);
        messageManager = new MessageManager(this);
        commandManager = new CommandManager();
        utils = new Utils(this);

        loadConfig();

        registerEvents();
        registerCommands();

        new PlaytimeScheduler(this).runTaskTimerAsynchronously(this, 0, 1);
        // save to database every autoSaveInterval seconds
        new DatabaseScheduler(this).runTaskTimerAsynchronously(this, 0, playtimeConfig.getAutoSaveInterval());

        for (Player p : Bukkit.getOnlinePlayers()) {
            playtimeManager.loadPlayer(p.getUniqueId());
        }

        // bStats Metrics
        int pluginID = 7974;
        Metrics metrics = new Metrics(this, pluginID);

        getLogger().info("LimitedPlaytime loaded successfuly");
    }

    @NotNull
    public Utils getUtils() {
        return utils;
    }

    @NotNull
    public CommandManager getCommandManager() {
        return commandManager;
    }

    @NotNull
    public MessageManager getMessageManager() {
        return messageManager;
    }

    @NotNull
    public PlaytimeManager getPlaytimeManager() {
        return playtimeManager;
    }

    @NotNull
    public PlaytimeConfig getPlaytimeConfig() {
        return playtimeConfig;
    }

    private void loadConfig() {
        saveDefaultConfig();
        boolean mysqlEnabled = getConfig().getBoolean("mysql.enabled", false);

        if (mysqlEnabled) {
            String mysqlHost = getConfig().getString("mysql.host", "");
            String mysqlDatabase = getConfig().getString("mysql.database", "");
            String mysqlUsername = getConfig().getString("mysql.username", "");
            String mysqlPassword = getConfig().getString("mysql.password", "");
            int mysqlPort = getConfig().getInt("mysql.port", 3306);

            database = new MySQL(this, mysqlHost, mysqlPort, mysqlDatabase, mysqlUsername, mysqlPassword);
        } else {
            database = new SQLite(this);
        }

        playtimeConfig.setAutoSaveInterval(getConfig().getInt("autosave_interval", 20 * 60 * 10));
        playtimeConfig.setLanguage(getConfig().getString("language", "en"));

        playtimeManager.setPlaytimeStacking(getConfig().getBoolean("stack_playtime", false));
        playtimeManager.setDefaultMaxPlaytime(getConfig().getInt("default_playtime"));

        Map<String, Integer> maxPlaytimes = new HashMap<>();

        List<Map<?, ?>> maxPlaytimeList = getConfig().getMapList("playtime");
        for (Map<?, ?> map : maxPlaytimeList) {
            for (Map.Entry<?, ?> permission : map.entrySet()) {
                if (!(permission.getKey() instanceof String) || !(permission.getValue() instanceof Integer)) continue;
                maxPlaytimes.put((String) permission.getKey(), (Integer) permission.getValue());
            }
        }
        playtimeManager.setMaxPlaytimes(maxPlaytimes);
        playtimeManager.setNotifySteps(getConfig().getIntegerList("notify_steps"));

        playtimeConfig.setViewPlaytimePermission(getConfig().getString("view_playtime_permission", ""));
        playtimeConfig.setViewOthersPlaytimePermission(getConfig().getString("view_others_playtime_permission", "limitedplaytime.viewothers"));
        playtimeConfig.setEditPlaytimePermission(getConfig().getString("edit_playtime_permission", "limitedplaytime.edit"));

        new File(getDataFolder() + "/lang").mkdirs();

        String[] languages = {"en", "de"};
        for (String lang : languages) {
            saveResource("lang/" + lang + ".yml", true);
        }

        messageManager.loadMessages();
    }

    private void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new JoinListener(this), this);
        pm.registerEvents(new QuitListener(this), this);
    }

    private void registerCommands() {
        PluginCommand cmd = getCommand("playtime");
        if (cmd == null) {
            getLogger().severe("Playtime command could not be registered");
            return;
        }
        cmd.setExecutor(new PlaytimeCommand(this));
        cmd.setTabCompleter(new PlaytimeCommand(this));

        new Command("Playtime").setHandler(command -> {

        }).addParameter(new StringParameter("Player").setHandler(
                command -> {

                }
        ));
    }

    @NotNull
    public Database getDB() {
        return database;
    }

    @Override
    public void onDisable() {
        getLogger().info("LimitedPlaytime unloading...");

        playtimeManager.unloadPlayers().join();
        database.shutdown();

        getLogger().info("LimitedPlaytime unloaded successfuly.");
    }
}
