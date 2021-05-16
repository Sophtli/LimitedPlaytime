package com.tobiplayer3.limitedplaytime.commands;

public class IntParameter extends Parameter<Integer> {
    public IntParameter(String name, String permission) {
        super(name, permission);
    }

    public IntParameter(String name) {
        super(name);
    }

    @Override
    protected Integer parseInput(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
