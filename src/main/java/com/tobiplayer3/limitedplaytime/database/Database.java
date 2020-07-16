package com.tobiplayer3.limitedplaytime.database;

import com.tobiplayer3.limitedplaytime.Playtime;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface Database {

    Connection getConnection() throws SQLException;
    CompletableFuture<Void> savePlayer(UUID uuid, Playtime playtime);
    CompletableFuture<Playtime> loadPlayer(UUID uuid);
    void shutdown();

}
