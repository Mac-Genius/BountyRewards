package io.github.mac_genius.bountyrewards;

import io.github.mac_genius.bountyrewards.CommandInfo.Command;
import io.github.mac_genius.bountyrewards.Utils.AnnouncementListener;
import io.github.mac_genius.bountyrewards.Utils.ExpirationChecker;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This is the main class for the plugin. It will
 * allow people to place bounties on others and
 * claim the bounties after killing a player.
 *
 * @author John Harrison
 */
public class BountyRewards extends JavaPlugin {
    private Economy econ = null;
    public static PluginSettings settings;
    /**
     * This is activated when the plugin
     */
    @Override
    public void onEnable() {
        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        settings = new PluginSettings(this, econ);
        if (settings.getPlugin().isEnabled()) {
            getServer().getPluginManager().registerEvents(new Listeners(settings), settings.getPlugin());
            this.getCommand("bounty").setExecutor(new Command(settings));
            settings.getPlugin().getServer().getScheduler().runTaskTimer(settings.getPlugin(), new ExpirationChecker(settings), 0, 600);
            this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
            this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new AnnouncementListener());
            getLogger().info("Plugin enabled");
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled");
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
}
