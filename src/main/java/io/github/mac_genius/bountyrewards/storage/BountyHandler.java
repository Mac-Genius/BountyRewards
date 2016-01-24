package io.github.mac_genius.bountyrewards.storage;

import io.github.mac_genius.bountyrewards.PluginSettings;
import io.github.mac_genius.bountyrewards.storage.MySQL.MySQLBounty;
import io.github.mac_genius.bountyrewards.storage.local.LocalBounty;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by Mac on 1/18/2016.
 */
public class BountyHandler {
    private PluginSettings settings;

    public BountyHandler(PluginSettings settings) {
        this.settings = settings;
    }

    public boolean addBounty(Bounty bounty) {
        if (settings.isUsingMySQL()) {
            return new MySQLBounty(settings).addBounty(bounty);
        } else {
            return new LocalBounty(settings).addBounty(bounty);
        }
    }

    public boolean updateBounty(Bounty bounty) {
        if (settings.isUsingMySQL()) {
            return new MySQLBounty(settings).updateBounty(bounty);
        } else {
            return new LocalBounty(settings).updateBounty(bounty);
        }
    }

    public Bounty getBounty(String UUID) {
        if (settings.isUsingMySQL()) {
            return new MySQLBounty(settings).getBounty(UUID);
        } else {
            return new LocalBounty(settings).getBounty(UUID);
        }
    }

    public Bounty getBountyByName(String name) {
        if (settings.isUsingMySQL()) {
            return new MySQLBounty(settings).getBountyByName(name);
        } else {
            return new LocalBounty(settings).getBountyByName(name);
        }
    }

    public ArrayList<Bounty> getPlayerBounties(String UUID) {
        if (settings.isUsingMySQL()) {
            return new MySQLBounty(settings).getPlayerBounties(UUID);
        } else {
            return new LocalBounty(settings).getPlayerBounties(UUID);
        }
    }

    public ArrayList<Bounty> getAllBounties() {
        if (settings.isUsingMySQL()) {
            return new MySQLBounty(settings).getAllBounties();
        } else {
            return new LocalBounty(settings).getAllBounties();
        }
    }

    public boolean removeBounty(Bounty bounty) {
        if (settings.isUsingMySQL()) {
            return new MySQLBounty(settings).removeBounty(bounty);
        } else {
            return new LocalBounty(settings).removeBounty(bounty);
        }
    }

    public boolean bountyExists(Bounty bounty) {
        if (settings.isUsingMySQL()) {
            return new MySQLBounty(settings).bountyExists(bounty);
        } else {
            return new LocalBounty(settings).bountyExists(bounty);
        }
    }

    public boolean bountyExists(String uuid) {
        if (settings.isUsingMySQL()) {
            return new MySQLBounty(settings).bountyExists(uuid);
        } else {
            return new LocalBounty(settings).bountyExists(uuid);
        }
    }

    public boolean addPlayerCooldown(String UUID, String name) {
        if (settings.isUsingMySQL()) {
            return new MySQLBounty(settings).addPlayerCooldown(UUID, name);
        } else {
            return new LocalBounty(settings).addPlayerCooldown(UUID, name);
        }
    }

    public Timestamp getCooldown(String UUID) {
        if (settings.isUsingMySQL()) {
            return new MySQLBounty(settings).getCooldown(UUID);
        } else {
            return new LocalBounty(settings).getCooldown(UUID);
        }
    }

    public boolean removePlayerCooldown(String UUID) {
        if (settings.isUsingMySQL()) {
            return new MySQLBounty(settings).removePlayerCooldown(UUID);
        } else {
            return new LocalBounty(settings).removePlayerCooldown(UUID);
        }
    }

    public boolean cooldownExists(String UUID) {
        if (settings.isUsingMySQL()) {
            return new MySQLBounty(settings).cooldownExists(UUID);
        } else {
            return new LocalBounty(settings).cooldownExists(UUID);
        }
    }
}
