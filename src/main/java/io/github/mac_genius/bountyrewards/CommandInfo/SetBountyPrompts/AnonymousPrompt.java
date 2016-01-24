package io.github.mac_genius.bountyrewards.CommandInfo.SetBountyPrompts;

import io.github.mac_genius.bountyrewards.PluginSettings;
import io.github.mac_genius.bountyrewards.storage.Bounty;
import io.github.mac_genius.bountyrewards.storage.ConfigHandler;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

/**
 * Created by Mac on 1/18/2016.
 */
public class AnonymousPrompt extends StringPrompt {
    private PluginSettings settings;
    private Player player;
    private Bounty bounty;

    public AnonymousPrompt(PluginSettings settings, Player player, Bounty bounty) {
        this.settings = settings;
        this.player = player;
        this.bounty = bounty;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return new ConfigHandler(settings).getMessage("AnonymousMessage");
    }

    @Override
    public Prompt acceptInput(ConversationContext conversationContext, String s) {
        if (s.equalsIgnoreCase("no")) {
            return new SetFinalPrompt(settings, player, bounty, false);
        } else if (s.equalsIgnoreCase("yes")) {
            return new SetFinalPrompt(settings, player, bounty, true);
        } else {
            return new AnonymousPrompt(settings, player, bounty);
        }
    }
}
