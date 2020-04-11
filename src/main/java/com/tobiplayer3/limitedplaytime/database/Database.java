package com.tobiplayer3.limitedplaytime.database;

import com.tobiplayer3.limitedplaytime.PlaytimePlayer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

public interface Database {

    Connection getConnection() throws SQLException, ClassNotFoundException;
    void savePlayer(PlaytimePlayer player);
    PlaytimePlayer loadPlayer(UUID uuid);

}
