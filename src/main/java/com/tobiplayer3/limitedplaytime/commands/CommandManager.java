package com.tobiplayer3.limitedplaytime.commands;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    private final List<Command> commands = new ArrayList<>();

    public void addCommand(Command command) {
        commands.add(command);
    }

    public List<Command> getCommands() {
        return commands;
    }
}
