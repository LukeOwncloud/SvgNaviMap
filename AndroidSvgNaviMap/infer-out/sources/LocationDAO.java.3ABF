package de.tuhh.ti5.androidsvgnavimap.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import de.tuhh.ti5.androidsvgnavimap.db.DBHelper;
import de.tuhh.ti5.androidsvgnavimap.db.model.Location;

import java.sql.SQLException;

/**
 * Created by ruibrito on 20/05/15.
 */
public class LocationDAO {

    public static final String TAG = "LocationDAO";

    // DATABASE FIELDs
    private SQLiteDatabase sqLiteDatabase;
    private DBHelper dbHelper;
    private Context context;

    private String[] tableColumns = {DBHelper.LOC_ID, DBHelper.LOC_vertex,
            DBHelper.LOC_floor, DBHelper.LOC_building, DBHelper.LOC_alias};

    public LocationDAO(Context context) {
        dbHelper = new DBHelper(context);
        this.context = context;
        // open the database
        try {
            open();
        } catch (SQLException e) {
            Log.e(TAG, "SQLException on openning database " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void open() throws SQLException {
        sqLiteDatabase = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Location createLocation(String vertex, String floor, String building, String alias) {
        ContentValues values = new ContentValues();

        values.put(DBHelper.LOC_vertex, vertex);
        values.put(DBHelper.LOC_floor, floor);
        values.put(DBHelper.LOC_building, building);
        values.put(DBHelper.LOC_alias, alias);

        long insertID = sqLiteDatabase.insert(DBHelper.TABLE_LOCATION, null, values);

        Cursor cursor = sqLiteDatabase.query(
                DBHelper.TABLE_LOCATION,
                tableColumns,
                DBHelper.LOC_ID + " = " + insertID, null, null, null, null);

        cursor.moveToFirst();

        Location newLocation = cursorToLocation(cursor);

        cursor.close();

        return newLocation;
    }

    public Location getLocationID(String vertex, String floor, String building, String alias) {

        Cursor cursor = sqLiteDatabase.query(
                DBHelper.TABLE_LOCATION,
                tableColumns,
                DBHelper.LOC_vertex + " = ? AND " + DBHelper.LOC_floor + " = ? AND " + DBHelper.LOC_building + " = ?",
                new String[]{vertex, floor, building}, null, null, null);

        if (alias == null) {
            alias = String.valueOf(vertex);
        }

        if (cursor.moveToFirst()) {
            return cursorToLocation(cursor);
        } else {
            return createLocation(vertex, floor, building, alias);
        }
    }

    public Location getLocationByID(long id) {
        Cursor cursor = sqLiteDatabase.query(
                DBHelper.TABLE_LOCATION,
                tableColumns,
                DBHelper.LOC_ID + " = ?", new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        Location location = cursorToLocation(cursor);

        return location;
    }

    public void deleteAllLocations() {
        String sql = "DELETE FROM " + DBHelper.TABLE_LOCATION;
        sqLiteDatabase.execSQL(sql);
    }

    private Location cursorToLocation(Cursor cursor) {
        Location loc = new Location();

        loc.setLocationID(cursor.getLong(0));
        loc.setVertex(cursor.getString(1));
        loc.setFloor(cursor.getString(2));
        loc.setBuilding(cursor.getString(3));
        loc.setLocationName(cursor.getString(4));

        return loc;
    }
}
