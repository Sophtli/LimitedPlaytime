package com.tobiplayer3.limitedplaytime.database;

import com.tobiplayer3.limitedplaytime.PlaytimePlayer;

import java.sql.*;
import java.time.LocalDate;
import java.util.UUID;

public class MySQL implements Database {

    private Connection connection;

    private String host;
    private String database;
    private String username;
    private String password;
    private int port;

    public MySQL(String host, String database, String username, String password, int port){
        this.host = host;
        this.database = database;
        this.username = username;
        this.password = password;
        this.port = port;
    }

    public Connection getConnection() throws SQLException, ClassNotFoundException {
        if (connection != null && !connection.isClosed()) {
            return connection;
        }

        synchronized (this) {
            if (connection != null && !connection.isClosed()) {
                return connection;
            }
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
            return connection;
        }
    }

    public void savePlayer(PlaytimePlayer player) {
        try {
            Connection connection = getConnection();

            PreparedStatement statement = connection.prepareStatement("INSERT INTO players (uuid,playtime,last_played) VALUES (?,?,?) ON DUPLICATE KEY UPDATE playtime=?,last_played=?;");
            statement.setObject(1, player.getUUID());
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

    public PlaytimePlayer loadPlayer(UUID uuid) {
        try {
            Connection connection = getConnection();

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM players WHERE uuid=?;");
            statement.setObject(1, uuid);
            ResultSet result = statement.executeQuery();

            PlaytimePlayer playtimePlayer = null;
            if (result.next()) {
                Integer playtime = result.getInt("playtime");
                //Date lastPlayed = result.getDate("last_played");
                playtimePlayer = new PlaytimePlayer(uuid, playtime, LocalDate.now());
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

}
