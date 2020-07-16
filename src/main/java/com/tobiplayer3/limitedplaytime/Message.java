package com.tobiplayer3.limitedplaytime;

public enum Message {

    PREFIX("[LimitedPlaytime] "),
    TIME("&c%hh% hours %mm% minutes %ss% seconds"),
    TIME_SHORT("%hh%:%mm%:%ss%"),
    JOIN("You have %time% left."),
    PLAYTIME_COMMAND("You have %time% left."),
    PLAYTIME_COMMAND_ERROR(""),
    PLAYTIME_EDIT_COMMAND("");

    private final String def;

    Message(String def){
        this.def = def;
    }

    public String getDefault() {
        return def;
    }
}
