package de.tuhh.ti5.androidsvgnavimap.db.model;

/**
 * Created by ruibrito on 13/05/15.
 */
public class Mac {

    private long macID;
    private long strength;
    private long channel;
    private Fingerprint fingerprint;

    public Mac() {
    }

    public Mac(long strength, long channel, Fingerprint fingerprint) {
        this.strength = strength;
        this.channel = channel;
        this.fingerprint = fingerprint;
    }

    public Mac(long macID, long strength, long channel, Fingerprint fingerprint) {
        this.macID = macID;
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

    public long getStrength() {
        return strength;
    }

    public void setStrength(long strength) {
        this.strength = strength;
    }

    public long getChannel() {
        return channel;
    }

    public void setChannel(long channel) {
        this.channel = channel;
    }

    public Fingerprint getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(Fingerprint fingerprint) {
        this.fingerprint = fingerprint;
    }
}
