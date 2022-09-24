package li.sopht.limitedplaytime.commands;

import li.sopht.limitedplaytime.LimitedPlaytime;
import org.bukkit.command.PluginCommand;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandManager {
    @NotNull
    private final LimitedPlaytime limitedPlaytime;

    public CommandManager(@NotNull LimitedPlaytime limitedPlaytime) {
        this.limitedPlaytime = limitedPlaytime;
    }

    private final Map<String, Command> commands = new HashMap<>();

    public void addCommand(Command command) {
        commands.put(command.getName(), command);

        PluginCommand pluginCommand = limitedPlaytime.getCommand(command.getName());
        pluginCommand.setTabCompleter(limitedPlaytime.getCommandHandler());
        pluginCommand.setExecutor(limitedPlaytime.getCommandHandler());
    }

    @Nullable
    public Command getCommand(String name) {
        return commands.get(name);
    }
}
