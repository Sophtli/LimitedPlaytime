package com.tobiplayer3.limitedplaytime;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class PlaytimeScheduler extends BukkitRunnable {

    private PlaytimeManager playtimeManager = PlaytimeManager.getManager();
    private LimitedPlaytime limitedPlaytime = LimitedPlaytime.getInstance();

    public void run() {

        new BukkitRunnable() {
            @Override
            public void run() {

                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (!playtimeManager.isLoaded(player.getUniqueId())) {
                        continue;
                    }
                    PlaytimePlayer playtimePlayer = playtimeManager.getPlayer(player.getUniqueId());

                    int newPlaytime = playtimePlayer.getPlaytime() - 1;
                    playtimePlayer.setPlaytime(newPlaytime);

                    if (playtimeManager.isNotifyStep(newPlaytime)) {
                        player.sendMessage(Integer.toString(playtimePlayer.getPlaytime() / 20));
                    }

                    if (newPlaytime <= 0) {
                        player.kickPlayer("test!");
                    }
                }
                
            }
        }.runTask(limitedPlaytime);

    }

}
