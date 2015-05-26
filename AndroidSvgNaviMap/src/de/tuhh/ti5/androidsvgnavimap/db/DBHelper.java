package de.tuhh.ti5.androidsvgnavimap.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ruibrito on 14/05/15.
 */
public class DBHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String TAG = "DBHelper";

    // Database version
    private static final int DATABASE_VERSION = 1;

    // Database name
    private static final String DATABASE_NAME = "fingerprintsDB";

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
    public static final String MAC_mac = "mac";
    public static final String MAC_strength = "strength";
    public static final String MAC_channel = "channel";
    public static final String MAC_fp = "mac_FP";


    public static final String CREATE_LOCATION_TABLE =
            "CREATE TABLE " + TABLE_LOCATION + "("
                    + LOC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
                    + LOC_vertex + " INTEGER"
                    + LOC_floor + " INTEGER"
                    + LOC_building + " TEXT"
                    + LOC_alias + " TEXT";

    public static final String CREATE_FINGERPRINT_TABLE =
            "CREATE TABLE " + TABLE_FINGERPRINT + "("
                    + FP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
                    + FP_date + " TEXT"
                    + FP_weather + " TEXT"
                    + FP_barrier + " TEXT"
                    + FP_devices + " TEXT"
                    + FP_people + " TEXT"
                    + FP_location + " TEXT"
                    + "FOREIGN KEY(" + FP_location + ") REFERENCES "
                    + TABLE_LOCATION + " (" + LOC_ID + ")";

    //    + FP_location + " TEXT " + "REFERENCES " + TABLE_LOCATION + " (" + LOC_ID + ")";

    public static final String CREATE_MAC_TABLE =
            "CREATE TABLE " + TABLE_MAC + "("
                    + MAC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
                    + MAC_mac + " TEXT"
                    + MAC_strength + " TEXT"
                    + MAC_channel + " INTEGER"
                    + MAC_fp + " TEXT"
                    + "FOREIGN KEY(" + MAC_fp + ") REFERENCES "
                    + TABLE_FINGERPRINT + " (" + FP_ID + ")";


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


    }
}
