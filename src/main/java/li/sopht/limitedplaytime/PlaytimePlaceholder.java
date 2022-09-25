package li.sopht.limitedplaytime;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaytimePlaceholder extends PlaceholderExpansion {
    private final LimitedPlaytime limitedPlaytime;

    public PlaytimePlaceholder(LimitedPlaytime limitedPlaytime) {
        this.limitedPlaytime = limitedPlaytime;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "limitedplaytime";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Sophtli";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        Playtime playtime = limitedPlaytime.getPlaytimeManager().getPlaytime(player.getUniqueId()).join();
        return limitedPlaytime.getMessageManager().getMessage(Message.TIME_SHORT, playtime);
    }
}
