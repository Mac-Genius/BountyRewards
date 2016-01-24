package io.github.mac_genius.bountyrewards.Utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.github.mac_genius.bountyrewards.BountyRewards;
import io.github.mac_genius.bountyrewards.storage.ConfigHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.fusesource.jansi.Ansi;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Mac on 1/20/2016.
 */
public class Announcement {

    public static void announce(String announcement) {
        ConfigHandler configHandler = new ConfigHandler(BountyRewards.settings);
        if (configHandler.getSettings().isBungeecord()) {
            try {
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("Forward");
                out.writeUTF("ALL");
                out.writeUTF("BountyRewards");
                ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
                DataOutputStream msgout = new DataOutputStream(msgbytes);
                msgout.writeLong(System.currentTimeMillis());
                msgout.writeUTF("Message");
                msgout.writeUTF(announcement);
                out.writeShort(msgbytes.toByteArray().length);
                out.write(msgbytes.toByteArray());
                ArrayList<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
                if (players.size() > 0) {
                    players.get(0).sendPluginMessage(BountyRewards.settings.getPlugin(), "BungeeCord", out.toByteArray());
                }
                Bukkit.broadcastMessage(announcement);
            } catch (IOException e) {
                BountyRewards.settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Error writing your message to an array.." + Ansi.ansi().fg(Ansi.Color.WHITE));
            }
        } else {
            Bukkit.broadcastMessage(announcement);
        }
    }
}
