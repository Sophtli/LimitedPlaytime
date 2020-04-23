package com.tobiplayer3.limitedplaytime;

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

    public static MessageManager getMessageManager() {
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

    public String getMessage(Message message){
        return messages.get(message);
    }

}
