package li.sopht.limitedplaytime.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandHandler implements CommandExecutor, TabCompleter {

    @NotNull
    private final CommandManager commandManager;

    public CommandHandler(@NotNull CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender cs, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (cs instanceof Player) {
            Player p = (Player) cs;
            commandManager.getCommand(cmd.getName()).run(p, args);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String label, String[] args) {
        if (cs instanceof Player) {
            Player p = (Player) cs;
            int i = args.length - 1;
            List<String> autoCompletions = commandManager.getCommand(cmd.getName()).getAutoCompletions(p, args);
            return StringUtil.copyPartialMatches(args[i], autoCompletions, new ArrayList<>());
        }
        return new ArrayList<>();
    }
}
