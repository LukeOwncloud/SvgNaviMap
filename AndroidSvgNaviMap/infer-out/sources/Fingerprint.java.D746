package de.tuhh.ti5.androidsvgnavimap.db.model;

/**
 * Created by ruibrito on 07/05/15.
 */
public class Fingerprint {

    private long fingerPrintID; // Primary Key
    private String date;
    private String weather;
    private String barriers;
    private String electricDevices;
    private String people;
    private Location location; // Foreign Key

    public Fingerprint() {
    }

    public Fingerprint(String date, String weather, String barriers, String electricDevices, String people, Location location) {
        this.date = date;
        this.weather = weather;
        this.barriers = barriers;
        this.electricDevices = electricDevices;
        this.people = people;
        this.location = location;
    }

    public Fingerprint(long fingerPrintID, String date, String weather, String barriers, String electricDevices, String people, long locationID) {
        this.fingerPrintID = fingerPrintID;
        this.date = date;
        this.weather = weather;
        this.barriers = barriers;
        this.electricDevices = electricDevices;
        this.people = people;
        this.location = location;
    }

    public long getFingerPrintID() {
        return fingerPrintID;
    }

    public void setFingerPrintID(long fingerPrintID) {
        this.fingerPrintID = fingerPrintID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getBarriers() {
        return barriers;
    }

    public void setBarriers(String barriers) {
        this.barriers = barriers;
    }

    public String getElectricDevices() {
        return electricDevices;
    }

    public void setElectricDevices(String electricDevices) {
        this.electricDevices = electricDevices;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocationID(Location location) {
        this.location = location;
    }
}
