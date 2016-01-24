package io.github.mac_genius.bountyrewards;

import io.github.mac_genius.bountyrewards.Utils.Announcement;
import io.github.mac_genius.bountyrewards.storage.Bounty;
import io.github.mac_genius.bountyrewards.storage.BountyHandler;
import io.github.mac_genius.bountyrewards.storage.ConfigHandler;
import io.github.mac_genius.bountyrewards.storage.ConfigSettings;
import io.github.mac_genius.bountyrewards.storage.MySQL.Cooldown;
import io.github.mac_genius.bountyrewards.storage.MySQL.MySQLBounty;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Random;

/**
 * Created by Mac on 4/25/2015.
 */
public class Listeners implements Listener {
    private PluginSettings settings;

    public Listeners(PluginSettings settings) {
        this.settings = settings;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = event.getEntity().getKiller();
        BountyHandler bountyHandler = new BountyHandler(settings);
        ConfigHandler configHandler = new ConfigHandler(settings);
        ConfigSettings configSettings = configHandler.getSettings();
        Bounty bounty = bountyHandler.getBounty(player.getUniqueId().toString());
        if (bounty.getPlayerName() != null) {
            if (bounty.getSetterUUID().equals(killer.getUniqueId().toString())) {
                killer.sendMessage(configHandler.getMessage("CollectYourself"));
                return;
            }
            event.setDeathMessage("");
            Announcement.announce(configHandler.getMessage("BountyComplete").replace("%collector%", killer.getDisplayName()).replace("%player%", bounty.getPlayerName()));
            settings.getPlugin().getServer().getScheduler().runTaskAsynchronously(settings.getPlugin(), () -> {
                bountyHandler.removeBounty(bounty);
                bountyHandler.addPlayerCooldown(player.getUniqueId().toString(), player.getName());
            });
            if (configSettings.getDeathCostMode().equalsIgnoreCase("fixed")) {
                if (configSettings.isRanDeathCost()) {
                    int random = new Random().nextInt(configSettings.getRanRange());
                    settings.getEconomy().depositPlayer(killer, bounty.getAmount() + random);
                    if (random > 0) {
                        settings.getEconomy().withdrawPlayer(player, random);
                        player.sendMessage(configHandler.getMessage("DropOnDeath").replace("%money%", random + ""));
                    }
                } else {
                    settings.getEconomy().depositPlayer(killer, bounty.getAmount() + configSettings.getDeathCost());
                    if (configSettings.getDeathCost() > 0) {
                        settings.getEconomy().withdrawPlayer(player, configSettings.getDeathCost());
                        player.sendMessage(configHandler.getMessage("DropOnDeath").replace("%money%", configSettings.getDeathCost() + ""));
                    }
                }
            } else if (configSettings.getDeathCostMode().equalsIgnoreCase("percentage")) {
                int amount = (int) ((((double) configSettings.getDeathCost()) / 100.0) * settings.getEconomy().getBalance(player));
                settings.getEconomy().depositPlayer(killer, bounty.getAmount() + amount);
                if (amount > 0) {
                    settings.getEconomy().withdrawPlayer(player, amount);
                    player.sendMessage(configHandler.getMessage("DropOnDeath").replace("%money%", amount + ""));
                }
            }
        }
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        if (settings.isUsingMySQL()) {
            MySQLBounty sqlBounty = new MySQLBounty(settings);
            Bounty bounty = sqlBounty.getBounty(event.getPlayer().getUniqueId().toString());
            if (bounty.getPlayerName() != null) {
                BountyRewards.settings.getCache().getBounties().put(event.getPlayer(), bounty);
            }
            if (sqlBounty.cooldownExistsDatabase(event.getPlayer().getUniqueId().toString())) {
                settings.getCache().getCooldowns().put(event.getPlayer(),
                        new Cooldown(event.getPlayer().getUniqueId().toString(),
                                sqlBounty.getCooldownDatabase(event.getPlayer().getUniqueId().toString()),
                                event.getPlayer().getName()));
            }
        }
    }

    @EventHandler
    public void playerLeave(PlayerQuitEvent event) {
        if (settings.isUsingMySQL()) {
            if (BountyRewards.settings.getCache().getBounties().containsKey(event.getPlayer())) {
                BountyRewards.settings.getCache().getBounties().remove(event.getPlayer());
            }
            if (settings.getCache().getCooldowns().containsKey(event.getPlayer())) {
                settings.getCache().getCooldowns().remove(event.getPlayer());
            }
        }
    }

    @EventHandler
    public void playerLeave(PlayerKickEvent event) {
        if (settings.isUsingMySQL()) {
            if (BountyRewards.settings.getCache().getBounties().containsKey(event.getPlayer())) {
                BountyRewards.settings.getCache().getBounties().remove(event.getPlayer());
            }
            if (settings.getCache().getCooldowns().containsKey(event.getPlayer())) {
                settings.getCache().getCooldowns().remove(event.getPlayer());
            }
        }
    }
}
