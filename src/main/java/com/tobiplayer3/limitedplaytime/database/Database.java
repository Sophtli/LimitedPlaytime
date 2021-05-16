package com.tobiplayer3.limitedplaytime.database;

import com.tobiplayer3.limitedplaytime.Playtime;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public interface Database {
    Connection getConnection() throws SQLException;

    CompletableFuture<Void> savePlayer(Playtime playtime);

    CompletableFuture<Void> savePlayers(Collection<Playtime> playtimes);

    CompletableFuture<Playtime> loadPlayer(UUID uuid);

    void shutdown();
}
