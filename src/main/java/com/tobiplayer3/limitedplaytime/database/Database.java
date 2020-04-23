package com.tobiplayer3.limitedplaytime.database;

import com.tobiplayer3.limitedplaytime.PlaytimePlayer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface Database {

    Connection getConnection() throws SQLException;
    void savePlayer(PlaytimePlayer player);
    void savePlayers(List<PlaytimePlayer> players);
    PlaytimePlayer loadPlayer(UUID uuid);
    List<PlaytimePlayer> loadPlayers(List<UUID> uuids);
    void shutdown();
}
