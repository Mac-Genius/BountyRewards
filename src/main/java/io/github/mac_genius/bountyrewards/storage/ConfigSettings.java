package io.github.mac_genius.bountyrewards.storage;

/**
 * Created by Mac on 1/10/2016.
 */
public class ConfigSettings {
    private boolean cancelBounty;
    private int refund;
    private int minSet;
    private int maxSet;
    private int transFee;
    private int deathCost;
    private String deathCostMode;
    private boolean ranDeathCost;
    private int ranRange;
    private int cooldown;
    private int expiration;
    private boolean anon;
    private boolean bungeecord;

    public ConfigSettings(boolean cancelBounty, int refund, int minSet, int maxSet, int transFee, int deathCost,
                          String deathCostMode, boolean ranDeathCost, int ranRange, int cooldown, int expiration,
                          boolean anon, boolean bungeecord) {
        this.cancelBounty = cancelBounty;
        this.refund = refund;
        this.minSet = minSet;
        this.maxSet = maxSet;
        this.transFee = transFee;
        this.deathCost = deathCost;
        this.deathCostMode = deathCostMode;
        this.ranDeathCost = ranDeathCost;
        this.ranRange = ranRange;
        this.cooldown = cooldown;
        this.expiration = expiration;
        this.anon = anon;
        this.bungeecord = bungeecord;
    }

    public ConfigSettings() {}

    public boolean isCancelBounty() {
        return cancelBounty;
    }

    public int getRefund() {
        return refund;
    }

    public int getMinSet() {
        return minSet;
    }

    public int getMaxSet() {
        return maxSet;
    }

    public int getTransFee() {
        return transFee;
    }

    public int getDeathCost() {
        return deathCost;
    }

    public String getDeathCostMode() {
        return deathCostMode;
    }

    public boolean isRanDeathCost() {
        return ranDeathCost;
    }

    public int getRanRange() {
        return ranRange;
    }

    public int getCooldown() {
        return cooldown;
    }

    public int getExpiration() {
        return expiration;
    }

    public boolean isAnon() {
        return anon;
    }

    public boolean isBungeecord() {
        return bungeecord;
    }

    public void setCancelBounty(boolean cancelBounty) {
        this.cancelBounty = cancelBounty;
    }

    public void setRefund(int refund) {
        this.refund = refund;
    }

    public void setMinSet(int minSet) {
        this.minSet = minSet;
    }

    public void setMaxSet(int maxSet) {
        this.maxSet = maxSet;
    }

    public void setTransFee(int transFee) {
        this.transFee = transFee;
    }

    public void setDeathCost(int deathCost) {
        this.deathCost = deathCost;
    }

    public void setDeathCostMode(String deathCostMode) {
        this.deathCostMode = deathCostMode;
    }

    public void setRanDeathCost(boolean ranDeathCost) {
        this.ranDeathCost = ranDeathCost;
    }

    public void setRanRange(int ranRange) {
        this.ranRange = ranRange;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public void setExpiration(int expiration) {
        this.expiration = expiration;
    }

    public void setAnon(boolean anon) {
        this.anon = anon;
    }

    public void setBungeecord(boolean bungeecord) {
        this.bungeecord = bungeecord;
    }
}
