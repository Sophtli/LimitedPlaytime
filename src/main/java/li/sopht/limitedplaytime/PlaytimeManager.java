package li.sopht.limitedplaytime;

import com.google.common.collect.ImmutableMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class PlaytimeManager {
    @NotNull
    private final LimitedPlaytime limitedPlaytime;

    private boolean playtimeStacking;

    @NotNull
    private final Map<UUID, Playtime> playtimes = new ConcurrentHashMap<>();

    private int defaultMaxPlaytime;
    private ImmutableMap<String, Integer> maxPlaytimes;
    private List<Integer> notifySteps;

    public PlaytimeManager(@NotNull LimitedPlaytime limitedPlaytime) {
        this.limitedPlaytime = limitedPlaytime;
    }

    public boolean isPlaytimeCached(@NotNull UUID uuid) {
        return playtimes.containsKey(uuid);
    }

    public Map<UUID, Playtime> getPlaytimes() {
        return playtimes;
    }

    /**
     * Get the playtime of a player,
     * if the player is online getCachedPlaytime is used,
     * otherwise this will make a database call
     *
     * @param uuid A player
     * @return CompletableFutute
     */
    @NotNull
    public CompletableFuture<Playtime> getPlaytime(@NotNull UUID uuid) {
        CompletableFuture<Playtime> completableFuture = new CompletableFuture<>();

        if (isPlaytimeCached(uuid)) {
            completableFuture.complete(getCachedPlaytime(uuid));
            return completableFuture;
        }

        limitedPlaytime.getDB().loadPlayer(uuid)
                .thenAccept(completableFuture::complete)
                .exceptionally(e -> {
                    completableFuture.completeExceptionally(e);
                    return null;
                });

        return completableFuture;
    }

    /**
     * Get the playtime of a player stored in the cache
     *
     * @param uuid A player (who should be online)
     * @return The playtime of the player,
     * null if the player is not cached (is not online right now)
     */
    @Nullable
    public Playtime getCachedPlaytime(UUID uuid) {
        return playtimes.get(uuid);
    }

    /**
     * Loads the a player into the cache
     *
     * @param uuid Player to load
     * @return CompletableFuture containing the loaded playtime
     */
    @NotNull
    public CompletableFuture<Playtime> loadPlayer(UUID uuid) {
        CompletableFuture<Playtime> completableFuture = new CompletableFuture<>();

        limitedPlaytime.getDB().loadPlayer(uuid)
                .thenAcceptAsync(playtime -> {
                    playtimes.put(uuid, playtime);
                    completableFuture.complete(playtime);
                })
                .exceptionally(e -> {
                    completableFuture.completeExceptionally(e);
                    return null;
                });

        return completableFuture;
    }

    @NotNull
    public Playtime createPlayer(@NotNull UUID uuid) {
        Playtime playtime = new Playtime(uuid, getMaxPlaytime(uuid), LocalDate.now());
        playtimes.put(uuid, playtime);
        return playtime;
    }

    @NotNull
    public Playtime createPlayer(@NotNull UUID uuid, int maxPlaytime) {
        Playtime playtime = new Playtime(uuid, maxPlaytime, LocalDate.now());
        playtimes.put(uuid, playtime);
        return playtime;
    }

    @NotNull
    public CompletableFuture<Void> unloadPlayer(@NotNull UUID uuid) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        Playtime playtime = playtimes.get(uuid);
        playtimes.remove(uuid);

        if (playtime == null) {
            completableFuture.completeExceptionally(new Exception(""));
            return completableFuture;
        }

        limitedPlaytime.getDB().savePlayer(playtime)
                .thenRunAsync(() -> completableFuture.complete(null))
                .exceptionally(exception -> {
                    completableFuture.completeExceptionally(exception);
                    return null;
                });

        return completableFuture;
    }

    @NotNull
    public CompletableFuture<Void> unloadPlayers() {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        limitedPlaytime.getDB().savePlayersSync(new ArrayList<>(playtimes.values()))
                .thenRunAsync(() -> completableFuture.complete(null))
                .exceptionally(exception -> {
                    completableFuture.completeExceptionally(exception);
                    return null;
                });
        playtimes.clear();

        return completableFuture;
    }

    public void setPlaytimeStacking(boolean playtimeStacking) {
        this.playtimeStacking = playtimeStacking;
    }

    public boolean isPlaytimeStacking() {
        return playtimeStacking;
    }

    @Nullable
    public Integer getMaxPlaytime(@NotNull UUID uuid) {
        int maxPlaytime = -1;
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return null;
        }
        for (Map.Entry<String, Integer> permission : maxPlaytimes.entrySet()) {
            if (player.hasPermission(permission.getKey()) && maxPlaytime < permission.getValue()) {
                maxPlaytime = permission.getValue();
            }
        }

        if (maxPlaytime == -1) {
            maxPlaytime = defaultMaxPlaytime;
        }
        return maxPlaytime;
    }

    public void setMaxPlaytimes(Map<String, Integer> maxPlaytimes) {
        this.maxPlaytimes = ImmutableMap.copyOf(maxPlaytimes);
    }

    public void setDefaultMaxPlaytime(Integer defaultMaxPlaytime) {
        this.defaultMaxPlaytime = defaultMaxPlaytime;
    }

    public void setNotifySteps(List<Integer> notifySteps) {
        this.notifySteps = Collections.unmodifiableList(notifySteps);
    }

    public boolean isNotifyStep(Integer tick) {
        return notifySteps.contains(tick);
    }
}
