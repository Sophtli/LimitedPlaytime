package li.sopht.limitedplaytime;

import li.sopht.limitedplaytime.commands.*;
import li.sopht.limitedplaytime.database.Database;
import li.sopht.limitedplaytime.database.DatabaseScheduler;
import li.sopht.limitedplaytime.database.MySQL;
import li.sopht.limitedplaytime.database.SQLite;
import li.sopht.limitedplaytime.listeners.JoinListener;
import li.sopht.limitedplaytime.listeners.QuitListener;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

public class LimitedPlaytime extends JavaPlugin {
    private PlaytimeManager playtimeManager;
    private MessageManager messageManager;
    private CommandManager commandManager;
    private CommandHandler commandHandler;
    private Utils utils;
    private LimitedPlaytimeAPI api;

    private Database database;
    private PlaytimeConfig playtimeConfig;

    public static final String PREFIX = "[LimitedPlaytime]";

    @Override
    public void onEnable() {
        getLogger().info("loading...");
        saveDefaultConfig();
        playtimeConfig = new PlaytimeConfig();

        getDataFolder().mkdirs();

        playtimeManager = new PlaytimeManager(this);
        messageManager = new MessageManager(this);
        commandManager = new CommandManager(this);
        commandHandler = new CommandHandler(commandManager);
        utils = new Utils(this);
        api = new LimitedPlaytimeAPI(this);

        loadConfig();

        registerEvents();
        registerCommands();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaytimePlaceholder(this).register();
        }

        new PlaytimeScheduler(this).runTaskTimerAsynchronously(this, 0, 1);
        // save to database every autoSaveInterval seconds
        new DatabaseScheduler(this).runTaskTimerAsynchronously(this, 0, playtimeConfig.getAutoSaveInterval());

        for (Player p : Bukkit.getOnlinePlayers()) {
            playtimeManager.loadPlayer(p.getUniqueId());
        }

        getLogger().info("loaded successfully");
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
    public CommandHandler getCommandHandler() {
        return commandHandler;
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

    public LimitedPlaytimeAPI getAPI() {
        return api;
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

        // messageManager.loadMessages();
    }

    private void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new JoinListener(this), this);
        pm.registerEvents(new QuitListener(this), this);
    }

    private void registerCommands() {
        new Command("playtime")
                .withSubcommand(new Command("set")
                        .setHandler(
                                (p, args) -> {

                                }
                        )
                )

                .withSubcommand(new Command("add")
                        .withSubcommand(new Command("lol")
                                .setHandler(
                                        (p, params) -> {
                                            p.sendMessage("Meow " + params.getString(0));
                                        }
                                )
                                .setAutoCompleteHandler(0,
                                        p -> new ArrayList<>(Arrays.asList("yay", "yuuuu", "lol", "23", "pokemon"))
                                )
                        )
                        .setAutoCompleteHandler(1,
                                p -> new ArrayList<>(Arrays.asList("axe", "book", "dick", "4", "able"))
                        )
                        .setHandler(
                                (p, params) -> {
                                    p.sendMessage("Woof");
                                }
                        )
                )

                .setHandler(
                        (p, params) -> p.sendMessage("Playtime: " + params.getInteger(0) + " " + params.getPlayer(1).getAddress())
                )
                .setAutoCompleteHandler(1,
                        p -> new ArrayList<>(Arrays.asList("axe", "book", "dick", "4", "able"))
                )
                .register(commandManager);
    }

    @NotNull
    public Database getDB() {
        return database;
    }

    @Override
    public void onDisable() {
        getLogger().info("LimitedPlaytime unloading...");

        if (playtimeManager != null) {
            playtimeManager.unloadPlayers().join();
        }
        if (database != null) {
            database.shutdown();
        }

        getLogger().info("LimitedPlaytime unloaded successfuly.");
    }
}
