package com.tobiplayer3.limitedplaytime.commands;

import com.tobiplayer3.limitedplaytime.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PlaytimeCommand implements CommandExecutor, TabCompleter {

    private PlaytimeManager playtimeManager = PlaytimeManager.getManager();
    private LimitedPlaytime limitedPlaytime = LimitedPlaytime.getInstance();
    private MessageManager messageManager = MessageManager.getManager();

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player)) {
            return true;
        }
        Player p = (Player) cs;
        PlaytimePlayer playtimePlayer = playtimeManager.getPlayer(p.getUniqueId());

        if (!p.hasPermission(limitedPlaytime.getViewPlaytimePermission())
                && !p.hasPermission(limitedPlaytime.getViewOthersPlaytimePermission())
                && !p.hasPermission(limitedPlaytime.getEditPlaytimePermission())) {
            return true;
        }

        if (args.length == 0) {
            p.sendMessage(messageManager.getMessage(Message.PLAYTIME_COMMAND, playtimePlayer));
        }

        if (args.length == 1) {
            p.sendMessage(messageManager.getMessage(Message.PLAYTIME_COMMAND_ERROR, playtimePlayer));
        }

        if (args.length == 2) {
            p.sendMessage(messageManager.getMessage(Message.PLAYTIME_COMMAND_ERROR, playtimePlayer));
        }

        if (args.length == 3) {
            p.sendMessage(messageManager.getMessage(Message.PLAYTIME_COMMAND_ERROR, playtimePlayer));
        }

        if (args.length == 4) {
            p.sendMessage(messageManager.getMessage(Message.PLAYTIME_EDIT_COMMAND, playtimePlayer));
        }

        return true;
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
            }
            if (p.hasPermission(limitedPlaytime.getViewOthersPlaytimePermission())
                    || p.hasPermission(limitedPlaytime.getEditPlaytimePermission())) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player == p) {
                        continue;
                    }
                    l.add(p.getName());
                }
            }
            l = l.stream().filter(s -> StringUtil.startsWithIgnoreCase(s, args[0])).collect(Collectors.toList());
        } else if (args.length == 5) {
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
            l = l.stream().filter(s -> StringUtil.startsWithIgnoreCase(s, args[0])).collect(Collectors.toList());
        }

        return l;
    }

}
