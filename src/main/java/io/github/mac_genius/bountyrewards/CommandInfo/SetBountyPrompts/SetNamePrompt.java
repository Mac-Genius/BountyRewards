package io.github.mac_genius.bountyrewards.CommandInfo.SetBountyPrompts;

import io.github.mac_genius.bountyrewards.PluginSettings;
import io.github.mac_genius.bountyrewards.Utils.PlayerUtils;
import io.github.mac_genius.bountyrewards.storage.Bounty;
import io.github.mac_genius.bountyrewards.storage.BountyHandler;
import io.github.mac_genius.bountyrewards.storage.ConfigHandler;
import org.bukkit.Bukkit;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

/**
 * Created by Mac on 1/18/2016.
 */
public class SetNamePrompt extends StringPrompt {
    private Player player;
    private PluginSettings settings;
    private Bounty bounty;
    private String message;

    public SetNamePrompt(Player player, PluginSettings settings, String message) {
        this.player = player;
        bounty = new Bounty();
        this.settings = settings;
        bounty.setSetterName(player.getName());
        bounty.setSetterUUID(PlayerUtils.getUUID(player.getName()));
        this.message = message;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return message;
    }

    @Override
    public Prompt acceptInput(ConversationContext conversationContext, String s) {
        ConfigHandler configHandler = new ConfigHandler(settings);
        BountyHandler bountyHandler = new BountyHandler(settings);
        if (s.equals(player.getName())) {
            return new SetNamePrompt(player, settings, configHandler.getMessage("PlaceOnYourself"));
        }
        String playerUUID = PlayerUtils.getUUID(s);
        if (playerUUID != null) {
            if (bountyHandler.bountyExists(PlayerUtils.getUUID(s))) {
                return new SetNamePrompt(player, settings, configHandler.getMessage("SetAlreadyExists").replace("%player%", s));
            } else if (bountyHandler.cooldownExists(playerUUID)) {
                long time = System.currentTimeMillis() - bountyHandler.getCooldown(playerUUID).getTime();
                int timeLeft = configHandler.getSettings().getCooldown() - (int) Math.floor(((double) time) / 60000.0);
                if (time >= (long) (configHandler.getSettings().getCooldown()) * 60000) {
                    settings.getPlugin().getServer().getScheduler().runTaskAsynchronously(settings.getPlugin(), () -> {
                        bountyHandler.removePlayerCooldown(playerUUID);
                    });
                    bounty.setPlayerName(s);
                    bounty.setPlayerUUID(playerUUID);
                    String messsage = configHandler.getMessage("SetAmountMessage").replace("%player%", bounty.getPlayerName())
                            .replace("%transfee%", configHandler.getSettings().getTransFee() + "")
                            .replace("%max%", configHandler.getSettings().getMaxSet() + "")
                            .replace("-1", "infinity");
                    return new AmountPrompt(player, settings, bounty, messsage);
                } else {
                    return new SetNamePrompt(player, settings, configHandler.getMessage("SetCooldownMessage").replace("%player%", s).replace("%time%", (timeLeft <= 1.0 ? timeLeft + " more minute" : timeLeft + " more minutes")));
                }
            } else {
                bounty.setPlayerName(s);
                bounty.setPlayerUUID(playerUUID);
                String messsage = configHandler.getMessage("SetAmountMessage").replace("%player%", bounty.getPlayerName())
                        .replace("%transfee%", configHandler.getSettings().getTransFee() + "")
                        .replace("%max%", configHandler.getSettings().getMaxSet() + "")
                        .replace("-1", "infinity");
                return new AmountPrompt(player, settings, bounty, messsage);
            }

        }
        return new SetNamePrompt(player, settings, configHandler.getMessage("SetOfflineMessage").replace("%player%", s));
    }
}
