package com.tobiplayer3.limitedplaytime;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class PlaytimeScheduler extends BukkitRunnable {

    private PlaytimeManager playtimeManager = PlaytimeManager.getManager();
    private LimitedPlaytime limitedPlaytime = LimitedPlaytime.getInstance();
    private MessageManager messageManager = MessageManager.getManager();

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

                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(messageManager.getMessage(Message.TIME_SHORT, playtimePlayer)));

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
