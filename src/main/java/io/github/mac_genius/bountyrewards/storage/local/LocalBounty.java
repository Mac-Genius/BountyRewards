package io.github.mac_genius.bountyrewards.storage.local;

import io.github.mac_genius.bountyrewards.PluginSettings;
import io.github.mac_genius.bountyrewards.storage.Bounty;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by Mac on 1/18/2016.
 */
public class LocalBounty {
    private PluginSettings settings;

    public LocalBounty(PluginSettings settings) {
        this.settings = settings;
    }

    public boolean addBounty(Bounty bounty) {
        if (!bountyExists(bounty)) {
            ArrayList<String> list = new ArrayList<>(settings.getBountyData().getConfig().getStringList("CurrentBounties"));
            list.add(bounty.toString());
            settings.getBountyData().getConfig().set("CurrentBounties", list);
            settings.getBountyData().saveConfig();
            return true;
        }
        return false;
    }

    public boolean updateBounty(Bounty bounty) {
        if (bountyExists(bounty)) {
            ArrayList<String> list = new ArrayList<>(settings.getBountyData().getConfig().getStringList("CurrentBounties"));
            for (int i = 0; i < list.size(); i++) {
                if (bounty.getPlayerUUID().equals(new Bounty(list.get(i)).getPlayerUUID())) {
                    list.set(i, bounty.toString());
                    break;
                }
            }
            settings.getBountyData().getConfig().set("CurrentBounties", list);
            settings.getBountyData().saveConfig();
            return true;
        }
        return false;
    }

    public Bounty getBounty(String UUID) {
        for (String s : settings.getBountyData().getConfig().getStringList("CurrentBounties")) {
            Bounty bounty = new Bounty(s);
            if (bounty.getPlayerUUID().equals(UUID)) {
                return bounty;
            }
        }
        return null;
    }

    public Bounty getBountyByName(String name) {
        for (String s : settings.getBountyData().getConfig().getStringList("CurrentBounties")) {
            Bounty bounty = new Bounty(s);
            if (bounty.getPlayerName().equals(name)) {
                return bounty;
            }
        }
        return null;
    }

    public ArrayList<Bounty> getPlayerBounties(String UUID) {
        ArrayList<Bounty> list = new ArrayList<>();
        for (String s : settings.getBountyData().getConfig().getStringList("CurrentBounties")) {
            Bounty bounty = new Bounty(s);
            if (bounty.getSetterUUID().equalsIgnoreCase(UUID)) {
                list.add(bounty);
            }
        }
        return list;
    }

    public ArrayList<Bounty> getAllBounties() {
        ArrayList<Bounty> bounties = new ArrayList<>();
        for (String s : settings.getBountyData().getConfig().getStringList("CurrentBounties")) {
            bounties.add(new Bounty(s));
        }
        return bounties;
    }

    public boolean removeBounty(Bounty bounty) {
        if (bountyExists(bounty)) {
            ArrayList<String> list = new ArrayList<>(settings.getBountyData().getConfig().getStringList("CurrentBounties"));
            for (String s : list) {
                if (s.equals(bounty.toString())) {
                    list.remove(s);
                    break;
                }
            }
            settings.getBountyData().getConfig().set("CurrentBounties", list);
            settings.getBountyData().saveConfig();
            return true;
        }
        return false;
    }

    public boolean bountyExists(Bounty bounty) {
        for (String s : settings.getBountyData().getConfig().getStringList("CurrentBounties")) {
            Bounty b = new Bounty(s);
            if (b.getPlayerUUID().equalsIgnoreCase(bounty.getPlayerUUID())) {
                return true;
            }
        }
        return false;
    }

    public boolean bountyExists(String uuid) {
        for (String s : settings.getBountyData().getConfig().getStringList("CurrentBounties")) {
            Bounty b = new Bounty(s);
            if (b.getPlayerUUID().equalsIgnoreCase(uuid)) {
                return true;
            }
        }
        return false;
    }

    public boolean addPlayerCooldown(String UUID, String name) {
        if (!cooldownExists(UUID)) {
            ArrayList<String> list = new ArrayList<>(settings.getBountyData().getConfig().getStringList("Cooldown"));
            list.add(UUID + "|" + System.currentTimeMillis() + "|" + name);
            settings.getBountyData().getConfig().set("Cooldown", list);
            settings.getBountyData().saveConfig();
            return true;
        }
        return false;
    }

    public Timestamp getCooldown(String UUID) {
        if (cooldownExists(UUID)) {
            ArrayList<String> list = new ArrayList<>(settings.getBountyData().getConfig().getStringList("Cooldown"));
            for (String s : list) {
                StringTokenizer split = new StringTokenizer(s, "|");
                String uuid = split.nextToken();
                if (uuid.equals(UUID)) {
                    return new Timestamp(Long.parseLong(split.nextToken()));
                }
            }
        }
        return null;
    }

    public boolean removePlayerCooldown(String UUID) {
        if (cooldownExists(UUID)) {
            ArrayList<String> list = new ArrayList<>(settings.getBountyData().getConfig().getStringList("Cooldown"));
            for (String s : list) {
                StringTokenizer split = new StringTokenizer(s, "|");
                String uuid = split.nextToken();
                if (uuid.equals(UUID)) {
                    list.remove(s);
                    break;
                }
            }
            settings.getBountyData().getConfig().set("Cooldown", list);
            settings.getBountyData().saveConfig();
            return true;
        }
        return false;
    }

    public boolean cooldownExists(String UUID) {
        ArrayList<String> list = new ArrayList<>(settings.getBountyData().getConfig().getStringList("Cooldown"));
        for (String s : list) {
            StringTokenizer split = new StringTokenizer(s, "|");
            String uuid = split.nextToken();
            if (uuid.equals(UUID)) {
                return true;
            }
        }
        return false;
    }
}
