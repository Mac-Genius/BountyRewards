package io.github.mac_genius.bountyrewards.Utils;

import io.github.mac_genius.bountyrewards.PluginSettings;

import java.io.*;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Created by Mac on 1/26/2016.
 */
public class UpdateConfig {
    private PluginSettings settings;
    private String config = "";
    private File mainConfig;

    public UpdateConfig(PluginSettings settings) {
        this.settings = settings;
    }

    public void updateConfig() {
        if (settings.getPlugin().getDataFolder().exists()) {
            File dataFolder = settings.getPlugin().getDataFolder();
            File configFile = new File(dataFolder, "config.yml");
            if (configFile.exists()) {
                mainConfig = configFile;
                try {
                    BufferedReader input = new BufferedReader(new FileReader(configFile));
                    while (input.ready()) {
                        config += input.readLine() + "\n";
                    }
                    if (!config.equals("")) {
                        versionChecker();
                    }
                } catch (IOException e) {}
            }
        }
    }

    private void versionChecker() {
        if (config.contains("ConfigVersion:")) {
            String version = "";
            int start = config.indexOf("ConfigVersion: ") + 15;
            char startChar = config.charAt(start);
            while (startChar != ' ' && startChar != '\n') {
                version += config.charAt(start);
                start++;
                startChar = config.charAt(start);
            }
            if (version.equals("1.3")) {
                settings.getPlugin().getLogger().info("Config.yml is up to date. (v.1.3)");
                if (!config.contains("UpdateChecking:")) {
                    spigot1_3();
                }
            }
        } else if (config.contains("BungeeCord:")) {
            settings.getPlugin().getLogger().info("Your config.yml is outdated (v.1.2). Updating now...");
            config1_2();
            settings.getPlugin().getLogger().info("Finished updating the config.yml");
        } else {
            settings.getPlugin().getLogger().info("Your config is extremely outdated and cannot be automatically updated. " +
                    "Please rename config.yml and bountydata.yml or remove them so that new ones can be generated.");
        }
    }

    private void spigot1_3() {
        String newConfig = "";
        Scanner splitter = new Scanner(config);
        while (splitter.hasNext()) {
            String current = splitter.nextLine();
            if (current.contains("ConfigVersion:")) {
                newConfig += current + "\n";
                newConfig += "\n";
                newConfig += "# UpdateChecking allows the plugin to check for a new version\n";
                newConfig += "UpdateChecking: true\n";
                newConfig += "\n";
            } else {
                newConfig += current + "\n";
            }
        }
        config = newConfig;
        try {
            FileWriter writer = new FileWriter(mainConfig, false);
            Scanner scan = new Scanner(config);
            while (scan.hasNext()) {
                writer.write(scan.nextLine() + "\n");
                writer.flush();
            }
            writer.close();
        } catch (IOException e) {
        }
    }
    private void config1_2() {
        String newConfig = "";
        Scanner splitter = new Scanner(config);
        while (splitter.hasNext()) {
            String current = splitter.nextLine();
            if (current.equals("############################################################")) {
                String header1 = splitter.nextLine();
                String header2 = splitter.nextLine();
                String header3 = splitter.nextLine();
                String header4 = splitter.nextLine();
                if (header2.contains("Messages")) {
                    newConfig += "# The version of the config. DO NOT CHANGE\n";
                    newConfig += "ConfigVersion: 1.3\n";
                    newConfig += "\n";
                    newConfig += "# UpdateChecking allows the plugin to check for a new version\n";
                    newConfig += "UpdateChecking: true\n";
                    newConfig += "\n";
                }
                newConfig += current + "\n";
                newConfig += header1 + "\n";
                newConfig += header2 + "\n";
                newConfig += header3 + "\n";
                newConfig += header4 + "\n";
            } else {
                newConfig += current + "\n";
            }
        }
        config = newConfig;
        try {
            FileWriter writer = new FileWriter(mainConfig, false);
            Scanner scan = new Scanner(config);
            while (scan.hasNext()) {
                writer.write(scan.nextLine() + "\n");
                writer.flush();
            }
            writer.close();
        } catch (IOException e) {
        }
    }

    private void printConfig() {
        StringTokenizer splitter = new StringTokenizer(config, "\n");
        while (splitter.hasMoreTokens()) {
            settings.getPlugin().getLogger().info(splitter.nextToken());
        }
    }
}
