package li.sopht.limitedplaytime.database;

import li.sopht.limitedplaytime.Playtime;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public interface Database {
    @NotNull
    Connection getConnection() throws SQLException;

    @NotNull
    CompletableFuture<Void> savePlayer(@NotNull Playtime playtime);

    @NotNull
    CompletableFuture<Void> savePlayers(@NotNull Collection<Playtime> playtimes);

    @NotNull
    CompletableFuture<Void> savePlayersSync(@NotNull Collection<Playtime> playtimes);

    @NotNull
    CompletableFuture<Playtime> loadPlayer(@NotNull UUID uuid);

    void shutdown();
}
