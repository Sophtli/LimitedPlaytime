package com.tobiplayer3.limitedplaytime.listeners;

import com.tobiplayer3.limitedplaytime.LimitedPlaytime;
import com.tobiplayer3.limitedplaytime.PlaytimeManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class QuitListener implements Listener {

    private PlaytimeManager playtimeManager = PlaytimeManager.getManager();
    private LimitedPlaytime limitedPlaytime = LimitedPlaytime.getInstance();

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        new BukkitRunnable() {
            @Override
            public void run() {
                playtimeManager.unloadPlayer(e.getPlayer().getUniqueId());
            }
        }.runTaskAsynchronously(limitedPlaytime);
    }

}
