package io.github.mac_genius.bountyrewards;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.Plugin;
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
    Plugin plugin = this;
    private Economy economy;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        setupEconomy();
        getServer().getPluginManager().registerEvents(new Listeners(plugin, economy), plugin);
        this.getCommand("br").setExecutor(new Commands(plugin, economy));
        getLogger().info("Plugin enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled");
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
        return (economy != null);
    }
}
