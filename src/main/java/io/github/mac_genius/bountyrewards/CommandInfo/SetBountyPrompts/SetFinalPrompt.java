package io.github.mac_genius.bountyrewards.CommandInfo.SetBountyPrompts;

import io.github.mac_genius.bountyrewards.PluginSettings;
import io.github.mac_genius.bountyrewards.Utils.Announcement;
import io.github.mac_genius.bountyrewards.storage.Bounty;
import io.github.mac_genius.bountyrewards.storage.BountyHandler;
import io.github.mac_genius.bountyrewards.storage.ConfigHandler;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

import java.sql.Timestamp;

/**
 * Created by Mac on 1/18/2016.
 */
public class SetFinalPrompt extends MessagePrompt {
    private PluginSettings settings;
    private Player player;
    private Bounty bounty;
    private boolean anon;

    public SetFinalPrompt(PluginSettings settings, Player player, Bounty bounty, boolean anon) {
        this.settings = settings;
        this.player = player;
        this.bounty = bounty;
        this.anon = anon;
    }

    @Override
    protected Prompt getNextPrompt(ConversationContext conversationContext) {
        return null;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        ConfigHandler configHandler = new ConfigHandler(settings);
        BountyHandler bountyHandler = new BountyHandler(settings);
        bounty.setWhenSet(new Timestamp(System.currentTimeMillis()));
        String message = "";
        if (anon) {
            if (bountyHandler.addBounty(bounty)) {
                settings.getEconomy().withdrawPlayer(player, bounty.getAmount() + configHandler.getSettings().getTransFee());
                message = configHandler.getMessage("AnonymousBroadcast").replace("%money%", bounty.getAmount() + "").replace("%player%", bounty.getPlayerName());
                Announcement.announce(message);
            } else {
                message = configHandler.getMessage("SetAlreadyExists").replace("%player%", bounty.getPlayerName());
            }
        } else {
            if (bountyHandler.addBounty(bounty)) {
                settings.getEconomy().withdrawPlayer(player, bounty.getAmount() + configHandler.getSettings().getTransFee());
                message = configHandler.getMessage("BountyBroadcast").replace("%money%", bounty.getAmount() + "").replace("%player%", bounty.getPlayerName()).replace("%placer%", bounty.getSetterName());
                Announcement.announce(message);
            } else {
                message = configHandler.getMessage("SetAlreadyExists").replace("%player%", bounty.getPlayerName());
            }
        }
        return message;
    }
}
