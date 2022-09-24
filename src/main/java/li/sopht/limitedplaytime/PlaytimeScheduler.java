package li.sopht.limitedplaytime;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class PlaytimeScheduler extends BukkitRunnable {

    @NotNull
    private final LimitedPlaytime limitedPlaytime;
    @NotNull
    private final PlaytimeManager playtimeManager;
    @NotNull
    private final MessageManager messageManager;
    @NotNull
    private final Utils utils;

    public PlaytimeScheduler(@NotNull LimitedPlaytime limitedPlaytime) {
        this.limitedPlaytime = limitedPlaytime;
        playtimeManager = limitedPlaytime.getPlaytimeManager();
        messageManager = limitedPlaytime.getMessageManager();
        utils = limitedPlaytime.getUtils();
    }

    public void run() {

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    Playtime playtime = playtimeManager.getCachedPlaytime(player.getUniqueId());
                    if (playtime == null) {
                        continue;
                    }

                    int newPlaytime = playtime.getTimeRemaining() - 1;
                    playtime.setTimeRemaining(newPlaytime);

                    utils.sendActionBarMessage(player, messageManager.getMessage(Message.TIME_SHORT, playtime));

                    if (playtimeManager.isNotifyStep(newPlaytime)) {
                        player.sendMessage(Integer.toString(playtime.getTimeRemaining() / 20));
                    }

                    if (newPlaytime <= 0) {
                        utils.runSync(() -> player.kickPlayer("test!"));
                    }
                }

            }
        }.runTask(limitedPlaytime);

    }

}
