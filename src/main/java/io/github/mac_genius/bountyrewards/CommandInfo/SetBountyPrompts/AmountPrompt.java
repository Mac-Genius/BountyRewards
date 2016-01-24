package io.github.mac_genius.bountyrewards.CommandInfo.SetBountyPrompts;

import io.github.mac_genius.bountyrewards.PluginSettings;
import io.github.mac_genius.bountyrewards.storage.Bounty;
import io.github.mac_genius.bountyrewards.storage.ConfigHandler;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

/**
 * Created by Mac on 1/18/2016.
 */
public class AmountPrompt extends NumericPrompt {
    private Player player;
    private PluginSettings settings;
    private Bounty bounty;
    private String message;

    public AmountPrompt(Player player, PluginSettings settings, Bounty bounty, String message) {
        this.player = player;
        this.settings = settings;
        this.bounty = bounty;
        this.message = message;
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext conversationContext, Number number) {
        ConfigHandler configHandler = new ConfigHandler(settings);
        if (settings.getEconomy().hasAccount(player)) {
            if (number.intValue() >= configHandler.getSettings().getMinSet()) {
                if (configHandler.getSettings().getMaxSet() > 0 && number.intValue() <= configHandler.getSettings().getMaxSet() || configHandler.getSettings().getMaxSet() < 0) {
                    if (settings.getEconomy().getBalance(player) >= number.intValue() + configHandler.getSettings().getTransFee()) {
                        bounty.setAmount(number.intValue());
                        if (configHandler.getSettings().isAnon() && player.hasPermission("br.anonymous")) {
                            return new AnonymousPrompt(settings, player, bounty);
                        } else {
                            return new SetFinalPrompt(settings, player, bounty, false);
                        }
                    } else {
                        return new AmountPrompt(player, settings, bounty, configHandler.getMessage("NoMoney").replace("%money%", settings.getEconomy().getBalance(player) + ""));
                    }
                } else {
                    return new AmountPrompt(player, settings, bounty, configHandler.getMessage("SetOverMax").replace("%max%", configHandler.getSettings().getMaxSet() + ""));
                }
            } else {
                return new AmountPrompt(player, settings, bounty, configHandler.getMessage("SetMinMessage").replace("%min%", configHandler.getSettings().getMinSet() + ""));
            }
        }
        return null;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        ConfigHandler configHandler = new ConfigHandler(settings);
        return message;
    }
}
