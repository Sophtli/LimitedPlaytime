package com.tobiplayer3.limitedplaytime.listeners;

import com.tobiplayer3.limitedplaytime.LimitedPlaytime;
import com.tobiplayer3.limitedplaytime.Playtime;
import com.tobiplayer3.limitedplaytime.PlaytimeManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.time.LocalDate;
import java.util.UUID;

public class JoinListener implements Listener {

    private final PlaytimeManager playtimeManager;

    public JoinListener(LimitedPlaytime limitedPlaytime) {
        playtimeManager = limitedPlaytime.getPlaytimeManager();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();

        playtimeManager.loadPlayer(uuid)
                .exceptionally(exception -> playtimeManager.createPlayer(uuid))
                .thenAcceptAsync(playtime -> {
                    if (playtime.getLastLogin().isBefore(LocalDate.now())) {
                        if (playtimeManager.isPlaytimeStacking()) {
                            playtime.setTimeRemaining(playtime.getTimeRemaining() + playtimeManager.getMaxPlaytime(uuid) *
                                    playtime.getLastLogin().until(LocalDate.now()).getDays());
                        } else {
                            playtime.setTimeRemaining(playtimeManager.getMaxPlaytime(uuid));
                        }
                    }

                    playtime.setLastLogin(LocalDate.now());
                });
    }

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent e) {
        UUID uuid = e.getUniqueId();
        Playtime playtime = playtimeManager.loadPlayer(uuid).exceptionally(exception -> null).join();
        if (playtime == null) {
            return;
        }
        if (playtime.getTimeRemaining() <= 0 && !playtime.getLastLogin().isBefore(LocalDate.now())) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "Playtime: " + playtime.getTimeRemaining());
        }
    }

}
