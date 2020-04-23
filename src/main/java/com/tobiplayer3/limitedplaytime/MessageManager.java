package com.tobiplayer3.limitedplaytime;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

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
        for(Message message : Message.values()){
            loadedMessages.put(message, messageConfig.getString(message.name().toLowerCase(), message.getDefault()));
        }
        messages = Collections.unmodifiableMap(loadedMessages);
    }

    public String getMessage(Message message, PlaytimePlayer playtimePlayer){
        String m = ChatColor.translateAlternateColorCodes('&', messages.get(message));

        int totalSeconds = playtimePlayer.getPlaytime() / 20;

        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        if(message == Message.TIME){
            return m.replaceAll("%h%", Integer.toString(hours)).replaceAll("%m%", Integer.toString(minutes)).replaceAll("%s%", Integer.toString(seconds));
        }

        return m.replaceAll("%time%", getMessage(Message.TIME, playtimePlayer));
    }

}
