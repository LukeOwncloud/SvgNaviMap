package de.tuhh.ti5.androidsvgnavimap.db;


/**
 * Created by ruibrito on 30/04/15.
 */

public class ScanConditionParam {

    private String scanBuilding;
    private String scanFloor;
    private String scanWeather;
    private String scanDevices;
    private String scanPeople;
    private String scanBarrier;

    public ScanConditionParam() {
        this.scanBuilding = "None";
        this.scanFloor = "None";
        this.scanWeather = "None";
        this.scanDevices = "None";
        this.scanPeople = "None";
        this.scanBarrier = "None";
    }

    public String getScanBuilding() {
        return scanBuilding;
    }

    public void setScanBuilding(String scanBuilding) {
        this.scanBuilding = scanBuilding;
    }

    public String getScanFloor() {
        return scanFloor;
    }

    public void setScanFloor(String scanFloor) {
        this.scanFloor = scanFloor;
    }

    public String getScanWeather() {
        return scanWeather;
    }

    public void setScanWeather(String scanWeather) {
        this.scanWeather = scanWeather;
    }

    public String getScanDevices() {
        return scanDevices;
    }

    public void setScanDevices(String scanDevices) {
        this.scanDevices = scanDevices;
    }

    public String getScanPeople() {
        return scanPeople;
    }

    public void setScanPeople(String scanPeople) {
        this.scanPeople = scanPeople;
    }

    public String getScanBarrier() {
        return scanBarrier;
    }

    public void setScanBarrier(String scanBarrier) {
        this.scanBarrier = scanBarrier;
    }
}
