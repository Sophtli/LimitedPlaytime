package com.tobiplayer3.limitedplaytime;

public enum Message {

    PREFIX("[LimitedPlaytime] "),
    TIME("%h% hours %m% minutes %s% seconds"),
    JOIN("You have %time% left."),
    PLAYTIME_COMMAND("You have %time% left."),
    PLAYTIME_COMMAND_ERROR(""),
    PLAYTIME_EDIT_COMMAND("");

    private String def;

    Message(String def){
        this.def = def;
    }

    public String getDefault() {
        return def;
    }
}
