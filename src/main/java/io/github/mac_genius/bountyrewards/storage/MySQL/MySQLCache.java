package io.github.mac_genius.bountyrewards.storage.MySQL;

import io.github.mac_genius.bountyrewards.storage.Bounty;
import io.github.mac_genius.bountyrewards.storage.ConfigSettings;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * Created by Mac on 1/21/2016.
 */
public class MySQLCache {
    private HashMap<String, String> messages;
    private HashMap<Player, Bounty> bounties;
    private HashMap<Player, Cooldown> cooldowns;
    private ConfigSettings settings;

    public MySQLCache(HashMap<String, String> messages, HashMap<Player, Bounty> bounties, HashMap<Player, Cooldown> cooldowns, ConfigSettings settings) {
        this.messages = messages;
        this.bounties = bounties;
        this.cooldowns = cooldowns;
        this.settings = settings;
    }

    public MySQLCache() {}

    public HashMap<String, String> getMessages() {
        return messages;
    }

    public String getMessage(String name) { return messages.get(name); }

    public void setMessages(HashMap<String, String> messages) {
        this.messages = messages;
    }

    public HashMap<Player, Bounty> getBounties() {
        return bounties;
    }

    public void setBounties(HashMap<Player, Bounty> bounties) {
        this.bounties = bounties;
    }

    public ConfigSettings getSettings() {
        return settings;
    }

    public void setSettings(ConfigSettings settings) {
        this.settings = settings;
    }

    public HashMap<Player, Cooldown> getCooldowns() {
        return cooldowns;
    }

    public void setCooldowns(HashMap<Player, Cooldown> cooldowns) {
        this.cooldowns = cooldowns;
    }
}
