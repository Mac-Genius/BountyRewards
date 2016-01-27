package io.github.mac_genius.bountyrewards.Utils;

import io.github.mac_genius.bountyrewards.PluginSettings;

/**
 * Created by Mac on 1/26/2016.
 */
public class UpdateRun implements Runnable {
    private PluginSettings settings;

    public UpdateRun(PluginSettings settings) {
        this.settings = settings;
    }

    @Override
    public void run() {
        settings.getPlugin().getLogger().info("Checking for updates...");
        Version latestVersion = new UpdateChecker().getVersion();
        if (latestVersion.getVersion() != null) {
            if (settings.getPlugin().getDescription().getVersion().equals(latestVersion.getVersion())) {
                settings.getPlugin().getLogger().info("BountyRewards is up to date.");
                latestVersion.setNeedsUpdate(false);
            } else {
                latestVersion.setNeedsUpdate(true);
                settings.getPlugin().getLogger().info("There is a new version of BountyRewards available! (v." + latestVersion.getVersion() + ")");
                settings.getPlugin().getLogger().info("You can download the latest version here: " + latestVersion.getDownloadLink() + " or you can download it from the Spigot page.");
            }
        }
    }
}
