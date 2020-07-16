package com.tobiplayer3.limitedplaytime.database;

import com.tobiplayer3.limitedplaytime.LimitedPlaytime;
import com.tobiplayer3.limitedplaytime.Playtime;
import com.tobiplayer3.limitedplaytime.exceptions.PlaytimeNotSavedException;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class MySQL implements Database {

    private final HikariDataSource hikari;
    private final LimitedPlaytime limitedPlaytime = LimitedPlaytime.getInstance();

    public MySQL(String host, int port, String database, String username, String password) {
        hikari = new HikariDataSource();
        hikari.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
        hikari.setUsername(username);
        hikari.setPassword(password);

        new BukkitRunnable() {
            @Override
            public void run() {
                try (Connection connection = getConnection()) {
                    try (PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `players` (" +
                            "`uuid` CHAR(36) NOT NULL," +
                            "`playtime` INT NOT NULL," +
                            "`last_played` DATE NOT NULL," +
                            "PRIMARY KEY(`uuid`)" +
                            ");")) {

                        statement.execute();
                    }
                } catch (SQLException e) {
                    limitedPlaytime.getLogger().log(Level.SEVERE, e.getMessage(), e);
                }
            }
        }.runTaskAsynchronously(limitedPlaytime);
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
    public CompletableFuture<Void> savePlayer(UUID uuid, Playtime playtime) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO `players` (`uuid`," +
                    "`playtime`,`last_played`) VALUES (?,?,?) ON DUPLICATE KEY UPDATE `playtime`=?,`last_played`=?;")) {
                statement.setString(1, uuid.toString());
                statement.setInt(2, playtime.getTimeRemaining());
                statement.setDate(3, Date.valueOf(playtime.getLastLogin()));
                statement.setInt(4, playtime.getTimeRemaining());
                statement.setDate(5, Date.valueOf(playtime.getLastLogin()));
                statement.executeUpdate();
                completableFuture.complete(null);
            }
        } catch (Exception e) {
            limitedPlaytime.getLogger().log(Level.SEVERE, e.getMessage(), e);
            completableFuture.completeExceptionally(e);
        }

        return completableFuture;
    }

    @Override
    public CompletableFuture<Playtime> loadPlayer(UUID uuid) {
        CompletableFuture<Playtime> completableFuture = new CompletableFuture<>();

        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM players WHERE uuid=?;")) {
                statement.setString(1, uuid.toString());
                try (ResultSet result = statement.executeQuery()) {
                    if (result.next()) {
                        Integer timeRemaining = result.getInt("playtime");
                        Date lastPlayed = result.getDate("last_played");

                        Playtime playtime = new Playtime(timeRemaining, lastPlayed.toLocalDate());
                        completableFuture.complete(playtime);
                    }
                    completableFuture.completeExceptionally(new PlaytimeNotSavedException("The player does not exist in the database"));
                }
            }
        } catch (Exception e) {
            limitedPlaytime.getLogger().log(Level.SEVERE, e.getMessage(), e);
            completableFuture.completeExceptionally(e);
        }

        return completableFuture;
    }

    @Override
    public void shutdown() {
        if (hikari != null) {
            hikari.close();
        }
    }
}
