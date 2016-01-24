package io.github.mac_genius.bountyrewards.storage.MySQL;

import java.sql.Timestamp;
import java.util.StringTokenizer;

/**
 * Created by Mac on 1/21/2016.
 */
public class Cooldown {
    private String uuid;
    private Timestamp whenKilled;
    private String name;

    public Cooldown(String uuid, Timestamp whenKilled, String name) {
        this.uuid = uuid;
        this.whenKilled = whenKilled;
        this.name = name;
    }

    public Cooldown(String cooldownFormat) {
        StringTokenizer splitter = new StringTokenizer(cooldownFormat, "|");
        this.uuid = splitter.nextToken();
        this.whenKilled = new Timestamp(Long.parseLong(splitter.nextToken()));
        this.name = splitter.nextToken();
    }

    public Cooldown() {}

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Timestamp getWhenKilled() {
        return whenKilled;
    }

    public void setWhenKilled(Timestamp whenKilled) {
        this.whenKilled = whenKilled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return uuid + "|" + whenKilled.getTime() + "|" + name;
    }
}
