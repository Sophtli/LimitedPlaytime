package com.tobiplayer3.limitedplaytime.database;

import com.tobiplayer3.limitedplaytime.LimitedPlaytime;
import com.tobiplayer3.limitedplaytime.PlaytimePlayer;

import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SQLite implements Database {

    private Connection c;

    private LimitedPlaytime limitedPlaytime = LimitedPlaytime.getInstance();

    private String file;

    public SQLite() {
        file = limitedPlaytime.getDataFolder() + File.separator + "limitedplaytime.db";

        try {
            Connection connection = getConnection();
            try (PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS players (" +
                    "uuid TEXT NOT NULL," +
                    "playtime INTEGER NOT NULL," +
                    "last_played TEXT NOT NULL," +
                    "PRIMARY KEY(uuid)" +
                    ");")) {

                statement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        synchronized (this) {
            if (c == null || c.isClosed()) {
                c = DriverManager.getConnection("jdbc:sqlite:" + file);
            }

            return c;
        }
    }

    @Override
    public void savePlayer(PlaytimePlayer player) {
        try {
            Connection connection = getConnection();

            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO players (uuid,playtime,last_played) VALUES (?,?,?) ON CONFLICT(uuid) DO UPDATE SET playtime=?,last_played=?;")) {
                statement.setObject(1, player.getUUID());
                statement.setInt(2, player.getPlaytime());
                statement.setString(3, player.getLastLogin().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                statement.setInt(4, player.getPlaytime());
                statement.setString(5, player.getLastLogin().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void savePlayers(List<PlaytimePlayer> players) {
        try {
            if (players.size() <= 0) {
                return;
            }

            Connection connection = getConnection();

            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO players (uuid,playtime,last_played) VALUES (?,?,?) ON CONFLICT(uuid) DO UPDATE SET playtime=?,last_played=?;")) {

                for (PlaytimePlayer player : players) {
                    statement.setObject(1, player.getUUID());
                    statement.setInt(2, player.getPlaytime());
                    statement.setString(3, player.getLastLogin().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                    statement.setInt(4, player.getPlaytime());
                    statement.setString(5, player.getLastLogin().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                    statement.executeUpdate();
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PlaytimePlayer loadPlayer(UUID uuid) {
        try {
            Connection connection = getConnection();

            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM players WHERE uuid=?;")) {
                statement.setObject(1, uuid);

                try (ResultSet result = statement.executeQuery()) {
                    PlaytimePlayer playtimePlayer = null;
                    if (result.next()) {
                        Integer playtime = result.getInt("playtime");
                        String lastPlayed = result.getString("last_played");
                        playtimePlayer = new PlaytimePlayer(uuid, playtime, LocalDate.parse(lastPlayed, DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                    }
                    return playtimePlayer;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PlaytimePlayer> loadPlayers(List<UUID> uuids) {
        List<PlaytimePlayer> playtimePlayers = new ArrayList<>();
        try {
            Connection connection = getConnection();

            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM players WHERE uuid=?;")) {
                for(UUID uuid : uuids) {
                    statement.setObject(1, uuid);

                    try (ResultSet result = statement.executeQuery()) {
                        if (result.next()) {
                            Integer playtime = result.getInt("playtime");
                            String lastPlayed = result.getString("last_played");
                            PlaytimePlayer playtimePlayer = new PlaytimePlayer(uuid, playtime, LocalDate.parse(lastPlayed, DateTimeFormatter.ofPattern("dd.MM.yyyy")));

                            playtimePlayers.add(playtimePlayer);
                        }
                    }
                }
                return playtimePlayers;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return playtimePlayers;
        }
    }

    @Override
    public void shutdown() {
        if (c != null) {
            try {
                c.close();
            } catch (SQLException e) {
                // ignore
            }
        }
    }
}
