package com.tobiplayer3.limitedplaytime;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.LocalDate;
import java.util.*;

public class PlaytimeManager {

    private static PlaytimeManager manager = null;
    private final LimitedPlaytime limitedPlaytime = LimitedPlaytime.getInstance();

    private boolean playtimeStacking;

    private final List<PlaytimePlayer> players = Collections.synchronizedList(new ArrayList<>());

    private Integer defaultMaxPlaytime;
    private Map<String, Integer> maxPlaytimes;
    private List<Integer> notifySteps;

    public static PlaytimeManager getManager() {
        if (manager == null) {
            manager = new PlaytimeManager();
        }
        return manager;
    }

    public PlaytimePlayer getPlayer(UUID uuid) {
        synchronized (players) {
            for (PlaytimePlayer player : players) {
                if (player.getUUID() == uuid) {
                    return player;
                }
            }
            return null;
        }
    }

    public PlaytimePlayer loadPlayer(UUID uuid) {
        PlaytimePlayer playtimePlayer = limitedPlaytime.getDatabase().loadPlayer(uuid);
        if (playtimePlayer == null) {
            return null;
        }
        players.add(playtimePlayer);
        return playtimePlayer;
    }

    public PlaytimePlayer registerPlayer(UUID uuid, Integer playtime, LocalDate date) {
        PlaytimePlayer player = new PlaytimePlayer(uuid, playtime, date);
        players.add(player);
        return player;
    }

    public void unloadPlayer(UUID uuid) {
        limitedPlaytime.getDatabase().savePlayer(getPlayer(uuid));
        players.remove(getPlayer(uuid));
    }

    public boolean isLoaded(UUID uuid) {
        synchronized (players) {
            for (PlaytimePlayer player : players) {
                if (player.getUUID() == uuid) {
                    return true;
                }
            }
            return false;
        }
    }

    public List<PlaytimePlayer> getPlayers() {
        return players;
    }

    public void setPlaytimeStacking(boolean playtimeStacking) {
        this.playtimeStacking = playtimeStacking;
    }

    public boolean isPlaytimeStacking() {
        return playtimeStacking;
    }

    public Integer getMaxPlaytime(UUID uuid) {
        Integer maxPlaytime = -1;
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return null;
        }
        for (String permission : maxPlaytimes.keySet()) {
            if (player.hasPermission(permission) && maxPlaytime < maxPlaytimes.get(permission)) {
                maxPlaytime = maxPlaytimes.get(permission);
            }
        }

        if (maxPlaytime == -1) {
            maxPlaytime = defaultMaxPlaytime;
        }
        return maxPlaytime;
    }

    public void setMaxPlaytimes(Map<String, Integer> maxPlaytimes) {
        this.maxPlaytimes = Collections.unmodifiableMap(maxPlaytimes);
    }

    public void setDefaultMaxPlaytime(Integer defaultMaxPlaytime) {
        this.defaultMaxPlaytime = defaultMaxPlaytime;
    }

    public void setNotifySteps(List<Integer> notifySteps) {
        this.notifySteps = Collections.unmodifiableList(notifySteps);
    }

    public boolean isNotifyStep(Integer tick) {
        if (notifySteps.contains(tick)) {
            return true;
        }
        return false;
    }
}
