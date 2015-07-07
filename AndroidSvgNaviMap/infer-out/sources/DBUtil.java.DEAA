package de.tuhh.ti5.androidsvgnavimap.db;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by ruibrito on 27/05/15.
 */
public class DBUtil {

    public static int scanFreqToChannel(int freq) {
        switch (freq) {
            case 2412:
                return 1;

            case 2417:
                return 2;

            case 2422:
                return 3;

            case 2427:
                return 4;

            case 2432:
                return 5;

            case 2437:
                return 6;

            case 2442:
                return 7;

            case 2447:
                return 8;

            case 2452:
                return 9;

            case 2457:
                return 10;

            case 2462:
                return 11;

            case 2467:
                return 12;

            case 2472:
                return 13;

            case 2484:
                return 14;

            default:
                return 0;
        }
    }

    public static String getCurrentTime() {
        Calendar rightNow = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String formattedDate = dateFormat.format(rightNow.getTime());

        return formattedDate;
    }

    public static void exportDB() {

        String databaseName = DBHelper.DATABASE_NAME;

        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "/data/" + "de.tuhh.ti5.androidsvgnavimap" + "/databases/" + databaseName;
                String backupDBPath = databaseName + "_" + getCurrentTime();

                File currentDB = new File(data, currentDBPath);

                File backupDB = new File(sd + File.separator + "SVGDatabaseSave/", backupDBPath + ".db3");
                //File backupDB = Utils.getSdDir("SVGDatabaseSave/" + databaseName + "_" + getCurrentTime());

                if (currentDB.exists()) {

                    FileChannel source = new FileInputStream(currentDB).getChannel();
                    FileChannel destination = new FileOutputStream(backupDB).getChannel();

                    destination.transferFrom(source, 0, source.size());

                    source.close();

                    destination.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

