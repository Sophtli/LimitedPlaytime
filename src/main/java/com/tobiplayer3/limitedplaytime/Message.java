package com.tobiplayer3.limitedplaytime;

public enum Message {

    JOIN_MESSAGE("d");

    private String def;

    Message(String def){
        this.def = def;
    }

    public String getDefault() {
        return def;
    }
}
