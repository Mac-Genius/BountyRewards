package io.github.mac_genius.bountyrewards.Utils;

import io.github.mac_genius.bountyrewards.PluginSettings;
import io.github.mac_genius.bountyrewards.Utils.Announcement;
import io.github.mac_genius.bountyrewards.storage.Bounty;
import io.github.mac_genius.bountyrewards.storage.BountyHandler;
import io.github.mac_genius.bountyrewards.storage.ConfigHandler;
import org.bukkit.Bukkit;

import java.util.UUID;

/**
 * Created by Mac on 1/19/2016.
 */
public class ExpirationChecker implements Runnable {
    private PluginSettings settings;

    public ExpirationChecker(PluginSettings settings) {
        this.settings = settings;
    }

    @Override
    public void run() {
        ConfigHandler configHandler = new ConfigHandler(settings);
        if (configHandler.getSettings().getExpiration() > 0) {
            long expiration = configHandler.getSettings().getExpiration() * 60000;
            BountyHandler bountyHandler = new BountyHandler(settings);
            for (Bounty b : bountyHandler.getAllBounties()) {
                if (System.currentTimeMillis() - b.getWhenSet().getTime() >= expiration) {
                    bountyHandler.removeBounty(b);
                    Announcement.announce(configHandler.getMessage("BountyExpire").replace("%player%", b.getPlayerName()));
                    settings.getEconomy().depositPlayer(Bukkit.getOfflinePlayer(UUID.fromString(b.getSetterUUID())), b.getAmount());
                }
            }
        }
    }
}
