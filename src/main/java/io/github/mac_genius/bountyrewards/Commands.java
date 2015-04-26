package io.github.mac_genius.bountyrewards;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Mac on 4/25/2015.
 */
public class Commands implements CommandExecutor {
    private Plugin plugin;
    private Economy economy;

    public Commands(Plugin pluginIn, Economy economyIn) {
        plugin = pluginIn;
        economy = economyIn;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        ArrayList<String> bounties = new ArrayList<>(plugin.getConfig().getStringList("CurrentBounties"));
        String bountyBroadcast = plugin.getConfig().getString("BountyBroadcast");
        String noMoney = plugin.getConfig().getString("NoMoney");
        noMoney = ChatColor.translateAlternateColorCodes('&', noMoney);
        bountyBroadcast = ChatColor.translateAlternateColorCodes('&', bountyBroadcast);
        String bountyHeader = plugin.getConfig().getString("BountyHeader");
        bountyHeader = ChatColor.translateAlternateColorCodes('&', bountyHeader);
        String bountyList = plugin.getConfig().getString("BountyList");
        bountyList = ChatColor.translateAlternateColorCodes('&', bountyList);
        ArrayList<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
        if (command.getName().equalsIgnoreCase("br")) {
            if (commandSender instanceof Player) {
                for (Player p : onlinePlayers) {
                    if (commandSender.hasPermission("br.help")) {
                        if (args.length == 0) {
                            commandSender.sendMessage(ChatColor.GREEN + "-- BountyRewards Help --");
                            commandSender.sendMessage(ChatColor.GOLD + "/br reload" + ChatColor.WHITE + " reloads the config");
                            commandSender.sendMessage(ChatColor.GOLD + "/epc help" + ChatColor.WHITE + " commands");
                            commandSender.sendMessage(ChatColor.GOLD + "/br <player> (amount)" + ChatColor.WHITE + " sets a player's bounty");
                            return true;
                        }
                    }
                    if (commandSender.hasPermission("br.help")) {
                        if (args[0].equalsIgnoreCase("help")) {
                            commandSender.sendMessage(ChatColor.GREEN + "-- BountyRewards Help --");
                            commandSender.sendMessage(ChatColor.GOLD + "/br reload" + ChatColor.WHITE + " reloads the config");
                            commandSender.sendMessage(ChatColor.GOLD + "/epc help" + ChatColor.WHITE + " commands");
                            commandSender.sendMessage(ChatColor.GOLD + "/br <player> (amount)" + ChatColor.WHITE + " sets a player's bounty");
                            return true;
                        }
                    }
                    if (commandSender.hasPermission("br.setbounty")) {
                        if (args[0].equals(commandSender.getName())) {
                            commandSender.sendMessage("You cannot put a bounty on yourself.");
                            return true;
                        }
                        if (args[0].equals(p.getName())) {
                            if (args.length == 1) {
                                commandSender.sendMessage("You need to enter an amount to place a bounty!");
                                return true;
                            }
                            for (String string : plugin.getConfig().getStringList("CurrentBounties")) {
                                String output = bountyList;
                                Scanner scan = new Scanner(string).useDelimiter(" ");
                                String setBounty = scan.next();
                                String amount = scan.next();
                                String bounty = scan.next();
                                if (args[0].equals(bounty)) {
                                    commandSender.sendMessage(bounty + " already has a bounty on him.");
                                    return true;
                                }
                            }
                            double money = Double.parseDouble(args[1]);
                            if (economy.hasAccount((OfflinePlayer) commandSender) && economy.getBalance((OfflinePlayer) commandSender) > money) {
                                bountyBroadcast = bountyBroadcast.replaceAll("%player%", p.getName());
                                bountyBroadcast = bountyBroadcast.replaceAll("%money%", args[1] + "");
                                bounties.add(commandSender.getName() + " " + args[1] + " " + args[0]);
                                plugin.getConfig().set("CurrentBounties", bounties);
                                plugin.saveConfig();
                                economy.withdrawPlayer((OfflinePlayer) commandSender, money);
                                Bukkit.broadcastMessage(bountyBroadcast);
                                return true;
                            } else {
                                commandSender.sendMessage(noMoney);
                                return true;
                            }
                        }
                    }
                    if (commandSender.hasPermission("br.list")) {
                        if (args[0].equalsIgnoreCase("list")) {
                            commandSender.sendMessage(bountyHeader);
                            int i = 1;
                            for (String string : plugin.getConfig().getStringList("CurrentBounties")) {
                                String output = bountyList;
                                Scanner scan = new Scanner(string).useDelimiter(" ");
                                String setBounty = scan.next();
                                String amount = scan.next();
                                String bounty = scan.next();
                                output = output.replaceAll("%number%", i + "");
                                output = output.replaceAll("%player%", bounty);
                                output = output.replaceAll("%money%", amount);
                                commandSender.sendMessage(output);
                                i++;
                            }
                            return true;
                        }
                    }
                    if (commandSender.hasPermission("br.reload")) {
                        if (args[0].equalsIgnoreCase("reload")) {
                            plugin.reloadConfig();
                            commandSender.sendMessage(ChatColor.GREEN + "[BountyRewards]" + ChatColor.WHITE + " Config reloaded!");
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
