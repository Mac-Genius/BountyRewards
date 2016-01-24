package io.github.mac_genius.bountyrewards.CommandInfo.IncreaseBountyPrompts;

import io.github.mac_genius.bountyrewards.PluginSettings;
import io.github.mac_genius.bountyrewards.storage.Bounty;
import io.github.mac_genius.bountyrewards.storage.ConfigHandler;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

/**
 * Created by Mac on 1/19/2016.
 */
public class IncreaseAmountPrompt extends NumericPrompt {
    private PluginSettings settings;
    private Player player;
    private Bounty bounty;
    private String message;

    public IncreaseAmountPrompt(PluginSettings settings, Player player, Bounty bounty, String message) {
        this.settings = settings;
        this.player = player;
        this.bounty = bounty;
        this.message = message;
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext conversationContext, Number number) {
        ConfigHandler configHandler = new ConfigHandler(settings);
        int max = configHandler.getSettings().getMaxSet();
        if (max < 0 || bounty.getAmount() + number.intValue() <= max) {
            if (settings.getEconomy().getBalance(player) >= bounty.getAmount() + number.intValue()) {
                bounty.setAmount(bounty.getAmount() + number.intValue());
                return new IncreaseFinalPrompt(settings, player, bounty);
            } else {
                return new IncreaseAmountPrompt(settings, player, bounty, configHandler.getMessage("IncreaseNoMoney").replace("%money%", settings.getEconomy().getBalance(player) + ""));
            }
        } else {
            return new IncreaseAmountPrompt(settings, player, bounty, configHandler.getMessage("IncreaseOverMax").replace("%max%", configHandler.getSettings().getMaxSet() + ""));
        }
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return message;
    }
}
