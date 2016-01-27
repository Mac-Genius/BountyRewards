package io.github.mac_genius.bountyrewards.Utils;

/**
 * Created by Mac on 1/25/2016.
 */
public class Version {
    private String version;
    private String downloadLink;
    private boolean needsUpdate;

    public Version(String version, String downloadLink) {
        this.version = version;
        this.downloadLink = downloadLink;
    }

    public Version() {}

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    public boolean needsUpdate() {
        return needsUpdate;
    }

    public void setNeedsUpdate(boolean needsUpdate) {
        this.needsUpdate = needsUpdate;
    }
}
