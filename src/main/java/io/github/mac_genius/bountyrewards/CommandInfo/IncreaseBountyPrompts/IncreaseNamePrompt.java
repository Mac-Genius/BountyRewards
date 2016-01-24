package io.github.mac_genius.bountyrewards.CommandInfo.IncreaseBountyPrompts;

import io.github.mac_genius.bountyrewards.PluginSettings;
import io.github.mac_genius.bountyrewards.storage.Bounty;
import io.github.mac_genius.bountyrewards.storage.BountyHandler;
import io.github.mac_genius.bountyrewards.storage.ConfigHandler;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

/**
 * Created by Mac on 1/18/2016.
 */
public class IncreaseNamePrompt extends StringPrompt {
    private PluginSettings settings;
    private Player player;
    private String message;

    public IncreaseNamePrompt(PluginSettings settings, Player player, String message) {
        this.settings = settings;
        this.player = player;
        this.message = message;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return message;
    }

    @Override
    public Prompt acceptInput(ConversationContext conversationContext, String s) {
        Bounty bounty = new BountyHandler(settings).getBountyByName(s);
        ConfigHandler configHandler = new ConfigHandler(settings);
        if (bounty != null) {
            String message = configHandler.getMessage("IncreaseAmountMessage").replace("%current%", bounty.getAmount() + "").replace("%max%", configHandler.getSettings().getMaxSet() + "").replace("-1", "infinity");
            return new IncreaseAmountPrompt(settings, player, bounty, message);
        } else {
            return new IncreaseNamePrompt(settings, player, configHandler.getMessage("IncreaseNoBounty").replace("%player%", s));
        }
    }
}
