package de.tuhh.ti5.androidsvgnavimap.db;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by ruibrito on 14/05/15.
 */
public class DBHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String TAG = "DBHelper";

    // Database version
    private static final int DATABASE_VERSION = 2;

    // Database name
    public static final String DATABASE_NAME = "fingerprintsDB";

    // Table names
    public static final String TABLE_FINGERPRINT = "fingerprint";
    public static final String TABLE_LOCATION = "location";
    public static final String TABLE_MAC = "mac";

    // FINGERPRINT Table
    public static final String FP_ID = "fp_ID";
    public static final String FP_date = "date";
    public static final String FP_weather = "weather";
    public static final String FP_barrier = "barrier";
    public static final String FP_devices = "devices";
    public static final String FP_people = "people";
    public static final String FP_location = "fp_Location";

    // LOCATION Table
    public static final String LOC_ID = "loc_ID";
    public static final String LOC_vertex = "vertex";
    public static final String LOC_floor = "floor";
    public static final String LOC_building = "building";
    public static final String LOC_alias = "alias";


    // MAC Table
    public static final String MAC_ID = "mac_ID";
    public static final String MAC_SSID = "ssid";
    public static final String MAC_BSSID = "bssid";
    public static final String MAC_strength = "strength";
    public static final String MAC_channel = "channel";
    public static final String MAC_fp = "mac_FP";


    public static final String CREATE_LOCATION_TABLE =
            "CREATE TABLE " + TABLE_LOCATION + "("
                    + LOC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + LOC_vertex + " TEXT, "
                    + LOC_floor + " TEXT, "
                    + LOC_building + " TEXT, "
                    + LOC_alias + " TEXT "
                    + ");";

    public static final String CREATE_FINGERPRINT_TABLE =
            "CREATE TABLE " + TABLE_FINGERPRINT + "("
                    + FP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + FP_date + " TEXT, "
                    + FP_weather + " TEXT, "
                    + FP_barrier + " TEXT, "
                    + FP_devices + " TEXT, "
                    + FP_people + " TEXT, "
                    + FP_location + " TEXT, "
                    + "FOREIGN KEY(" + FP_location + ") REFERENCES "
                    + TABLE_LOCATION + " (" + LOC_ID + ")"
                    + ");";

    //    + FP_location + " TEXT " + "REFERENCES " + TABLE_LOCATION + " (" + LOC_ID + ")";

    public static final String CREATE_MAC_TABLE =
            "CREATE TABLE " + TABLE_MAC + "("
                    + MAC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + MAC_SSID + " TEXT, "
                    + MAC_BSSID + " TEXT, "
                    + MAC_strength + " TEXT, "
                    + MAC_channel + " INTEGER, "
                    + MAC_fp + " TEXT, "
                    + "FOREIGN KEY(" + MAC_fp + ") REFERENCES "
                    + TABLE_FINGERPRINT + " (" + FP_ID + ")"
                    + ");";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_LOCATION_TABLE);
        db.execSQL(CREATE_FINGERPRINT_TABLE);
        db.execSQL(CREATE_MAC_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MAC);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FINGERPRINT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION);
        onCreate(db);
    }

    public ArrayList<Cursor> getData(String Query) {
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[]{"mesage"};
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2 = new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try {
            String maxQuery = Query;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);

            //add value to cursor2
            Cursor2.addRow(new Object[]{"Success"});

            alc.set(1, Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0, c);
                c.moveToFirst();

                return alc;
            }
            return alc;
        } catch (SQLException sqlEx) {
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[]{"" + sqlEx.getMessage()});
            alc.set(1, Cursor2);
            return alc;
        } catch (Exception ex) {

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[]{"" + ex.getMessage()});
            alc.set(1, Cursor2);
            return alc;
        }

    }
}
