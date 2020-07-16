package com.tobiplayer3.limitedplaytime.listeners;

import com.tobiplayer3.limitedplaytime.LimitedPlaytime;
import com.tobiplayer3.limitedplaytime.PlaytimeManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class QuitListener implements Listener {

    private final PlaytimeManager playtimeManager = PlaytimeManager.getManager();

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        playtimeManager.unloadPlayer(e.getPlayer().getUniqueId());
    }

}
