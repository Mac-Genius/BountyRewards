package io.github.mac_genius.bountyrewards.Utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by Mac on 1/18/2016.
 */
public class PlayerUtils {
    public static String getUUID(String name) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getName().equalsIgnoreCase(name)) {
                return p.getUniqueId().toString();
            }
        }
        return null;
    }
}
