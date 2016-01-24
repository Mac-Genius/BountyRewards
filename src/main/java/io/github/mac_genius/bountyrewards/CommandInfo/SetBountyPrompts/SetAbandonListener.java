package io.github.mac_genius.bountyrewards.CommandInfo.SetBountyPrompts;

import io.github.mac_genius.bountyrewards.PluginSettings;
import io.github.mac_genius.bountyrewards.storage.ConfigHandler;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationAbandonedListener;
import org.bukkit.conversations.InactivityConversationCanceller;
import org.bukkit.entity.Player;

/**
 * Created by Mac on 1/19/2016.
 */
public class SetAbandonListener implements ConversationAbandonedListener {
    private Player player;
    private PluginSettings settings;

    public SetAbandonListener(Player player, PluginSettings settings) {
        this.player = player;
        this.settings = settings;
    }

    @Override
    public void conversationAbandoned(ConversationAbandonedEvent conversationAbandonedEvent) {
        if (!conversationAbandonedEvent.gracefulExit()) {
            if (conversationAbandonedEvent.getCanceller() instanceof InactivityConversationCanceller) {
                player.sendMessage(new ConfigHandler(settings).getMessage("TimeoutMessage").replace("%action%", "placing"));
            } else {
                player.sendMessage(new ConfigHandler(settings).getMessage("CancelAction").replace("%action%", "placing"));
            }
        }
    }

}
