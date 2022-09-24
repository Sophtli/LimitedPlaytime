package li.sopht.limitedplaytime.commands;

import li.sopht.limitedplaytime.LimitedPlaytime;
import li.sopht.limitedplaytime.exceptions.CommandException;
import li.sopht.limitedplaytime.exceptions.InvalidArgumentTypeException;
import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Consumer;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class Command {
    private final String name;
    private final Map<String, Command> subCommands = new HashMap<>();
    private final String permission;
    private BiConsumer<Player, Parameters> handler;
    private Map<Integer, Function<Player, List<String>>> autoCompleteHandlers = new HashMap<>();

    public Command(String name, String permission) {
        this.name = name;
        this.permission = permission;
    }

    public Command(String name) {
        this.name = name;
        this.permission = "";
    }

    public String getName() {
        return name;
    }

    public Command withSubcommand(Command subCommand) {
        subCommands.put(subCommand.getName().toLowerCase(), subCommand);
        return this;
    }

    public Command setHandler(BiConsumer<Player, Parameters> handler) {
        this.handler = handler;
        return this;
    }

    public Command setAutoCompleteHandler(int i, Function<Player, List<String>> autoCompleteHandler) {
        autoCompleteHandlers.put(i, autoCompleteHandler);
        return this;
    }

    public void run(@NotNull Player p, @NotNull String[] args) throws CommandException {
        if (args.length >= 1) {
            for (Map.Entry<String, Command> subCommand : subCommands.entrySet()) {
                if (subCommand.getKey().equalsIgnoreCase(args[0])) {
                    subCommand.getValue().run(p, ArrayUtils.remove(args, 0));
                    return;
                }
            }
        }

        try {
            handler.accept(p, new Parameters(args));
        } catch (Exception e) {
            System.out.println("dkioawjdiowajdiowadj");
        }
    }

    public List<String> getAutoCompletions(@NotNull Player p, String[] args) {
        if (args.length >= 1) {
            for (Map.Entry<String, Command> subCommand : subCommands.entrySet()) {
                if (subCommand.getKey().equalsIgnoreCase(args[0])) {
                    return subCommand.getValue().getAutoCompletions(p, ArrayUtils.remove(args, 0));
                }
            }
        }

        int i = args.length - 1;
        List<String> autoCompletions = autoCompleteHandlers.containsKey(i) ? autoCompleteHandlers.get(i).apply(p) : new ArrayList<>();
        if (i == 0) autoCompletions.addAll(subCommands.keySet());
        return autoCompletions;
    }

    public void register(CommandManager commandManager) {
        commandManager.addCommand(this);
    }
}