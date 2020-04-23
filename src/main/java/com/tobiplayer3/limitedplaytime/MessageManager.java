package com.tobiplayer3.limitedplaytime;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.StringUtil;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MessageManager {

    private static MessageManager messageManager;
    private LimitedPlaytime limitedPlaytime = LimitedPlaytime.getInstance();

    private FileConfiguration messageConfig = YamlConfiguration.loadConfiguration(new File(limitedPlaytime.getDataFolder(), limitedPlaytime.getLanguage() + ".yml"));

    private Map<Message, String> messages;

    public static MessageManager getManager() {
        if (messageManager == null) {
            messageManager = new MessageManager();
        }
        return messageManager;
    }

    public void loadMessages() {
        Map<Message, String> loadedMessages = new HashMap<>();
        for (Message message : Message.values()) {
            loadedMessages.put(message, messageConfig.getString(message.name().toLowerCase(), message.getDefault()));
        }
        messages = Collections.unmodifiableMap(loadedMessages);
    }

    public String getMessage(Message message, PlaytimePlayer playtimePlayer) {
        String m = ChatColor.translateAlternateColorCodes('&', messages.get(message));

        int totalSeconds = playtimePlayer.getPlaytime() / 20;

        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        if (message == Message.TIME || message == Message.TIME_SHORT) {
            return m.replaceAll("%h%", Integer.toString(hours))
                    .replaceAll("%m%", Integer.toString(minutes))
                    .replaceAll("%s%", Integer.toString(seconds))
                    .replaceAll("%hh%", StringUtils.leftPad(Integer.toString(hours), 2, '0'))
                    .replaceAll("%mm%", StringUtils.leftPad(Integer.toString(minutes), 2, '0'))
                    .replaceAll("%ss%", StringUtils.leftPad(Integer.toString(seconds), 2, '0'));
        }

        return m.replaceAll("%time%", getMessage(Message.TIME, playtimePlayer))
                .replaceAll("%time_short%", getMessage(Message.TIME_SHORT, playtimePlayer));
    }

}
