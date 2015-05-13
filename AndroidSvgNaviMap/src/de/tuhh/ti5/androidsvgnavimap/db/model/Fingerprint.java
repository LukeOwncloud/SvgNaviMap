package de.tuhh.ti5.androidsvgnavimap.db.model;

/**
 * Created by ruibrito on 07/05/15.
 */
public class Fingerprint {

    private int fingerPrintID; // Primary Key
    private String date;
    private String weather;
    private String barriers;
    private String electricDevices;
    private String people;
    private int locationID; // Foreign Key

    public Fingerprint() {
    }

    public Fingerprint(String date, String weather, String barriers, String electricDevices, String people, int locationID) {
        this.date = date;
        this.weather = weather;
        this.barriers = barriers;
        this.electricDevices = electricDevices;
        this.people = people;
        this.locationID = locationID;
    }

    public Fingerprint(int fingerPrintID, String date, String weather, String barriers, String electricDevices, String people, int locationID) {
        this.fingerPrintID = fingerPrintID;
        this.date = date;
        this.weather = weather;
        this.barriers = barriers;
        this.electricDevices = electricDevices;
        this.people = people;
        this.locationID = locationID;
    }

    public int getFingerPrintID() {
        return fingerPrintID;
    }

    public void setFingerPrintID(int fingerPrintID) {
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

    public int getLocationID() {
        return locationID;
    }

    public void setLocationID(int locationID) {
        this.locationID = locationID;
    }
}
