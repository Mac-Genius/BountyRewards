package io.github.mac_genius.bountyrewards.CommandInfo;

import io.github.mac_genius.bountyrewards.CommandInfo.IncreaseBountyPrompts.IncreaseAbandonListener;
import io.github.mac_genius.bountyrewards.CommandInfo.IncreaseBountyPrompts.IncreaseNamePrompt;
import io.github.mac_genius.bountyrewards.CommandInfo.RemoveBountyPrompts.RemoveAbandonListener;
import io.github.mac_genius.bountyrewards.CommandInfo.RemoveBountyPrompts.RemoveNamePrompt;
import io.github.mac_genius.bountyrewards.CommandInfo.SetBountyPrompts.SetAbandonListener;
import io.github.mac_genius.bountyrewards.CommandInfo.SetBountyPrompts.SetNamePrompt;
import io.github.mac_genius.bountyrewards.PluginSettings;
import io.github.mac_genius.bountyrewards.storage.Bounty;
import io.github.mac_genius.bountyrewards.storage.BountyHandler;
import io.github.mac_genius.bountyrewards.storage.ConfigHandler;
import io.github.mac_genius.bountyrewards.storage.ConfigSettings;
import io.github.mac_genius.bountyrewards.storage.MySQL.MySQLConfig;
import io.github.mac_genius.bountyrewards.storage.local.LocalBounty;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * Created by Mac on 4/25/2015.
 */
public class Command implements CommandExecutor {
    private PluginSettings settings;

    public Command(PluginSettings settings) {
        this.settings = settings;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] args) {
        if (command.getName().equalsIgnoreCase("bounty")) {
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;
                if (player.hasPermission("br.help")) {
                    if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
                        commandSender.sendMessage(ChatColor.GREEN + "-- BountyRewards Help --");
                        commandSender.sendMessage(ChatColor.GOLD + "/bounty help" + ChatColor.WHITE + " commands");
                        commandSender.sendMessage(ChatColor.GOLD + "/bounty list" + ChatColor.WHITE + " lists all available bounties");
                        commandSender.sendMessage(ChatColor.GOLD + "/bounty set" + ChatColor.WHITE + " sets a player's bounty");
                        if (player.hasPermission("br.remove")) {
                            commandSender.sendMessage(ChatColor.GOLD + "/bounty remove" + ChatColor.WHITE + " removes a player's bounty");
                        }
                        commandSender.sendMessage(ChatColor.GOLD + "/bounty increase" + ChatColor.WHITE + " increases the amount a player's bounty is worth");
                        if (player.hasPermission("br.admin")) {
                            commandSender.sendMessage(ChatColor.GOLD + "/bounty reload" + ChatColor.WHITE + " reloads the config");
                            if (settings.isUsingMySQL()) {
                                commandSender.sendMessage(ChatColor.GOLD + "/bounty updatesql" + ChatColor.WHITE + " updates the database with information from this config");
                            }
                        }
                        return true;
                    }
                }
                if (args[0] != null) {
                    // Set a bounty
                    if (commandSender.hasPermission("br.setbounty") && args[0].equalsIgnoreCase("set")) {
                        SetAbandonListener listener = new SetAbandonListener(player, settings);
                        ConversationFactory factory = new ConversationFactory(settings.getPlugin()).withTimeout(20).addConversationAbandonedListener(listener).withEscapeSequence("cancel").withPrefix(new Prefix(new ConfigHandler(settings).getMessage("SetPrefix")));
                        String message = new ConfigHandler(settings).getMessage("SetBountyMessage");
                        Conversation conversation = factory.withFirstPrompt(new SetNamePrompt(player, settings, message)).withLocalEcho(false).buildConversation((Player) commandSender);
                        conversation.begin();
                        return true;
                    }
                    // Removes a bounty
                    if (player.hasPermission("br.removebounty") && args[0].equalsIgnoreCase("remove")) {
                        if (new ConfigHandler(settings).getSettings().isCancelBounty()) {
                            ConfigHandler configHandler = new ConfigHandler(settings);
                            ArrayList<Bounty> list = new BountyHandler(settings).getPlayerBounties(player.getUniqueId().toString());
                            if (list.size() > 0) {
                                int i = 1;
                                player.sendMessage(configHandler.getMessage("BountyHeader"));
                                for (Bounty b : list) {
                                    player.sendMessage(configHandler.getMessage("BountyList").replace("%number%", i + "").replace("%player%", b.getPlayerName()).replace("%money%", b.getAmount() + ""));
                                }
                                RemoveAbandonListener listener = new RemoveAbandonListener(settings, player);
                                //ConversationFactory factory = ;
                                Conversation conversation = new ConversationFactory(settings.getPlugin()).withLocalEcho(false).addConversationAbandonedListener(listener).withEscapeSequence("cancel").withTimeout(20).withPrefix(new Prefix(configHandler.getMessage("RemovePrefix"))).withFirstPrompt(new RemoveNamePrompt(settings, player, list)).buildConversation((Player) commandSender);
                                settings.getPlugin().getLogger().info(conversation.getPrefix().toString());
                                conversation.begin();
                            } else {
                                player.sendMessage(configHandler.getMessage("RemoveNoBounties"));
                            }
                        } else {
                            player.sendMessage(new ConfigHandler(settings).getMessage("RemoveDisabled"));
                        }
                        return true;
                    }
                    // Increases a bounty amount
                    if (player.hasPermission("br.increasebounty") && args[0].equalsIgnoreCase("increase")) {
                        String message = new ConfigHandler(settings).getMessage("IncreaseBountyMessage");
                        ConversationFactory factory = new ConversationFactory(settings.getPlugin()).withLocalEcho(false).withEscapeSequence("cancel").withTimeout(20).addConversationAbandonedListener(new IncreaseAbandonListener(settings, player)).withPrefix(new Prefix(new ConfigHandler(settings).getMessage("IncreasePrefix")));
                        Conversation conversation = factory.withFirstPrompt(new IncreaseNamePrompt(settings, player, message)).buildConversation((Player) commandSender);
                        conversation.begin();
                        return true;
                    }
                    // List all available bounties
                    if (commandSender.hasPermission("br.list") && args[0].equalsIgnoreCase("list")) {
                        ConfigHandler configHandler = new ConfigHandler(settings);
                        BountyHandler bountyHandler = new BountyHandler(settings);
                        ArrayList<Bounty> list = new ArrayList<>(bountyHandler.getAllBounties());
                        if (list.size() > 0) {
                            player.sendMessage(configHandler.getMessage("BountyHeader"));
                            int i = 1;
                            for (Bounty b : list) {
                                player.sendMessage(configHandler.getMessage("BountyList").replace("%number%", i + "").replace("%player%", b.getPlayerName()).replace("%money%", b.getAmount() + ""));
                                i++;
                            }
                        } else {
                            player.sendMessage(configHandler.getMessage("NoBounties"));
                        }
                        return true;
                    }
                    if (commandSender.hasPermission("br.reload")) {
                        if (args[0].equalsIgnoreCase("reload")) {
                            settings.getPlugin().reloadConfig();
                            commandSender.sendMessage(ChatColor.GREEN + "[BountyRewards]" + ChatColor.WHITE + " Config reloaded!");
                            return true;
                        }
                    }
                    if (player.hasPermission("br.updatesql") && args[0].equalsIgnoreCase("updatesql")) {
                        if (settings.isUsingMySQL()) {
                            settings.getPlugin().reloadConfig();
                            MySQLConfig mySQLConfig = new MySQLConfig(settings);
                            mySQLConfig.updateSettings(new ConfigHandler(settings).getLocalSettings());
                            mySQLConfig.updateAllMessages();
                            player.sendMessage(ChatColor.GREEN + "[BountyRewards]" + ChatColor.WHITE + " Database updated!");
                        } else {
                            player.sendMessage(ChatColor.GREEN + "[BountyRewards]" + ChatColor.WHITE + "You are not using the database!");
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
