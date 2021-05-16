package com.tobiplayer3.limitedplaytime.commands;

public class StringParameter extends Parameter<String> {
    public StringParameter(String name, String permission) {
        super(name, permission);
    }

    public StringParameter(String name) {
        super(name);
    }

    @Override
    protected String parseInput(String input) {
        return input;
    }
}
