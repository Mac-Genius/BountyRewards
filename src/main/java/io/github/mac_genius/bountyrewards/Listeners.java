package io.github.mac_genius.bountyrewards;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Mac on 4/25/2015.
 */
public class Listeners implements Listener {
    private Plugin plugin;
    private Economy economy;

    public Listeners(Plugin pluginIn, Economy economyIn) {
        plugin = pluginIn;
        economy = economyIn;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        String bountyComplete = plugin.getConfig().getString("BountyComplete");
        bountyComplete = ChatColor.translateAlternateColorCodes('&', bountyComplete);
        ArrayList<String> bounties = new ArrayList<>(plugin.getConfig().getStringList("CurrentBounties"));
        ArrayList<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
        plugin.getLogger().info("bop");
        if (bounties.size() > 0) {
            int i = 0;
            for (String s : plugin.getConfig().getStringList("CurrentBounties")) {
                Scanner scan = new Scanner(s).useDelimiter(" ");
                Player setBounty = null;
                String playerName = scan.next();
                for (Player player : onlinePlayers) {
                    if (player.getName().equals(playerName)) {
                        setBounty = player;
                    }
                }
                if (setBounty != null) {
                    double amount = Double.parseDouble(scan.next());
                    Player bounty = event.getEntity();
                    plugin.getLogger().info("boo");

                    economy.depositPlayer((OfflinePlayer) event.getEntity().getKiller(), amount);
                    bounties.remove(i);
                    plugin.getConfig().set("CurrentBounties", bounties);
                    plugin.saveConfig();
                    plugin.getLogger().info("boop");
                    bountyComplete = bountyComplete.replaceAll("%collector%", event.getEntity().getKiller().getName());
                    bountyComplete = bountyComplete.replaceAll("%money%", amount + "");
                    bountyComplete = bountyComplete.replaceAll("%player%", event.getEntity().getName());
                    Bukkit.broadcastMessage(bountyComplete);
                    i++;
                    event.setDeathMessage("");
                    return;
                }
            }
        }
    }
}
