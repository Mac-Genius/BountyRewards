package io.github.mac_genius.bountyrewards.CommandInfo;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;

/**
 * Created by Mac on 1/19/2016.
 */
public class Prefix implements ConversationPrefix {
    private String prefix;

    public Prefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String getPrefix(ConversationContext conversationContext) {
        return prefix;
    }
}
