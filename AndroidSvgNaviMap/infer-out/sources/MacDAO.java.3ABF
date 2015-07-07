package de.tuhh.ti5.androidsvgnavimap.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import de.tuhh.ti5.androidsvgnavimap.db.DBHelper;
import de.tuhh.ti5.androidsvgnavimap.db.model.Fingerprint;
import de.tuhh.ti5.androidsvgnavimap.db.model.Mac;

import java.sql.SQLException;

/**
 * Created by ruibrito on 26/05/15.
 */
public class MacDAO {

    public static final String TAG = "MacDAO";

    // DATABASE FIELDs
    private SQLiteDatabase sqLiteDatabase;
    private DBHelper dbHelper;
    private Context context;

    private String[] tableCollumns = {
            DBHelper.MAC_ID, DBHelper.MAC_SSID, DBHelper.MAC_BSSID,
            DBHelper.MAC_strength,
            DBHelper.MAC_channel, DBHelper.MAC_fp};

    public MacDAO(Context context) {
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

    public Mac createMac(String ssid, String bssid, int strength, int channel, long fingerprintID) {

        ContentValues values = new ContentValues();

        values.put(DBHelper.MAC_SSID, ssid);
        values.put(DBHelper.MAC_BSSID, bssid);
        values.put(DBHelper.MAC_strength, strength);
        values.put(DBHelper.MAC_channel, channel);
        values.put(DBHelper.MAC_fp, fingerprintID);

        long insertedID = sqLiteDatabase.insert(DBHelper.TABLE_MAC, null, values);

        Cursor cursor = sqLiteDatabase.query(
                DBHelper.TABLE_MAC,
                tableCollumns,
                DBHelper.MAC_ID + " = " + insertedID, null, null, null, null);

        cursor.moveToFirst();

        Mac newMac = cursorToMac(cursor);

        cursor.close();

        return newMac;
    }

    public void deleteAllMac() {
        String sql = "DELETE FROM " + DBHelper.TABLE_MAC;
        sqLiteDatabase.execSQL(sql);
    }

    private Mac cursorToMac(Cursor cursor) {

        Mac mac = new Mac();

        mac.setMacID(cursor.getLong(0));
        mac.setMacSSID(cursor.getString(1));
        mac.setMacBSSID(cursor.getString(2));
        mac.setStrength(cursor.getInt(3));
        mac.setChannel(cursor.getInt(4));

        long fingerprintID = cursor.getLong(5);

        FingerprintDAO fingerprintDAO = new FingerprintDAO(context);

        Fingerprint fingerprint = fingerprintDAO.getFingerprintByID(fingerprintID);

        if (fingerprint != null) {
            mac.setFingerprint(fingerprint);
        }
        return mac;
    }
}
