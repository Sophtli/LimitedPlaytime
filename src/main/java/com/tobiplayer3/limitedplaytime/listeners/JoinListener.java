package com.tobiplayer3.limitedplaytime.listeners;

import com.tobiplayer3.limitedplaytime.PlaytimeManager;
import com.tobiplayer3.limitedplaytime.PlaytimePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.time.LocalDate;

public class JoinListener implements Listener {

    private PlaytimeManager playtimeManager = PlaytimeManager.getManager();

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        playtimeManager.loadPlayer(e.getPlayer().getUniqueId());
    }

}
