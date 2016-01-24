package io.github.mac_genius.bountyrewards.CommandInfo.RemoveBountyPrompts;

import io.github.mac_genius.bountyrewards.PluginSettings;
import io.github.mac_genius.bountyrewards.storage.Bounty;
import io.github.mac_genius.bountyrewards.storage.BountyHandler;
import io.github.mac_genius.bountyrewards.storage.ConfigHandler;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * Created by Mac on 1/18/2016.
 */
public class RemoveNamePrompt extends NumericPrompt {
    private PluginSettings settings;
    private Player player;
    private ArrayList<Bounty> bounties;

    public RemoveNamePrompt(PluginSettings settings, Player player, ArrayList<Bounty> bounties) {
        this.settings = settings;
        this.player = player;
        this.bounties = bounties;
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext conversationContext, Number number) {
        BountyHandler bountyHandler = new BountyHandler(settings);
        bountyHandler.removeBounty(bounties.get(number.intValue() - 1));
        return new RemoveFinalPrompt(settings, player, bounties.get(number.intValue() - 1));
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return new ConfigHandler(settings).getMessage("RemoveBountyMessage");
    }
}
