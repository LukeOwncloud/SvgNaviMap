package de.tuhh.ti5.androidsvgnavimap.db.model;

/**
 * Created by ruibrito on 07/05/15.
 */
public class Location {

    private long locationID; // Primary Key
    private long vertex;
    private int floor;
    private String building;
    private String locationName;

    public Location() {
    }

    public Location(long vertex, int floor, String building, String locationName) {
        this.vertex = vertex;
        this.floor = floor;
        this.building = building;
        this.locationName = locationName;
    }

    public Location(long locationID, long vertex, int floor, String building, String locationName) {
        this.locationID = locationID;
        this.vertex = vertex;
        this.floor = floor;
        this.building = building;
        this.locationName = locationName;
    }

    public long getLocationID() {
        return locationID;
    }

    public void setLocationID(long locationID) {
        this.locationID = locationID;
    }

    public long getVertex() {
        return vertex;
    }

    public void setVertex(long vertex) {
        this.vertex = vertex;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
