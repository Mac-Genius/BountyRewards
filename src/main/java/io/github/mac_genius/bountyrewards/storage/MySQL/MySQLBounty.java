package io.github.mac_genius.bountyrewards.storage.MySQL;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.github.mac_genius.bountyrewards.BountyRewards;
import io.github.mac_genius.bountyrewards.PluginSettings;
import io.github.mac_genius.bountyrewards.storage.Bounty;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.fusesource.jansi.Ansi;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Mac on 1/18/2016.
 */
public class MySQLBounty {
    private PluginSettings settings;

    public MySQLBounty(PluginSettings settings) {
        this.settings = settings;
    }

    public boolean addBounty(Bounty bounty) {
        boolean success = false;
        Connection connection = new SQLConnect(settings).getConnection();
        if (connection != null) {
            try {
                if (bountyExists(bounty)) {
                    success = false;
                }
                PreparedStatement statement = connection.prepareStatement("INSERT INTO BountyR_Active " +
                        "(BountyUUID,SetterUUID,WhenSet,Amount,BountyName,SetterName) VALUES(?,?,?,?,?,?)");
                statement.setString(1, bounty.getPlayerUUID());
                statement.setString(2, bounty.getSetterUUID());
                statement.setTimestamp(3, bounty.getWhenSet());
                statement.setInt(4, bounty.getAmount());
                statement.setString(5, bounty.getPlayerName());
                statement.setString(6, bounty.getSetterName());
                statement.executeUpdate();
                statement.close();
                success = true;
            } catch (SQLException e) {
                settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Error adding a new bounty." + Ansi.ansi().fg(Ansi.Color.WHITE));
                success = false;
            }
            try {
                connection.close();
                success = true;
            } catch (SQLException e) {
                settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Error closing a connection to the database." + Ansi.ansi().fg(Ansi.Color.WHITE));
            }
        }
        if (success) {
            settings.getCache().getBounties().put(Bukkit.getPlayer(UUID.fromString(bounty.getPlayerUUID())), bounty);
        }
        return success;
    }

    public boolean updateBounty(Bounty bounty) {
        refreshNetworkUpdate(bounty);
        Connection connection = new SQLConnect(settings).getConnection();
        if (connection != null) {
            try {
                if (!bountyExists(bounty)) {
                    return false;
                }
                PreparedStatement statement = connection.prepareStatement("UPDATE BountyR_Active SET Amount='" +
                        bounty.getAmount() + "' WHERE BountyUUID='" + bounty.getPlayerUUID() + "'");
                statement.executeUpdate();
                statement.close();
            } catch (SQLException e) {
                settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Error adding a new bounty." + Ansi.ansi().fg(Ansi.Color.WHITE));
                return false;
            }
            try {
                connection.close();
                return true;
            } catch (SQLException e) {
                settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Error closing a connection to the database." + Ansi.ansi().fg(Ansi.Color.WHITE));
            }
        }
        return false;
    }

    public Bounty getBounty(String UUID) {
        for (Player p : settings.getCache().getBounties().keySet()) {
            if (p.getUniqueId().toString().equals(UUID)) {
                return settings.getCache().getBounties().get(p);
            }
        }
        Bounty bounty = new Bounty();
        Connection connection = new SQLConnect(settings).getConnection();
        if (connection != null) {
            try {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM BountyR_Active WHERE BountyUUID='" + UUID + "'");
                ResultSet set = statement.executeQuery();
                while (set.next()) {
                    bounty.setPlayerUUID(set.getString(2));
                    bounty.setSetterUUID(set.getString(3));
                    bounty.setWhenSet(set.getTimestamp(4));
                    bounty.setAmount(set.getInt(5));
                    bounty.setPlayerName(set.getString(6));
                    bounty.setSetterName(set.getString(7));
                }
                connection.close();
            } catch (SQLException e) {
                settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Error fetching a bounty." + Ansi.ansi().fg(Ansi.Color.WHITE));
            }
        }
        return bounty;
    }

    public Bounty getBountyByName(String name) {
        for (Bounty b : settings.getCache().getBounties().values()) {
            if (b.getPlayerName().equalsIgnoreCase(name)) {
                return b;
            }
        }
        Bounty bounty = new Bounty();
        Connection connection = new SQLConnect(settings).getConnection();
        if (connection != null) {
            try {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM BountyR_Active WHERE BountyName='" + name + "'");
                ResultSet set = statement.executeQuery();
                while (set.next()) {
                    bounty.setPlayerUUID(set.getString(2));
                    bounty.setSetterUUID(set.getString(3));
                    bounty.setWhenSet(set.getTimestamp(4));
                    bounty.setAmount(set.getInt(5));
                    bounty.setPlayerName(set.getString(6));
                    bounty.setSetterName(set.getString(7));
                }
                connection.close();
            } catch (SQLException e) {
                settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Error fetching a bounty." + Ansi.ansi().fg(Ansi.Color.WHITE));
            }
        }
        return bounty;
    }

    public ArrayList<Bounty> getPlayerBounties(String UUID) {
        ArrayList<Bounty> list = new ArrayList<>();
        Connection connection = new SQLConnect(settings).getConnection();
        if (connection != null) {
            try {

                PreparedStatement statement = connection.prepareStatement("SELECT * FROM BountyR_Active WHERE SetterUUID='" + UUID + "'");
                ResultSet set = statement.executeQuery();
                while (set.next()) {
                    list.add(new Bounty(set.getString(2), set.getString(3), set.getTimestamp(4), set.getInt(5), set.getString(6), set.getString(7)));
                }
                statement.close();
                connection.close();
            } catch (SQLException e) {
                settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Error fetching a player's bounties." + Ansi.ansi().fg(Ansi.Color.WHITE));
            }
        }
        return list;
    }

    public ArrayList<Bounty> getAllBounties() {
        ArrayList<Bounty> list = new ArrayList<>();
        Connection connection = new SQLConnect(settings).getConnection();
        if (connection != null) {
            try {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM BountyR_Active");
                ResultSet set = statement.executeQuery();
                while (set.next()) {
                    Bounty bounty = new Bounty();
                    bounty.setPlayerUUID(set.getString(2));
                    bounty.setSetterUUID(set.getString(3));
                    bounty.setWhenSet(set.getTimestamp(4));
                    bounty.setAmount(set.getInt(5));
                    bounty.setPlayerName(set.getString(6));
                    bounty.setSetterName(set.getString(7));
                    list.add(bounty);
                }
                connection.close();
            } catch (SQLException e) {
                settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Error fetching all bounties." + Ansi.ansi().fg(Ansi.Color.WHITE));
            }
        }
        return list;
    }

    public boolean removeBounty(Bounty bounty) {
        boolean success = false;
        Connection connection = new SQLConnect(settings).getConnection();
        if (connection != null) {
            try {
                PreparedStatement statement = connection.prepareStatement("DELETE FROM BountyR_Active WHERE BountyUUID='" + bounty.getPlayerUUID() + "'");
                statement.executeUpdate();
                connection.close();
                success = true;
            } catch (SQLException e) {
                settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Error deleting a bounty." + Ansi.ansi().fg(Ansi.Color.WHITE));
            }
        }
        if (success) {
            refreshNetworkRemove(bounty);
        }
        return success;
    }

    public boolean bountyExists(Bounty bounty) {
        for (Player p : settings.getCache().getBounties().keySet()) {
            if (p.getUniqueId().toString().equals(bounty.getPlayerUUID())) {
                return true;
            }
        }
        Connection connection = new SQLConnect(settings).getConnection();
        if (connection != null) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM BountyR_Active WHERE BountyUUID='" + bounty.getPlayerUUID() + "'");
                ResultSet set = preparedStatement.executeQuery();
                while (set.next()) {
                    preparedStatement.close();
                    connection.close();
                    return true;
                }
                connection.close();
            } catch (SQLException e) {
                settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Error checking if a bounty exists." + Ansi.ansi().fg(Ansi.Color.WHITE));
                return false;
            }
        }
        return false;
    }

    public boolean bountyExists(String uuid) {
        for (Player p : settings.getCache().getBounties().keySet()) {
            if (p.getUniqueId().toString().equals(uuid)) {
                return true;
            }
        }
        return false;
    }

    public boolean addPlayerCooldown(String uuid, String name) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        boolean success = false;
        Connection connection = new SQLConnect(settings).getConnection();
        if (connection != null) {
            try {
                if (cooldownExistsDatabase(uuid)) {
                    success = false;
                }
                PreparedStatement statement = connection.prepareStatement("INSERT INTO BountyR_Cooldown (PlayerUUID, LastKilled, PlayerName) VALUES(?,?,?)");
                statement.setString(1, uuid);
                statement.setTimestamp(2, now);
                statement.setString(3, name);
                statement.executeUpdate();
                connection.close();
                success = true;
            } catch (SQLException e) {
                settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Error adding a cooldown for a player." + Ansi.ansi().fg(Ansi.Color.WHITE));
            }
        }
        if (success) {
            settings.getCache().getCooldowns().put(Bukkit.getPlayer(UUID.fromString(uuid)), new Cooldown(uuid, now, name));
        }
        return success;
    }

    public Timestamp getCooldownDatabase(String uuid) {
        Connection connection = new SQLConnect(settings).getConnection();
        if (connection != null) {
            try {
                if (cooldownExistsDatabase(uuid)) {
                    PreparedStatement statement = connection.prepareStatement("SELECT * FROM BountyR_Cooldown WHERE PlayerUUID='" + uuid + "'");
                    ResultSet set = statement.executeQuery();
                    Timestamp timestamp = null;
                    while (set.next()) {
                        timestamp =  set.getTimestamp(3);
                    }
                    statement.close();
                    connection.close();
                    return timestamp;
                }
            } catch (SQLException e) {
                settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Error fetching a cooldown for a player." + Ansi.ansi().fg(Ansi.Color.WHITE));
            }
        }
        return null;
    }

    public Timestamp getCooldown(String uuid) {
        for (Player p : settings.getCache().getCooldowns().keySet()) {
            if (p.getUniqueId().toString().equals(uuid)) {
                return settings.getCache().getCooldowns().get(p).getWhenKilled();
            }
        }
        return null;
    }

    public boolean removePlayerCooldown(String UUID) {
        boolean success = false;
        Connection connection = new SQLConnect(settings).getConnection();
        if (connection != null) {
            try {
                if (!cooldownExists(UUID)) {
                    success = false;
                }
                PreparedStatement statement = connection.prepareStatement("DELETE FROM BountyR_Cooldown WHERE PlayerUUID='" + UUID + "'");
                statement.executeUpdate();
                connection.close();
                success = true;
            } catch (SQLException e) {
                settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Error removing a cooldown for a player." + Ansi.ansi().fg(Ansi.Color.WHITE));
            }
        }
        return success;
    }

    public boolean cooldownExists(String UUID) {
        for (Player p : settings.getCache().getCooldowns().keySet()) {
            if (p.getUniqueId().toString().equals(UUID)) {
                return true;
            }
        }
        return false;
    }

    public boolean cooldownExistsDatabase(String UUID) {
        Connection connection = new SQLConnect(settings).getConnection();
        if (connection != null) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM BountyR_Cooldown WHERE PlayerUUID='" + UUID + "'");
                ResultSet set = preparedStatement.executeQuery();
                while (set.next()) {
                    preparedStatement.close();
                    connection.close();
                    return true;
                }
                connection.close();
            } catch (SQLException e) {
                settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Error checking if a cooldown exists." + Ansi.ansi().fg(Ansi.Color.WHITE));
                return false;
            }
        }
        return false;
    }

    private void refreshNetworkRemove(Bounty bounty) {
        for (Player p : settings.getCache().getBounties().keySet()) {
            if (settings.getCache().getBounties().get(p).equals(bounty)) {
                settings.getCache().getBounties().remove(p);
                break;
            }
        }
        try {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Forward");
            out.writeUTF("ALL");
            out.writeUTF("BountyRewards");
            ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
            DataOutputStream msgout = new DataOutputStream(msgbytes);
            msgout.writeLong(System.currentTimeMillis());
            msgout.writeUTF("Remove");
            msgout.writeUTF(bounty.getPlayerUUID());
            out.writeShort(msgbytes.toByteArray().length);
            out.write(msgbytes.toByteArray());
            ArrayList<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
            if (players.size() > 0) {
                players.get(0).sendPluginMessage(BountyRewards.settings.getPlugin(), "BungeeCord", out.toByteArray());
            }
        } catch (IOException e) {
            settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Error sending remove plugin message." + Ansi.ansi().fg(Ansi.Color.WHITE));
        }
    }

    private void refreshNetworkUpdate(Bounty bounty) {
        try {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Forward");
            out.writeUTF("ALL");
            out.writeUTF("BountyRewards");
            ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
            DataOutputStream msgout = new DataOutputStream(msgbytes);
            msgout.writeLong(System.currentTimeMillis());
            msgout.writeUTF("Update");
            msgout.writeUTF("Bounty");
            msgout.writeUTF(bounty.getPlayerUUID());
            msgout.writeInt(bounty.getAmount());
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
