package com.tobiplayer3.limitedplaytime.commands;

import com.tobiplayer3.limitedplaytime.*;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PlaytimeCommand implements CommandExecutor, TabCompleter {

    private final PlaytimeManager playtimeManager = PlaytimeManager.getManager();
    private final LimitedPlaytime limitedPlaytime = LimitedPlaytime.getInstance();
    private final MessageManager messageManager = MessageManager.getManager();

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        Utils.runAsync(() -> {
            if (!(cs instanceof Player)) {
                return;
            }
            Player p = (Player) cs;

            if (!p.hasPermission(limitedPlaytime.getViewPlaytimePermission())
                    && !p.hasPermission(limitedPlaytime.getViewOthersPlaytimePermission())
                    && !p.hasPermission(limitedPlaytime.getEditPlaytimePermission())) {
                return;
            }

            if (args.length == 0) {
                Playtime playtime = playtimeManager.getCachedPlaytime(p.getUniqueId());
                if (playtime == null) {
                    return;
                }
                p.sendMessage(messageManager.getMessage(Message.PLAYTIME_COMMAND, playtime));
                return;
            }

            String action = args[0];
            if (args.length == 3) {
                if (action.equalsIgnoreCase("reset")) {
                    resetPlaytime(p);
                    return;
                }
            }

            if (args.length == 4) {
                if(action.equalsIgnoreCase("set")){
                    setPlaytime(p, args);
                    return;
                }

                if (action.equalsIgnoreCase("add")) {
                    addPlaytime(p, args);
                    return;
                }

                if (action.equalsIgnoreCase("remove")) {
                    removePlaytime(p, args);
                    return;
                }
                switch (args[0].toLowerCase()) {
                    case "set":
                        setPlaytime(p, args);
                    case "add":
                    case "remove":
                    case "reset":
                    default:
                        OfflinePlayer target = Utils.getOfflinePlayer(args[0]).join();
                        playtimeManager.getPlaytime(target.getUniqueId())
                                .thenAccept(playtime -> p.spigot().sendMessage(new TextComponent(Integer.toString(playtime.getTimeRemaining()))))
                                .exceptionally(exception -> {
                                    p.spigot().sendMessage(new TextComponent("Der Spieler konnte nicht gefunden werden!"));
                                    return null;
                                });
                }
            }
        });

        return true;
    }

    private void setPlaytime(Player p, String[] args) {

    }

    private void addPlaytime(Player p, String[] args) {

    }

    private void removePlaytime(Player p, String[] args) {

    }

    private void resetPlaytime(Player p) {

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return new ArrayList<>();
        }
        Player p = (Player) sender;
        List<String> l = new ArrayList<>();

        if (args.length == 1) {
            if (p.hasPermission(limitedPlaytime.getEditPlaytimePermission())) {
                l.add("set");
                l.add("add");
                l.add("remove");
                l.add("reset");
            }
            if (p.hasPermission(limitedPlaytime.getViewOthersPlaytimePermission())
                    || p.hasPermission(limitedPlaytime.getEditPlaytimePermission())) {
                l.addAll(Bukkit.getOnlinePlayers().stream().filter(player -> player != p).map(HumanEntity::getName).collect(Collectors.toList()));
            }
            return l.stream().filter(s -> StringUtil.startsWithIgnoreCase(s, args[0])).collect(Collectors.toList());
        }

        if (args.length == 5) {
            if (!args[0].equalsIgnoreCase("set") && !args[0].equalsIgnoreCase("add") && !args[0].equalsIgnoreCase("remove")) {
                return new ArrayList<>();
            }
            if (p.hasPermission(limitedPlaytime.getEditPlaytimePermission())) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player == p) {
                        continue;
                    }
                    l.add(p.getName());
                }
            }
            return l.stream().filter(s -> StringUtil.startsWithIgnoreCase(s, args[0])).collect(Collectors.toList());
        }

        return l;
    }
}
