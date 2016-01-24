package io.github.mac_genius.bountyrewards.storage.MySQL;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.github.mac_genius.bountyrewards.BountyRewards;
import io.github.mac_genius.bountyrewards.PluginSettings;
import io.github.mac_genius.bountyrewards.storage.Bounty;
import io.github.mac_genius.bountyrewards.storage.ConfigHandler;
import io.github.mac_genius.bountyrewards.storage.ConfigSettings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.fusesource.jansi.Ansi;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Mac on 1/9/2016.
 */
public class MySQLConfig {
    private PluginSettings settings;

    public MySQLConfig(PluginSettings settings) {
        this.settings = settings;
    }

    public String getMessage(String name) {
        Connection connection = new SQLConnect(settings).getConnection();
        if (connection != null) {
            try {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM BountyR_Messages WHERE MessageType='" + name + "'");
                ResultSet set = statement.executeQuery();
                String message = "";
                while (set.next()) {
                    message = set.getString(3);
                }
                statement.close();
                connection.close();
                return message;
            } catch (SQLException e) {
                settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Error fetching the message for " + name + "." + Ansi.ansi().fg(Ansi.Color.WHITE));
                return "";
            }
        }
        return "";
    }

    public HashMap<String, String> getAllMessages() {
        Connection connection = new SQLConnect(settings).getConnection();
        HashMap<String, String> messages = new HashMap<>();
        if (connection != null) {
            try {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM BountyR_Messages");
                ResultSet set = statement.executeQuery();
                while (set.next()) {
                    messages.put(set.getString(2), set.getString(3));
                }
                statement.close();
                connection.close();
                return messages;
            } catch (SQLException e) {
                settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Error fetching all messages." + Ansi.ansi().fg(Ansi.Color.WHITE));
                return messages;
            }
        }
        return messages;
    }

    public void updateMessage(String name, String message) {
        refreshNetworkMessages();
        Connection connection = new SQLConnect(settings).getConnection();
        if (connection != null) {
            try {
                PreparedStatement statement = connection.prepareStatement("UPDATE BountyR_Messages SET BountyBroadcast='" + message + "' WHERE MessageType='" + name + "'");
                statement.executeUpdate();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Error updating the message for " + name + "." + Ansi.ansi().fg(Ansi.Color.WHITE));
            }
        }
    }

    public void updateAllMessages() {
        Connection connection = new SQLConnect(settings).getConnection();
        if (connection != null) {
            ConfigHandler configHandler = new ConfigHandler(settings);
            for (String s : settings.getMessageNames()) {
                if (messageExists(s, connection)) {
                    try {
                        PreparedStatement statement = connection.prepareStatement("UPDATE BountyR_Messages SET BountyBroadcast='" + configHandler.getLocalMessage(s) + "' WHERE MessageType='" + s + "'");
                        statement.executeUpdate();
                        statement.close();
                    } catch (SQLException e) {
                        settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Error updating the message for " + s + "." + Ansi.ansi().fg(Ansi.Color.WHITE));
                    }
                } else {
                    try {
                        PreparedStatement statement = connection.prepareStatement("INSERT INTO BountyR_Messages (MessageType, BountyBroadcast) VALUES (?,?)");
                        statement.setString(1, s);
                        statement.setString(2, configHandler.getLocalMessage(s));
                        statement.executeUpdate();
                    } catch (SQLException e) {
                        settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Error updating the message for " + s + "." + Ansi.ansi().fg(Ansi.Color.WHITE));
                    }
                }
            }
            try {
                connection.close();
            } catch (SQLException e) {

            }
        }
        refreshNetworkMessages();
    }

    private boolean messageExists(String message, Connection connection) {
        try {
            boolean found = false;
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM BountyR_Messages WHERE MessageType='" + message + "'");
            ResultSet set = statement.executeQuery();
            String s = "";
            while (set.next()) {
                s = set.getString(2);
            }
            if (!s.equals("")) {
                found = true;
            }
            statement.close();
            return found;
        } catch (SQLException e) {
            settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Couldn't check if a message exists." + Ansi.ansi().fg(Ansi.Color.WHITE));
        }
        return false;
    }

    public ConfigSettings getSettings() {
        ConfigSettings configSettings = null;
        Connection connection = new SQLConnect(settings).getConnection();
        if (connection != null) {
            try {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM BountyR_Settings WHERE Id='1'");
                ResultSet set = statement.executeQuery();
                while (set.next()) {
                    configSettings = new ConfigSettings(set.getBoolean(2), set.getInt(3), set.getInt(4), set.getInt(5), set.getInt(6), set.getInt(7),
                            set.getString(8), set.getBoolean(9), set.getInt(10), set.getInt(11), set.getInt(12), set.getBoolean(13),
                            set.getBoolean(14));
                }
                statement.close();
                connection.close();
            } catch (SQLException e) {
                settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Couldn't fetch the data for the config." + Ansi.ansi().fg(Ansi.Color.WHITE));
            }
        }
        return configSettings;
    }

    public void updateSettings(ConfigSettings configSettings) {
        Connection connection = new SQLConnect(settings).getConnection();
        if (connection != null) {
            try {
                PreparedStatement statement = connection.prepareStatement("UPDATE BountyR_Settings SET CancelBounty='" +
                        (configSettings.isCancelBounty() ? 1 : 0) + "', Refund='" + configSettings.getRefund() + "', MinSet='" + configSettings.getMinSet() +
                        "', MaxSet='" + configSettings.getMaxSet() + "', TransFee='" + configSettings.getTransFee() + "', DeathCost='" +
                        configSettings.getDeathCost() + "', DeathCostMode='" + configSettings.getDeathCostMode() + "', RanDeathCost='" +
                        (configSettings.isRanDeathCost() ? 1 : 0) + "', RanRange='" + configSettings.getRanRange() + "', Cooldown='" +
                        configSettings.getCooldown() + "', Expiration='" + configSettings.getExpiration() + "', Anon='" + (configSettings.isAnon() ? 1 : 0)
                        + "', BungeeCord='" + (configSettings.isBungeecord() ? 1 : 0) + "' WHERE Id='1'");
                statement.executeUpdate();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Couldn't update the database settings." + Ansi.ansi().fg(Ansi.Color.WHITE));
            }
        }
        refreshNetworkSettings();
    }

    private void refreshNetworkMessages() {
        settings.getCache().setMessages(getAllMessages());
        try {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Forward");
            out.writeUTF("ALL");
            out.writeUTF("BountyRewards");
            ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
            DataOutputStream msgout = new DataOutputStream(msgbytes);
            msgout.writeLong(System.currentTimeMillis());
            msgout.writeUTF("Update");
            msgout.writeUTF("Message");
            out.writeShort(msgbytes.toByteArray().length);
            out.write(msgbytes.toByteArray());
            ArrayList<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
            if (players.size() > 0) {
                players.get(0).sendPluginMessage(BountyRewards.settings.getPlugin(), "BungeeCord", out.toByteArray());
            }
        } catch (IOException e) {
            settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Error sending updateBounty plugin message." + Ansi.ansi().fg(Ansi.Color.WHITE));
        }
    }

    private void refreshNetworkSettings() {
        settings.getCache().setSettings(getSettings());
        try {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Forward");
            out.writeUTF("ALL");
            out.writeUTF("BountyRewards");
            ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
            DataOutputStream msgout = new DataOutputStream(msgbytes);
            msgout.writeLong(System.currentTimeMillis());
            msgout.writeUTF("Update");
            msgout.writeUTF("Settings");
            out.writeShort(msgbytes.toByteArray().length);
            out.write(msgbytes.toByteArray());
            ArrayList<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
            if (players.size() > 0) {
                players.get(0).sendPluginMessage(BountyRewards.settings.getPlugin(), "BungeeCord", out.toByteArray());
            }
        } catch (IOException e) {
            settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Error sending updateBounty plugin message." + Ansi.ansi().fg(Ansi.Color.WHITE));
        }
    }
}