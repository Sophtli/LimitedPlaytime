package com.tobiplayer3.limitedplaytime.commands;

import org.bukkit.util.Consumer;

import java.util.ArrayList;
import java.util.List;

public class Command {

    private final String name;
    private final List<Parameter<?>> parameters = new ArrayList<>();
    private final String permission;
    private Consumer<Command> handler;

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

    public Command addParameter(Parameter<?> parameter) {
        parameters.add(parameter);
        return this;
    }

    public List<Parameter<?>> getParameters() {
        return parameters;
    }

    public String getPermission() {
        return permission;
    }

    public Command setHandler(Consumer<Command> handler) {
        this.handler = handler;
        return this;
    }

    public boolean hasParameters() {
        return parameters.size() > 0;
    }
}
