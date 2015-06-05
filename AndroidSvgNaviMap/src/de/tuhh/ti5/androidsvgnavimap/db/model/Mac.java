package de.tuhh.ti5.androidsvgnavimap.db.model;

/**
 * Created by ruibrito on 13/05/15.
 */
public class Mac {

    private long macID;
    private String macSSID;
    private String macBSSID;
    private int strength;
    private int channel;
    private Fingerprint fingerprint;

    public Mac() {
    }

    public Mac(String macSSID, String macBSSID, int strength, int channel, Fingerprint fingerprint) {
        this.macSSID = macSSID;
        this.macBSSID = macBSSID;
        this.strength = strength;
        this.channel = channel;
        this.fingerprint = fingerprint;
    }

    public Mac(long macID, String macSSID, String macBSSID, int strength, int channel, Fingerprint fingerprint) {
        this.macID = macID;
        this.macSSID = macSSID;
        this.macBSSID = macBSSID;
        this.strength = strength;
        this.channel = channel;
        this.fingerprint = fingerprint;
    }

    public long getMacID() {
        return macID;
    }

    public void setMacID(long macID) {
        this.macID = macID;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public Fingerprint getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(Fingerprint fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String getMacSSID() {
        return macSSID;
    }

    public void setMacSSID(String macSSID) {
        this.macSSID = macSSID;
    }

    public String getMacBSSID() {
        return macBSSID;
    }

    public void setMacBSSID(String macBSSID) {
        this.macBSSID = macBSSID;
    }
}
