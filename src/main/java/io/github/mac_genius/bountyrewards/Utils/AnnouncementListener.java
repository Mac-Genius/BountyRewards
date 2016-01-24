package io.github.mac_genius.bountyrewards.Utils;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.github.mac_genius.bountyrewards.BountyRewards;
import io.github.mac_genius.bountyrewards.PluginSettings;
import io.github.mac_genius.bountyrewards.storage.Bounty;
import io.github.mac_genius.bountyrewards.storage.MySQL.MySQLConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.fusesource.jansi.Ansi;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Mac on 1/20/2016.
 */
public class AnnouncementListener implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String s, Player player, byte[] bytes) {
        if (!s.equals("BungeeCord")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
        String subchannel = in.readUTF();
        if (subchannel.equals("BountyRewards")) {
            try {
                short len = in.readShort();
                byte[] message = new byte[len];
                in.readFully(message);
                DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(message));
                long whenSent = msgin.readLong();
                long now = System.currentTimeMillis();
                if (((double)now - whenSent) / 1000.0 <= 1.0) {
                    String type = msgin.readUTF();
                    if (type.equals("Message")) {
                        // Announce messages
                        String someData = msgin.readUTF();
                        Bukkit.broadcastMessage(someData);
                    } else if (type.equals("Remove")) {
                        // Remove a bounty from the cache
                        String uuid = msgin.readUTF();
                        for (Player p : BountyRewards.settings.getCache().getBounties().keySet()) {
                            if (p.getUniqueId().toString().equals(uuid)) {
                                BountyRewards.settings.getCache().getBounties().remove(p);
                                break;
                            }
                        }
                    } else if (type.equals("Update")) {
                        String updateType = msgin.readUTF();
                        if (updateType.equals("Bounty")) {
                            // Update a bounty in the cache
                            String uuid = msgin.readUTF();
                            int amount = msgin.readInt();
                            for (Player p : BountyRewards.settings.getCache().getBounties().keySet()) {
                                if (p.getUniqueId().toString().equals(uuid)) {
                                    Bounty b = BountyRewards.settings.getCache().getBounties().get(p);
                                    b.setAmount(amount);
                                    BountyRewards.settings.getCache().getBounties().put(p, b);
                                    break;
                                }
                            }
                        } else if (updateType.equals("Message")) {
                            // Refresh all messages
                            BountyRewards.settings.getCache().setMessages(new MySQLConfig(BountyRewards.settings).getAllMessages());
                        } else if (updateType.equals("Settings")) {
                            // Refresh the config
                            BountyRewards.settings.getCache().setSettings(new MySQLConfig(BountyRewards.settings).getSettings());
                        }
                    }
                }
            } catch (IOException e) {
                BountyRewards.settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Error reading a message from another server." + Ansi.ansi().fg(Ansi.Color.WHITE));
            }
        }
    }
}
