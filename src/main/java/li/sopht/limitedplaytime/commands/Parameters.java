package li.sopht.limitedplaytime.commands;

import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class Parameters {

    public ImmutableList<String> parameters;

    public Parameters(String[] parameters) {
        this.parameters = ImmutableList.copyOf(parameters);
    }

    public int getInteger(int i) {
        return Integer.parseInt(parameters.get(i));
    }

    public String getString(int i) {
        return parameters.get(i);
    }

    public Player getPlayer(int i) {
        return Bukkit.getPlayerExact(parameters.get(i));
    }
}
