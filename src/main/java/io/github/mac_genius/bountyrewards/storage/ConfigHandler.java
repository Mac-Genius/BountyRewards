package io.github.mac_genius.bountyrewards.storage;

import io.github.mac_genius.bountyrewards.PluginSettings;
import io.github.mac_genius.bountyrewards.storage.MySQL.MySQLConfig;
import org.bukkit.ChatColor;

/**
 * Created by Mac on 1/18/2016.
 */
public class ConfigHandler {
    private PluginSettings settings;

    public ConfigHandler(PluginSettings settings) {
        this.settings = settings;
    }

    public String getMessage(String name) {
        if (settings.isUsingMySQL()) {
            return ChatColor.translateAlternateColorCodes('&', settings.getCache().getMessage(name));
        } else {
            return ChatColor.translateAlternateColorCodes('&', settings.getPlugin().getConfig().getString(name));
        }
    }

    public String getLocalMessage(String name) {
        return settings.getPlugin().getConfig().getString(name);
    }

    public ConfigSettings getSettings() {
        if (settings.isUsingMySQL()) {
            return settings.getCache().getSettings();
        } else {
            ConfigSettings configSettings = new ConfigSettings();
            configSettings.setAnon(settings.getPlugin().getConfig().getBoolean("Anonymous"));
            configSettings.setBungeecord(settings.getPlugin().getConfig().getBoolean("BungeeCord"));
            configSettings.setCancelBounty(settings.getPlugin().getConfig().getBoolean("CancelBounty"));
            configSettings.setCooldown(settings.getPlugin().getConfig().getInt("Cooldown"));
            configSettings.setDeathCost(settings.getPlugin().getConfig().getInt("DeathCost"));
            configSettings.setDeathCostMode(settings.getPlugin().getConfig().getString("DeathCostMode"));
            configSettings.setExpiration(settings.getPlugin().getConfig().getInt("Expiration"));
            configSettings.setMaxSet(settings.getPlugin().getConfig().getInt("Max"));
            configSettings.setMinSet(settings.getPlugin().getConfig().getInt("Min"));
            configSettings.setRanDeathCost(settings.getPlugin().getConfig().getBoolean("RandomizeDeathCost"));
            configSettings.setRanRange(settings.getPlugin().getConfig().getInt("Range"));
            configSettings.setRefund(settings.getPlugin().getConfig().getInt("Refund"));
            configSettings.setTransFee(settings.getPlugin().getConfig().getInt("TransactionFee"));
            return configSettings;
        }
    }

    public ConfigSettings getLocalSettings() {
        ConfigSettings configSettings = new ConfigSettings();
        configSettings.setAnon(settings.getPlugin().getConfig().getBoolean("Anonymous"));
        configSettings.setBungeecord(settings.getPlugin().getConfig().getBoolean("BungeeCord"));
        configSettings.setCancelBounty(settings.getPlugin().getConfig().getBoolean("CancelBounty"));
        configSettings.setCooldown(settings.getPlugin().getConfig().getInt("Cooldown"));
        configSettings.setDeathCost(settings.getPlugin().getConfig().getInt("DeathCost"));
        configSettings.setDeathCostMode(settings.getPlugin().getConfig().getString("DeathCostMode"));
        configSettings.setExpiration(settings.getPlugin().getConfig().getInt("Expiration"));
        configSettings.setMaxSet(settings.getPlugin().getConfig().getInt("Max"));
        configSettings.setMinSet(settings.getPlugin().getConfig().getInt("Min"));
        configSettings.setRanDeathCost(settings.getPlugin().getConfig().getBoolean("RandomizeDeathCost"));
        configSettings.setRanRange(settings.getPlugin().getConfig().getInt("Range"));
        configSettings.setRefund(settings.getPlugin().getConfig().getInt("Refund"));
        configSettings.setTransFee(settings.getPlugin().getConfig().getInt("TransactionFee"));
        return configSettings;
    }
}
