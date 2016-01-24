package io.github.mac_genius.bountyrewards.CommandInfo.IncreaseBountyPrompts;

import io.github.mac_genius.bountyrewards.PluginSettings;
import io.github.mac_genius.bountyrewards.storage.ConfigHandler;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationAbandonedListener;
import org.bukkit.conversations.InactivityConversationCanceller;
import org.bukkit.entity.Player;

/**
 * Created by Mac on 1/19/2016.
 */
public class IncreaseAbandonListener implements ConversationAbandonedListener {
    private PluginSettings settings;
    private Player player;

    public IncreaseAbandonListener(PluginSettings settings, Player player) {
        this.settings = settings;
        this.player = player;
    }

    @Override
    public void conversationAbandoned(ConversationAbandonedEvent conversationAbandonedEvent) {
        if (!conversationAbandonedEvent.gracefulExit()) {
            if (conversationAbandonedEvent.getCanceller() instanceof InactivityConversationCanceller) {
                player.sendMessage(new ConfigHandler(settings).getMessage("TimeoutMessage").replace("%action%", "increasing"));
            } else {
                player.sendMessage(new ConfigHandler(settings).getMessage("CancelAction").replace("%action%", "increasing"));
            }
        }
    }
}
