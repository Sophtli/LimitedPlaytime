package li.sopht.limitedplaytime;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class LimitedPlaytimeAPI {

    private final LimitedPlaytime limitedPlaytime;
    private final PlaytimeManager playtimeManager;

    public LimitedPlaytimeAPI(LimitedPlaytime limitedPlaytime) {
        this.limitedPlaytime = limitedPlaytime;
        this.playtimeManager = limitedPlaytime.getPlaytimeManager();
    }

    @NotNull
    public CompletableFuture<Playtime> getPlaytime(UUID uuid) {
        return playtimeManager.getPlaytime(uuid);
    }

    @NotNull
    public CompletableFuture<Void> setPlaytime(UUID uuid, int timeRemaining) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        getPlaytime(uuid)
                .thenAccept(
                        playtime -> {
                            playtime.setTimeRemaining(timeRemaining);
                            completableFuture.complete(null);
                        }
                )
                .exceptionally(e -> {
                    completableFuture.completeExceptionally(e);
                    return null;
                });

        return completableFuture;
    }
}
