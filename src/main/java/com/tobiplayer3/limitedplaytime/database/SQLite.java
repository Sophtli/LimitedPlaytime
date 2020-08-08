package com.tobiplayer3.limitedplaytime.database;

import com.tobiplayer3.limitedplaytime.LimitedPlaytime;
import com.tobiplayer3.limitedplaytime.Playtime;
import com.tobiplayer3.limitedplaytime.exceptions.PlaytimeNotSavedException;

import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class SQLite implements Database {

    private Connection c;
    private final LimitedPlaytime limitedPlaytime = LimitedPlaytime.getInstance();
    private final String file;

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
            limitedPlaytime.getLogger().log(Level.SEVERE, e.getMessage(), e);
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
    public CompletableFuture<Void> savePlayer(UUID uuid, Playtime playtime) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO players (uuid,playtime,last_played) VALUES (?,?,?) ON CONFLICT(uuid) DO UPDATE SET playtime=?,last_played=?;")) {
                statement.setObject(1, uuid);
                statement.setInt(2, playtime.getTimeRemaining());
                statement.setString(3, playtime.getLastLogin().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                statement.setInt(4, playtime.getTimeRemaining());
                statement.setString(5, playtime.getLastLogin().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                statement.executeUpdate();
                completableFuture.complete(null);
            }
        } catch (SQLException e) {
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
                statement.setObject(1, uuid);

                try (ResultSet result = statement.executeQuery()) {
                    if (result.next()) {
                        Integer playtime = result.getInt("playtime");
                        String lastPlayed = result.getString("last_played");
                        completableFuture.complete(new Playtime(uuid, playtime, LocalDate.parse(lastPlayed, DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
                    }
                    completableFuture.completeExceptionally(new PlaytimeNotSavedException("The player does not exist in the database"));
                }
            }
        } catch (SQLException e) {
            limitedPlaytime.getLogger().log(Level.SEVERE, e.getMessage(), e);
            completableFuture.completeExceptionally(e);
        }

        return completableFuture;
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
