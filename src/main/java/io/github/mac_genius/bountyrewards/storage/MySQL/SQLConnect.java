package io.github.mac_genius.bountyrewards.storage.MySQL;

import io.github.mac_genius.bountyrewards.BountyRewards;
import io.github.mac_genius.bountyrewards.PluginSettings;
import org.bukkit.Bukkit;
import org.fusesource.jansi.Ansi;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Mac on 1/4/2016.
 */
public class SQLConnect {
    private PluginSettings settings;

    public SQLConnect(PluginSettings settings) {
        this.settings = settings;
    }

    public void databaseSetup(Connection connection) {
        settings.getPlugin().getLogger().info("Loading tables...");
        for (String s : tables()) {
            try {
                PreparedStatement tokoins = connection.prepareStatement(s);
                tokoins.executeUpdate();
                tokoins.close();
            } catch (SQLException e) {
                settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Could not create the PermUsers table." + Ansi.ansi().fg(Ansi.Color.WHITE));
            }
        }
    }

    public Connection getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Could not find the connection driver. Make sure it is installed and try again." + Ansi.ansi().fg(Ansi.Color.WHITE));
            return null;
        }
        Connection con = null;

        String url = "jdbc:mysql://" + settings.getPlugin().getConfig().getString("ip") + ":" + settings.getPlugin().getConfig().getString("port") + "/" + settings.getPlugin().getConfig().getString("database");
        String user = settings.getPlugin().getConfig().getString("user");
        String password = settings.getPlugin().getConfig().getString("password");

        try {
            con = DriverManager.getConnection(url, user, password);

        } catch (SQLException ex) {
            settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Could not establish a connection to the database!" + Ansi.ansi().fg(Ansi.Color.WHITE));

        }
        return con;
    }

    public boolean testConnection() {
        Connection connection = getConnection();
        if (connection != null) {
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<String> tables() {
        ArrayList<String> tables = new ArrayList<>();
        tables.add("CREATE TABLE IF NOT EXISTS BountyR_DatabaseInfo(Id INT PRIMARY KEY AUTO_INCREMENT, Version VARCHAR(15))");
        tables.add("CREATE TABLE IF NOT EXISTS BountyR_Active(Id INT PRIMARY KEY AUTO_INCREMENT, BountyUUID VARCHAR(36), " +
                "SetterUUID VARCHAR(36), WhenSet TIMESTAMP, Amount INT, BountyName VARCHAR(25), SetterName VARCHAR(25))");
        tables.add("CREATE TABLE IF NOT EXISTS BountyR_Cooldown(Id INT PRIMARY KEY AUTO_INCREMENT, PlayerUUID VARCHAR(36), LastKilled TIMESTAMP, PlayerName VARCHAR(25))");
        tables.add("CREATE TABLE IF NOT EXISTS BountyR_Messages(Id INT PRIMARY KEY AUTO_INCREMENT, MessageType VARCHAR(30), BountyBroadcast TEXT)");
        tables.add("CREATE TABLE IF NOT EXISTS BountyR_Settings(Id INT PRIMARY KEY AUTO_INCREMENT, CancelBounty BOOLEAN, Refund INT, " +
                "MinSet INT, MaxSet INT, TransFee INT, DeathCost INT, DeathCostMode VARCHAR(15), RanDeathCost BOOLEAN, " +
                "RanRange INT, Cooldown INT, Expiration INT, Anon BOOLEAN, BungeeCord BOOLEAN)");
        return tables;
    }

    public void firstTimeSetup() {
        Connection connection = getConnection();
        if (connection != null) {
            try {
                DatabaseMetaData data = connection.getMetaData();
                ResultSet set = data.getTables(null, null, "BountyR_DatabaseInfo", new String[] {"Table"});
                boolean exists = false;
                while (set.next()) {
                    exists = true;
                }
                if (!exists) {
                    settings.getPlugin().getLogger().info("Running first time setup on the database...");
                    databaseSetup(connection);
                    populateTables(connection);
                    settings.getPlugin().getLogger().info("Done.");
                }
            } catch (SQLException e) {
                settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Error setting up the database." + Ansi.ansi().fg(Ansi.Color.WHITE));
            }
            try {
                connection.close();
            } catch (SQLException e) {
                settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Could not close the connection." + Ansi.ansi().fg(Ansi.Color.WHITE));
            }
        }
    }

    private void populateTables(Connection connection) {
        settings.getPlugin().getLogger().info("Populating tables with information from the config...");
        ArrayList<String> tableData = new ArrayList<>();
        tableData.add("INSERT INTO BountyR_DatabaseInfo (Version) VALUES(" + settings.getPlugin().getDescription().getVersion() + ")");
        for (String s : settings.getMessageNames()) {
            tableData.add("INSERT INTO BountyR_Messages (MessageType, BountyBroadcast) VALUES('" + s
                    + "','" + settings.getPlugin().getConfig().getString(s) + "')");
        }
        tableData.add("INSERT INTO BountyR_Settings (CancelBounty,Refund,MinSet," +
                "MaxSet,TransFee,DeathCost,DeathCostMode,RanDeathCost,RanRange,Cooldown,Expiration,Anon,BungeeCord) VALUES("
                + settings.getPlugin().getConfig().getBoolean("CancelBounty") + "," + settings.getPlugin().getConfig().getInt("Refund") + ","
                + settings.getPlugin().getConfig().getInt("Min") + "," + settings.getPlugin().getConfig().getInt("Max") + ","
                + settings.getPlugin().getConfig().getInt("TransactionFee") + "," + settings.getPlugin().getConfig().getInt("DeathCost") + ",'"
                + settings.getPlugin().getConfig().getString("DeathCostMode") + "'," + settings.getPlugin().getConfig().getBoolean("RandomizeDeathCost") + ","
                + settings.getPlugin().getConfig().getInt("Range") + "," + settings.getPlugin().getConfig().getInt("Cooldown") + ","
                + settings.getPlugin().getConfig().getInt("Expiration") + "," + settings.getPlugin().getConfig().getBoolean("Anonymous") + ","
                + settings.getPlugin().getConfig().getBoolean("BungeeCord") + ")");
        try {
            for (String s : tableData) {
                PreparedStatement statement = connection.prepareStatement(s);
                statement.executeUpdate();
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Could not populate the tables." + Ansi.ansi().fg(Ansi.Color.WHITE));
        }
    }

    public void updateDatabase() {
        Connection connection = getConnection();
        if (connection != null) {
            double version = getDatabaseVersion(connection);
            settings.getPlugin().getLogger().info(Ansi.ansi().fg(Ansi.Color.GREEN) + "Database is up to date." + Ansi.ansi().fg(Ansi.Color.WHITE));
            try {
                connection.close();
            } catch (SQLException e) {
                settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Could not close the connection." + Ansi.ansi().fg(Ansi.Color.WHITE));
            }
        }
    }

    private double getDatabaseVersion(Connection connection) {
        double version = -1.0;
        try {
            PreparedStatement tokoins = connection.prepareStatement("SELECT * FROM BountyR_DatabaseInfo WHERE Id='1'");
            ResultSet query = tokoins.executeQuery();
            while (query.next()) {
                version = Double.parseDouble(query.getString(2));
            }
            tokoins.close();
            return version;
        } catch (SQLException e) {
            //settings.getPlugin().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Nothing to update." + Ansi.ansi().fg(Ansi.Color.WHITE));
            return version;
        }
    }
}