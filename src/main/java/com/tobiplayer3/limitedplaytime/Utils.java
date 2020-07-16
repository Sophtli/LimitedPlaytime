package com.tobiplayer3.limitedplaytime;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.CompletableFuture;

public class Utils {

    private Utils(){}

    private static final LimitedPlaytime limitedPlaytime = LimitedPlaytime.getInstance();

    public static CompletableFuture<Void> runAsync(Runnable runnable) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
                completableFuture.complete(null);
            }
        }.runTaskAsynchronously(limitedPlaytime);

        return completableFuture;
    }

    public static void runSync(Runnable runnable) {
        new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }.runTask(limitedPlaytime);
    }

    public static CompletableFuture<OfflinePlayer> getOfflinePlayer(String name) {
        CompletableFuture<OfflinePlayer> completableFuture = new CompletableFuture<>();

        runAsync(() -> {
            @SuppressWarnings("deprecation")
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
            completableFuture.complete(offlinePlayer);
        });

        return completableFuture;
    }

}
