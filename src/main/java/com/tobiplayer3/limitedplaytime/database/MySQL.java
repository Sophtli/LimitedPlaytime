package com.tobiplayer3.limitedplaytime.database;

import com.tobiplayer3.limitedplaytime.PlaytimePlayer;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
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
        try {
            Connection connection = getConnection();

            PreparedStatement statement = connection.prepareStatement("INSERT INTO `players` (`uuid`,`playtime`,`last_played`) VALUES (?,?,?) ON DUPLICATE KEY UPDATE `playtime`=?,`last_played`=?;");
            statement.setString(1, player.getUUID().toString());
            statement.setInt(2, player.getPlaytime());
            statement.setDate(3, Date.valueOf(player.getLastLogin()));
            statement.setInt(4, player.getPlaytime());
            statement.setDate(5, Date.valueOf(player.getLastLogin()));
            statement.executeUpdate();

            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void savePlayers(List<PlaytimePlayer> players) {

    }

    @Override
    public PlaytimePlayer loadPlayer(UUID uuid) {
        try {
            Connection connection = getConnection();

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM players WHERE uuid=?;");
            statement.setObject(1, uuid);
            ResultSet result = statement.executeQuery();

            PlaytimePlayer playtimePlayer = null;
            if (result.next()) {
                Integer playtime = result.getInt("playtime");
                Date lastPlayed = result.getDate("last_played");
                playtimePlayer = new PlaytimePlayer(uuid, playtime, lastPlayed.toLocalDate());
            }

            result.close();
            statement.close();
            connection.close();

            return playtimePlayer;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PlaytimePlayer> loadPlayers(List<UUID> uuids) {
        return null;
    }

    @Override
    public void shutdown() {
        if (hikari != null) {
            hikari.close();
        }
    }
}
