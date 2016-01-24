package io.github.mac_genius.bountyrewards.CommandInfo.RemoveBountyPrompts;

import io.github.mac_genius.bountyrewards.PluginSettings;
import io.github.mac_genius.bountyrewards.Utils.Announcement;
import io.github.mac_genius.bountyrewards.storage.Bounty;
import io.github.mac_genius.bountyrewards.storage.ConfigHandler;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

/**
 * Created by Mac on 1/18/2016.
 */
public class RemoveFinalPrompt extends MessagePrompt {
    private PluginSettings settings;
    private Player player;
    private Bounty bounty;

    public RemoveFinalPrompt(PluginSettings settings, Player player, Bounty bounty) {
        this.settings = settings;
        this.player = player;
        this.bounty = bounty;
    }

    @Override
    protected Prompt getNextPrompt(ConversationContext conversationContext) {
        return null;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        ConfigHandler configHandler = new ConfigHandler(settings);
        int amount = (int) ((((double) configHandler.getSettings().getRefund()) / 100.0) * bounty.getAmount());
        settings.getEconomy().depositPlayer(player, amount);
        Announcement.announce(configHandler.getMessage("RemoveAnnouncement").replace("%player%", bounty.getPlayerName()));
        return configHandler.getMessage("BountyCancel").replace("%player%", bounty.getPlayerName()).replace("%money%", amount + "");
    }
}
