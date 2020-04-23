package com.tobiplayer3.limitedplaytime.listeners;

import com.tobiplayer3.limitedplaytime.LimitedPlaytime;
import com.tobiplayer3.limitedplaytime.PlaytimeManager;
import com.tobiplayer3.limitedplaytime.PlaytimePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.LocalDate;
import java.util.UUID;

public class JoinListener implements Listener {

    private PlaytimeManager playtimeManager = PlaytimeManager.getManager();
    private LimitedPlaytime limitedPlaytime = LimitedPlaytime.getInstance();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();

        PlaytimePlayer playtimePlayer = playtimeManager.getPlayer(uuid);
        if (playtimePlayer == null) {
            playtimePlayer = playtimeManager.registerPlayer(uuid, playtimeManager.getMaxPlaytime(uuid), LocalDate.now());
        }

        if (playtimePlayer.getLastLogin().isBefore(LocalDate.now())) {
            if (playtimeManager.isPlaytimeStacking()) {
                playtimePlayer.setPlaytime(playtimePlayer.getPlaytime() + playtimeManager.getMaxPlaytime(uuid)*
                        playtimePlayer.getLastLogin().until(LocalDate.now()).getDays());
            } else {
                playtimePlayer.setPlaytime(playtimeManager.getMaxPlaytime(uuid));
            }
        }

        playtimePlayer.setLastLogin(LocalDate.now());
    }

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent e) {
        PlaytimePlayer player = playtimeManager.loadPlayer(e.getUniqueId());
        if (player == null) {
            return;
        }
        if (player.getPlaytime() <= 0 && !player.getLastLogin().isBefore(LocalDate.now())) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "Playtime: " + player.getPlaytime());
        }
    }

}
