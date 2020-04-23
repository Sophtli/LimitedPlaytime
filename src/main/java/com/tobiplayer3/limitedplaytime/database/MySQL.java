package com.tobiplayer3.limitedplaytime.database;

import com.tobiplayer3.limitedplaytime.PlaytimePlayer;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MySQL implements Database {

    private final HikariDataSource hikari;

    public MySQL(String host, int port, String database, String username, String password) {
        hikari = new HikariDataSource();
        hikari.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
        hikari.setUsername(username);
        hikari.setPassword(password);

        try {
            Connection connection = getConnection();
            try (PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `players` (" +
                    "`uuid` CHAR(36) NOT NULL," +
                    "`playtime` INT NOT NULL," +
                    "`last_played` DATE NOT NULL," +
                    "PRIMARY KEY(`uuid`)" +
                    ");")) {

                statement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (hikari == null) {
            return null;
        }
        Connection connection = hikari.getConnection();
        if (connection == null || connection.isClosed()) {
            return null;
        }
        return connection;
    }

    @Override
    public void savePlayer(PlaytimePlayer player) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO `players` (`uuid`," +
                    "`playtime`,`last_played`) VALUES (?,?,?) ON DUPLICATE KEY UPDATE `playtime`=?,`last_played`=?;")) {
                statement.setString(1, player.getUUID().toString());
                statement.setInt(2, player.getPlaytime());
                statement.setDate(3, Date.valueOf(player.getLastLogin()));
                statement.setInt(4, player.getPlaytime());
                statement.setDate(5, Date.valueOf(player.getLastLogin()));
                statement.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void savePlayers(List<PlaytimePlayer> players) {
        try (Connection connection = getConnection()) {
            if (players.size() <= 0) {
                return;
            }

            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO players (uuid,playtime,last_played) VALUES (?,?,?) ON CONFLICT(uuid) DO UPDATE SET playtime=?,last_played=?;")) {

                for (PlaytimePlayer player : players) {
                    statement.setObject(1, player.getUUID());
                    statement.setInt(2, player.getPlaytime());
                    statement.setDate(3, Date.valueOf(player.getLastLogin()));
                    statement.setInt(4, player.getPlaytime());
                    statement.setDate(5, Date.valueOf(player.getLastLogin()));
                    statement.executeUpdate();
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PlaytimePlayer loadPlayer(UUID uuid) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM players WHERE uuid=?;")) {
                statement.setObject(1, uuid);
                try (ResultSet result = statement.executeQuery()) {

                    PlaytimePlayer playtimePlayer = null;
                    if (result.next()) {
                        Integer playtime = result.getInt("playtime");
                        Date lastPlayed = result.getDate("last_played");
                        playtimePlayer = new PlaytimePlayer(uuid, playtime, lastPlayed.toLocalDate());
                    }
                    return playtimePlayer;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PlaytimePlayer> loadPlayers(List<UUID> uuids) {
        List<PlaytimePlayer> playtimePlayers = new ArrayList<>();
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM players WHERE uuid=?;")) {
                for (UUID uuid : uuids) {
                    statement.setObject(1, uuid);

                    try (ResultSet result = statement.executeQuery()) {
                        if (result.next()) {
                            Integer playtime = result.getInt("playtime");
                            Date lastPlayed = result.getDate("last_played");
                            PlaytimePlayer playtimePlayer = new PlaytimePlayer(uuid, playtime, lastPlayed.toLocalDate());

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
        if (hikari != null) {
            hikari.close();
        }
    }
}
