package com.tobiplayer3.limitedplaytime.commands;

import org.bukkit.util.Consumer;

public abstract class Parameter<T> extends Command {
    private T value;

    public Parameter(String name, String permission) {
        super(name, permission);
    }
    public Parameter(String name) {
        super(name);
    }

    public T getValue() {
        return value;
    }

    protected abstract T parseInput(String input);

    public Parameter<T> setHandler(Consumer<Command> handler) {
        return this;
    }
}
