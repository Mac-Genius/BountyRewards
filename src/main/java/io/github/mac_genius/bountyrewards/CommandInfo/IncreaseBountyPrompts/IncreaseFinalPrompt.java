package io.github.mac_genius.bountyrewards.CommandInfo.IncreaseBountyPrompts;

import io.github.mac_genius.bountyrewards.PluginSettings;
import io.github.mac_genius.bountyrewards.Utils.Announcement;
import io.github.mac_genius.bountyrewards.storage.Bounty;
import io.github.mac_genius.bountyrewards.storage.BountyHandler;
import io.github.mac_genius.bountyrewards.storage.ConfigHandler;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

/**
 * Created by Mac on 1/18/2016.
 */
public class IncreaseFinalPrompt extends MessagePrompt {
    private PluginSettings settings;
    private Player player;
    private Bounty bounty;

    public IncreaseFinalPrompt(PluginSettings settings, Player player, Bounty bounty) {
        this.settings = settings;
        this.player = player;
        this.bounty = bounty;
    }

    @Override
    protected Prompt getNextPrompt(ConversationContext conversationContext) {
        new BountyHandler(settings).updateBounty(bounty);
        return null;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        String message = new ConfigHandler(settings).getMessage("BountyIncrease").replace("%player%", bounty.getPlayerName()).replace("%money%", bounty.getAmount() + "");
        Announcement.announce(message);
        return message;
    }
}
