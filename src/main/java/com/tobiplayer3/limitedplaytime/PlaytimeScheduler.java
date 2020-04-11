package com.tobiplayer3.limitedplaytime;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PlaytimeScheduler extends BukkitRunnable {

    private PlaytimeManager playtimeManager = PlaytimeManager.getManager();

    public void run() {

        // synchronous task to access bukkit api
        new BukkitRunnable() {
            public void run() {

                for (Player player : Bukkit.getOnlinePlayers()) {
                    PlaytimePlayer playtimePlayer = playtimeManager.getPlayer(player.getUniqueId());
                    playtimePlayer.setPlaytime(playtimePlayer.getPlaytime() - 1);
                    player.sendMessage(playtimePlayer.getPlaytime().toString());
                }

            }
        }.runTask(LimitedPlaytime.getInstance());

    }

}
