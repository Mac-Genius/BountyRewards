package io.github.mac_genius.bountyrewards.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Mac on 1/25/2016.
 */
public class UpdateChecker {
    public Version getVersion() {
        Version version = new Version();
        try {
            String page = "";
            URL url = new URL("http://dev.bukkit.org/bukkit-plugins/bountyrewards/files.rss");
            URLConnection connection = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while (reader.ready()) {
                String current = reader.readLine();
                page += current;
            }
            if (!page.equals("")) {
                String item = latestItem(page);
                version.setVersion(parseVersion(getTitle(item)));
                version.setDownloadLink(getDownloadLink(item));
            }
            reader.close();
        } catch (IOException e) {}
        return version;
    }

    private String latestItem(String page) {
        try {
            return page.substring(page.indexOf("<item>"), page.indexOf("</item>"));
        } catch (StringIndexOutOfBoundsException e) {}
        return "";
    }

    private String getTitle(String item) {
        try {
            return item.substring(item.indexOf("<title>") + 7, item.indexOf("</title>"));
        } catch (StringIndexOutOfBoundsException e) {}
        return "";
    }

    private String parseVersion(String title) {
        try {
            String version = "";
            int start = title.indexOf("v.") + 2;
            char current = title.charAt(start);
            while (current != ' ') {
                version += current;
                start++;
                current = title.charAt(start);
            }
            return version;
        } catch (StringIndexOutOfBoundsException e) {}
        return "";
    }

    private String getDownloadLink(String item) {
        try {
            return item.substring(item.indexOf("<link>") + 6, item.indexOf("</link>"));
        } catch (StringIndexOutOfBoundsException e) {}
        return "";
    }
}
