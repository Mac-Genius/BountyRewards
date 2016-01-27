package io.github.mac_genius.bountyrewards;

import io.github.mac_genius.bountyrewards.Utils.UpdateChecker;
import io.github.mac_genius.bountyrewards.Utils.UpdateConfig;
import io.github.mac_genius.bountyrewards.Utils.Version;
import io.github.mac_genius.bountyrewards.bukkit.metrics.src.main.java.org.mcstats.Metrics;
import io.github.mac_genius.bountyrewards.storage.MySQL.MySQLCache;
import io.github.mac_genius.bountyrewards.storage.MySQL.MySQLConfig;
import io.github.mac_genius.bountyrewards.storage.MySQL.SQLConnect;
import io.github.mac_genius.bountyrewards.storage.local.BountyData;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.fusesource.jansi.Ansi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Mac on 1/4/2016.
 */
public class PluginSettings {
    private Plugin plugin;
    private JavaPlugin javaPlugin;
    private Economy economy;
    private BountyData data;
    private boolean usingMySQL;
    private MySQLCache cache;
    private ArrayList<String> onlineServers;
    private Version latestVersion;

    public PluginSettings(JavaPlugin javaPlugin, Economy economy) {
        this.javaPlugin = javaPlugin;
        this.plugin = javaPlugin;
        this.economy = economy;
        updateConfig();
        setupConfig();
        //setupEconomy();
        data = new BountyData(javaPlugin, "bountydata.yml");
        data.saveDefaultConfig();
        if (usingMySQL) {
            setupDatabase();
            if (plugin.isEnabled()) {
                setupCache();
            }
        }
        onlineServers = new ArrayList<>();
        if (plugin.getConfig().getBoolean("UpdateChecking")) {
            getVersion();
        }
        enableMetrics();
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public void setupConfig() {
        plugin.saveDefaultConfig();
        usingMySQL = plugin.getConfig().getBoolean("MySQL");
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            plugin.getLogger().info("check");
            economy = economyProvider.getProvider();
        }
        return (economy != null);
    }

    public Economy getEconomy() {
        return economy;
    }

    public ArrayList<String> getMessageNames() {
        ArrayList<String> names = new ArrayList<>();
        names.add("SetPrefix");
        names.add("SetBountyMessage");
        names.add("SetAmountMessage");
        names.add("SetCooldownMessage");
        names.add("SetOfflineMessage");
        names.add("SetAlreadyExists");
        names.add("AnonymousMessage");
        names.add("PlaceOnYourself");
        names.add("BountyBroadcast");
        names.add("AnonymousBroadcast");
        names.add("NoMoney");
        names.add("SetMinMessage");
        names.add("SetOverMax");
        names.add("IncreasePrefix");
        names.add("IncreaseBountyMessage");
        names.add("IncreaseAmountMessage");
        names.add("IncreaseNoBounty");
        names.add("IncreaseNoMoney");
        names.add("IncreaseOverMax");
        names.add("BountyIncrease");
        names.add("RemovePrefix");
        names.add("RemoveBountyMessage");
        names.add("RemoveDisabled");
        names.add("BountyCancel");
        names.add("RemoveAnnouncement");
        names.add("RemoveNoBounties");
        names.add("BountyComplete");
        names.add("BountyExpire");
        names.add("BountyHeader");
        names.add("BountyList");
        names.add("DropOnDeath");
        names.add("NoBounties");
        names.add("TimeoutMessage");
        names.add("CancelAction");
        names.add("CollectYourself");
        return names;
    }

    public boolean isUsingMySQL() {
        return usingMySQL;
    }

    public BountyData getBountyData() {
        return data;
    }

    public MySQLCache getCache() {
        return cache;
    }

    public void setupDatabase() {
        SQLConnect connect = new SQLConnect(this);
        if (connect.testConnection()) {
            plugin.getLogger().info(Ansi.ansi().fg(Ansi.Color.GREEN) + "Connected to the database." + Ansi.ansi().fg(Ansi.Color.WHITE));
            connect.firstTimeSetup();
            connect.updateDatabase();
        } else {
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
    }

    public void setupCache() {
        MySQLCache cache = new MySQLCache();
        MySQLConfig mySQLConfig = new MySQLConfig(this);
        cache.setSettings(mySQLConfig.getSettings());
        cache.setMessages(mySQLConfig.getAllMessages());
        cache.setBounties(new HashMap<>());
        cache.setCooldowns(new HashMap<>());
        this.cache = cache;
    }

    public ArrayList<String> getOnlineServers() {
        return onlineServers;
    }

    private void getVersion() {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.getLogger().info("Checking for updates...");
            latestVersion = new UpdateChecker().getVersion();
            if (!latestVersion.getVersion().equals("")) {
                if (plugin.getDescription().getVersion().equals(latestVersion.getVersion())) {
                    plugin.getLogger().info("BountyRewards is up to date.");
                    latestVersion.setNeedsUpdate(false);
                } else {
                    latestVersion.setNeedsUpdate(true);
                    plugin.getLogger().info("There is a new version of BountyRewards available! (v." + latestVersion.getVersion() + ")");
                    if (!latestVersion.getDownloadLink().equals("")) {
                        plugin.getLogger().info("You can download the latest version here: " + latestVersion.getDownloadLink() + " or you can download it from the Spigot page.");
                    }
                }
            }
        });
    }

    public Version getLatestVersion() {
        return latestVersion;
    }

    public void updateLatestVersion(Version version) {
        this.latestVersion = version;
    }

    private void enableMetrics() {
        try {
            Metrics metrics = new Metrics(plugin);
            metrics.start();
        } catch (IOException e) {
            // Failed to submit the stats :-(
        }
    }

    private void updateConfig() {
        new UpdateConfig(this).updateConfig();
    }


}
