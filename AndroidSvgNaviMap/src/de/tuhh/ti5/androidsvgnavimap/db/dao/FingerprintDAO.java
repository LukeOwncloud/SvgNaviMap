package de.tuhh.ti5.androidsvgnavimap.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import de.tuhh.ti5.androidsvgnavimap.db.DBHelper;
import de.tuhh.ti5.androidsvgnavimap.db.model.Fingerprint;
import de.tuhh.ti5.androidsvgnavimap.db.model.Location;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by ruibrito on 15/05/15.
 */
public class FingerprintDAO {

    public static final String TAG = "FingerprintDAO";

    // DATABASE FIELDs
    private SQLiteDatabase sqLiteDatabase;
    private DBHelper dbHelper;
    private Context context;

    private String[] tableCollumns = {
            DBHelper.FP_ID, DBHelper.FP_date,
            DBHelper.FP_weather, DBHelper.FP_barrier,
            DBHelper.FP_devices, DBHelper.FP_people, DBHelper.FP_location};

    public FingerprintDAO(Context context) {
        this.context = context;

        dbHelper = new DBHelper(context);
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

    public Fingerprint createFingerprint(String date, String weather, String barriers, String devices, String people, long locationID) {
        ContentValues values = new ContentValues();

        if (date == null) {
            values.put(DBHelper.FP_date, getCurrentDate());
        } else {
            values.put(DBHelper.FP_date, date);
        }
        values.put(DBHelper.FP_weather, weather);
        values.put(DBHelper.FP_barrier, barriers);
        values.put(DBHelper.FP_devices, devices);
        values.put(DBHelper.FP_people, people);
        values.put(DBHelper.FP_location, locationID);

        long insertedID = sqLiteDatabase.insert(DBHelper.TABLE_FINGERPRINT, null, values);

        Cursor cursor = sqLiteDatabase.query(DBHelper.TABLE_FINGERPRINT, tableCollumns,
                DBHelper.FP_ID + " = " + insertedID, null, null, null, null);

        cursor.moveToFirst();

        Fingerprint newFingerprint = cursorToFingerprint(cursor);

        cursor.close();

        return newFingerprint;
    }

    public Fingerprint getFingerprintByID(long id) {
        Cursor cursor = sqLiteDatabase.query(
                DBHelper.TABLE_LOCATION,
                tableCollumns,
                DBHelper.FP_ID + " = ?", new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        Fingerprint fingerprint = cursorToFingerprint(cursor);

        return fingerprint;
    }

    private Fingerprint cursorToFingerprint(Cursor cursor) {

        Fingerprint fp = new Fingerprint();

        fp.setFingerPrintID(cursor.getLong(0));
        fp.setDate(cursor.getString(1));
        fp.setWeather(cursor.getString(2));
        fp.setBarriers(cursor.getString(3));
        fp.setElectricDevices(cursor.getString(4));
        fp.setPeople(cursor.getString(5));

        long locationID = cursor.getLong(6);

        LocationDAO locationDAO = new LocationDAO(context);

        Location location = locationDAO.getLocationByID(locationID);

        if (location != null) {
            fp.setLocationID(location);
        }

        return fp;
    }

    private String getCurrentDate() {
        Calendar rightNow = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = dateFormat.format(rightNow.getTime());

        return formattedDate;
    }


}
