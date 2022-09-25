package li.sopht.limitedplaytime;

import li.sopht.limitedplaytime.database.MySQL;
import li.sopht.limitedplaytime.database.SQLite;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaytimeConfig {

    private Integer autoSaveInterval;
    private String language;
    private String viewPlaytimePermission;
    private String viewOthersPlaytimePermission;
    private String editPlaytimePermission;

    public Integer getAutoSaveInterval() {
        return autoSaveInterval;
    }

    public void setAutoSaveInterval(Integer autoSaveInterval) {
        this.autoSaveInterval = autoSaveInterval;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getEditPlaytimePermission() {
        return editPlaytimePermission;
    }

    public String getViewOthersPlaytimePermission() {
        return viewOthersPlaytimePermission;
    }

    public String getViewPlaytimePermission() {
        return viewPlaytimePermission;
    }

    public void setEditPlaytimePermission(String editPlaytimePermission) {
        this.editPlaytimePermission = editPlaytimePermission;
    }

    /**
     * Sets the permission that is required to view the remaining playtime of other players.
     *
     * @param viewOthersPlaytimePermission the string representing the required permission
     */
    public void setViewOthersPlaytimePermission(String viewOthersPlaytimePermission) {
        this.viewOthersPlaytimePermission = viewOthersPlaytimePermission;
    }

    /**
     * Sets the permission that is required to view your remaining playtime.
     *
     * @param viewPlaytimePermission the string representing the required permission
     */
    public void setViewPlaytimePermission(String viewPlaytimePermission) {
        this.viewPlaytimePermission = viewPlaytimePermission;
    }
}
