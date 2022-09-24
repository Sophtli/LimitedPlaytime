package li.sopht.limitedplaytime;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class Utils {

    private final LimitedPlaytime limitedPlaytime;

    public Utils(LimitedPlaytime limitedPlaytime){
        this.limitedPlaytime = limitedPlaytime;
    }

    @NotNull
    public CompletableFuture<Void> runAsync(Runnable runnable) {
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

    public void runSync(Runnable runnable) {
        new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }.runTask(limitedPlaytime);
    }

    @NotNull
    public CompletableFuture<OfflinePlayer> getOfflinePlayer(String name) {
        CompletableFuture<OfflinePlayer> completableFuture = new CompletableFuture<>();

        runAsync(() -> {
            @SuppressWarnings("deprecation")
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
            completableFuture.complete(offlinePlayer);
        });

        return completableFuture;
    }

    public void sendActionBarMessage(Player player, String message) {
        if (!StringUtils.containsIgnoreCase(Bukkit.getVersion(), "1.8") && !StringUtils.containsIgnoreCase(Bukkit.getVersion(), "1.9")) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
        } else {
            if (Bukkit.getPluginManager().isPluginEnabled("ActionBarAPI")) {
                //ActionBarAPI.sendActionBar(player, messageManager.getMessage(Message.TIME_SHORT, playtime));
            }
        }
    }

}
