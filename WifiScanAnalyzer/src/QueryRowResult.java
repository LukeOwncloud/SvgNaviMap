/**
 * Created by ruibrito on 04/06/15.
 */
public class QueryRowResult {

    private int fingerprint;
    private String vertex;
    private String bssid;
    private int strength;

    public QueryRowResult() {
    }

    public QueryRowResult(int fingerprint, String vertex, String bssid, int strength) {
        this.fingerprint = fingerprint;
        this.vertex = vertex;
        this.bssid = bssid;
        this.strength = strength;
    }

    public int getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(int fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String getVertex() {
        return vertex;
    }

    public void setVertex(String vertex) {
        this.vertex = vertex;
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }
}

