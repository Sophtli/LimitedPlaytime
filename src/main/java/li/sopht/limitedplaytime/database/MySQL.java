package li.sopht.limitedplaytime.database;

import li.sopht.limitedplaytime.LimitedPlaytime;
import li.sopht.limitedplaytime.Playtime;
import li.sopht.limitedplaytime.Utils;
import li.sopht.limitedplaytime.exceptions.PlaytimeNotSavedException;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class MySQL implements Database {

    private final HikariDataSource hikari;
    private final LimitedPlaytime limitedPlaytime;
    private final Utils utils;

    public MySQL(LimitedPlaytime limitedPlaytime, String host, int port, String database, String username, String password) {
        this.limitedPlaytime = limitedPlaytime;
        utils = limitedPlaytime.getUtils();

        hikari = new HikariDataSource();
        hikari.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
        hikari.setUsername(username);
        hikari.setPassword(password);

        utils.runAsync(() -> {
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
        });
    }

    @NotNull
    @Override
    public Connection getConnection() throws SQLException {
        return hikari.getConnection();
    }

    @NotNull
    @Override
    public CompletableFuture<Void> savePlayer(@NotNull Playtime playtime) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        utils.runAsync(() -> {
            try (Connection connection = getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement("INSERT INTO `players` (`uuid`," +
                        "`playtime`,`last_played`) VALUES (?,?,?) ON DUPLICATE KEY UPDATE `playtime`=?,`last_played`=?;")) {
                    statement.setString(1, playtime.getUUID().toString());
                    statement.setInt(2, playtime.getTimeRemaining());
                    statement.setDate(3, Date.valueOf(playtime.getLastLogin()));
                    statement.setInt(4, playtime.getTimeRemaining());
                    statement.setDate(5, Date.valueOf(playtime.getLastLogin()));
                    statement.executeUpdate();
                    completableFuture.complete(null);
                }
                Bukkit.getLogger().fine("saved playtime");
            } catch (Exception e) {
                limitedPlaytime.getLogger().log(Level.SEVERE, e.getMessage(), e);
                completableFuture.completeExceptionally(e);
            }
        });

        return completableFuture;
    }

    @NotNull
    @Override
    public CompletableFuture<Void> savePlayers(@NotNull Collection<Playtime> playtimes) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        utils.runAsync(() -> {
            try (Connection connection = getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement("INSERT INTO `players` (`uuid`," +
                        "`playtime`,`last_played`) VALUES (?,?,?) ON DUPLICATE KEY UPDATE `playtime`=?,`last_played`=?;")) {
                    for (Playtime playtime : playtimes) {
                        statement.setString(1, playtime.getUUID().toString());
                        statement.setInt(2, playtime.getTimeRemaining());
                        statement.setDate(3, Date.valueOf(playtime.getLastLogin()));
                        statement.setInt(4, playtime.getTimeRemaining());
                        statement.setDate(5, Date.valueOf(playtime.getLastLogin()));
                        statement.addBatch();
                    }
                    statement.executeBatch();
                    completableFuture.complete(null);
                }
            } catch (Exception e) {
                limitedPlaytime.getLogger().log(Level.SEVERE, e.getMessage(), e);
                completableFuture.completeExceptionally(e);
            }
        });

        return completableFuture;
    }

    @NotNull
    @Override
    public CompletableFuture<Void> savePlayersSync(@NotNull Collection<Playtime> playtimes) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO `players` (`uuid`," +
                    "`playtime`,`last_played`) VALUES (?,?,?) ON DUPLICATE KEY UPDATE `playtime`=?,`last_played`=?;")) {
                for (Playtime playtime : playtimes) {
                    statement.setString(1, playtime.getUUID().toString());
                    statement.setInt(2, playtime.getTimeRemaining());
                    statement.setDate(3, Date.valueOf(playtime.getLastLogin()));
                    statement.setInt(4, playtime.getTimeRemaining());
                    statement.setDate(5, Date.valueOf(playtime.getLastLogin()));
                    statement.addBatch();
                }
                statement.executeBatch();
                completableFuture.complete(null);
            }
        } catch (Exception e) {
            limitedPlaytime.getLogger().log(Level.SEVERE, e.getMessage(), e);
            completableFuture.completeExceptionally(e);
        }

        return completableFuture;
    }

    @NotNull
    @Override
    public CompletableFuture<Playtime> loadPlayer(@NotNull UUID uuid) {
        CompletableFuture<Playtime> completableFuture = new CompletableFuture<>();

        utils.runAsync(() -> {
            try (Connection connection = getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM players WHERE uuid=?;")) {
                    statement.setString(1, uuid.toString());
                    try (ResultSet result = statement.executeQuery()) {
                        if (result.next()) {
                            int timeRemaining = result.getInt("playtime");
                            Date lastPlayed = result.getDate("last_played");

                            Playtime playtime = new Playtime(uuid, timeRemaining, lastPlayed.toLocalDate());
                            completableFuture.complete(playtime);
                        }
                        completableFuture.completeExceptionally(new PlaytimeNotSavedException("The player does not exist in the database"));
                    }
                }
            } catch (Exception e) {
                limitedPlaytime.getLogger().log(Level.SEVERE, e.getMessage(), e);
                completableFuture.completeExceptionally(e);
            }
        });

        return completableFuture;
    }

    @Override
    public void shutdown() {
        if (hikari != null) {
            hikari.close();
        }
    }
}
