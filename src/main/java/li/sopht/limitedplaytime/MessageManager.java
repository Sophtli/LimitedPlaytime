package li.sopht.limitedplaytime;

import com.google.common.collect.ImmutableMap;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public class MessageManager {
    private final ImmutableMap<Message, String> messages;

    public MessageManager(LimitedPlaytime limitedPlaytime) {
        FileConfiguration messageConfig = YamlConfiguration.loadConfiguration(new File(limitedPlaytime.getDataFolder(), limitedPlaytime.getPlaytimeConfig().getLanguage() + ".yml"));

        Map<Message, String> loadedMessages = new EnumMap<>(Message.class);
        for (Message message : Message.values()) {
            loadedMessages.put(message, messageConfig.getString(message.name().toLowerCase(), message.getDefault()));
        }
        messages = ImmutableMap.copyOf(loadedMessages);
    }

    public String getMessage(Message message, Playtime playtime) {
        String m = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(messages.get(message)));

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
                .replace("%hh%", String.format("%02d", Integer.toString(hours)))
                .replace("%mm%", String.format("%02d", Integer.toString(minutes)))
                .replace("%ss%", String.format("%02d", Integer.toString(seconds)));
    }

}
