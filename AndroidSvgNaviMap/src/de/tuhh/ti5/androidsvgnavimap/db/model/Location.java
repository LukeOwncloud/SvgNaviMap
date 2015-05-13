package de.tuhh.ti5.androidsvgnavimap.db.model;

/**
 * Created by ruibrito on 07/05/15.
 */
public class Location {

    private int locationID; // Primary Key
    private int vertex;
    private int floor;
    private String building;
    private String locationName;

    public Location() {
    }

    public Location(int vertex, int floor, String building, String locationName) {
        this.vertex = vertex;
        this.floor = floor;
        this.building = building;
        this.locationName = locationName;
    }

    public Location(int locationID, int vertex, int floor, String building, String locationName) {
        this.locationID = locationID;
        this.vertex = vertex;
        this.floor = floor;
        this.building = building;
        this.locationName = locationName;
    }
}
