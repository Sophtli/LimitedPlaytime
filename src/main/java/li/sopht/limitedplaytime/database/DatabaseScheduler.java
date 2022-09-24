package li.sopht.limitedplaytime.database;

import li.sopht.limitedplaytime.LimitedPlaytime;
import li.sopht.limitedplaytime.PlaytimeManager;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DatabaseScheduler extends BukkitRunnable {
    private final LimitedPlaytime limitedPlaytime;
    private final PlaytimeManager playtimeManager;

    public DatabaseScheduler(@NotNull LimitedPlaytime limitedPlaytime) {
        this.limitedPlaytime = limitedPlaytime;
        this.playtimeManager = limitedPlaytime.getPlaytimeManager();
    }

    @Override
    public void run() {
        limitedPlaytime.getDB().savePlayers(new ArrayList<>(playtimeManager.getPlaytimes().values()));
    }
}
