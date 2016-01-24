package io.github.mac_genius.bountyrewards.storage;

import java.sql.Timestamp;
import java.util.StringTokenizer;

/**
 * Created by Mac on 1/18/2016.
 */
public class Bounty {
    private String playerUUID;
    private String playerName;
    private String setterUUID;
    private String setterName;
    private Timestamp whenSet;
    private int amount;

    public Bounty(String playerUUID, String setterUUID, Timestamp whenSet, int amount, String playerName, String setterName) {
        this.playerUUID = playerUUID;
        this.setterUUID = setterUUID;
        this.whenSet = whenSet;
        this.amount = amount;
        this.playerName = playerName;
        this.setterName = setterName;
    }

    public Bounty(String bountyFormat) {
        StringTokenizer split = new StringTokenizer(bountyFormat, "|");
        this.playerUUID = split.nextToken();
        this.setterUUID = split.nextToken();
        this.whenSet = new Timestamp(Long.parseLong(split.nextToken()));
        this.amount = Integer.parseInt(split.nextToken());
        this.playerName = split.nextToken();
        this.setterName = split.nextToken();
    }

    public Bounty() {}

    public String getPlayerUUID() {
        return playerUUID;
    }

    public void setPlayerUUID(String playerUUID) {
        this.playerUUID = playerUUID;
    }

    public String getSetterUUID() {
        return setterUUID;
    }

    public void setSetterUUID(String setterUUID) {
        this.setterUUID = setterUUID;
    }

    public Timestamp getWhenSet() {
        return whenSet;
    }

    public void setWhenSet(Timestamp whenSet) {
        this.whenSet = whenSet;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String toString() {
        return playerUUID + "|"
                + setterUUID + "|"
                + whenSet.getTime()
                + "|" + amount + "|"
                + playerName + "|"
                + setterName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getSetterName() {
        return setterName;
    }

    public void setSetterName(String setterName) {
        this.setterName = setterName;
    }
}
