package com.tobiplayer3.limitedplaytime;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.EnumMap;
import java.util.Map;

public class MessageManager {
    private final FileConfiguration messageConfig;
    private ImmutableMap<Message, String> messages;

    public MessageManager(LimitedPlaytime limitedPlaytime) {
        messageConfig = YamlConfiguration.loadConfiguration(new File(limitedPlaytime.getDataFolder(), limitedPlaytime.getPlaytimeConfig().getLanguage() + ".yml"));
    }

    public void loadMessages() {
        Map<Message, String> loadedMessages = new EnumMap<>(Message.class);
        for (Message message : Message.values()) {
            loadedMessages.put(message, messageConfig.getString(message.name().toLowerCase(), message.getDefault()));
        }
        messages = ImmutableMap.copyOf(loadedMessages);
    }

    public String getMessage(Message message, Playtime playtime) {
        String m = ChatColor.translateAlternateColorCodes('&', messages.get(message));


        if (message == Message.TIME || message == Message.TIME_SHORT) {
            return formatTime(playtime.getTimeRemaining(), m);
        }

        return m.replace("%time%", getMessage(Message.TIME, playtime))
                .replace("%time_short%", getMessage(Message.TIME_SHORT, playtime));
    }

    public String formatTime(int ticks, String format) {
        int totalSeconds = ticks / 20;

        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        return format.replace("%h%", Integer.toString(hours))
                .replace("%m%", Integer.toString(minutes))
                .replace("%s%", Integer.toString(seconds))
                .replace("%hh%", StringUtils.leftPad(Integer.toString(hours), 2, '0'))
                .replace("%mm%", StringUtils.leftPad(Integer.toString(minutes), 2, '0'))
                .replace("%ss%", StringUtils.leftPad(Integer.toString(seconds), 2, '0'));
    }

}
