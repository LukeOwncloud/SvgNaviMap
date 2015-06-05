import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * Created by ruibrito on 04/06/15.
 */
public class DBUtil {


    public static List<String> getDistinctMacs(Connection connection) {

        List<String> listDistinctMacs = new ArrayList<>();

        Statement statement = null;
        try {
            statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT DISTINCT bssid FROM MAC;");

            while (resultSet.next()) {
                String bssid = resultSet.getString("bssid");
                listDistinctMacs.add(bssid);
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listDistinctMacs;
    }

    public static List<String> getDistinctRooms(Connection connection) {

        List<String> listDistinctRooms = new ArrayList<>();

        Statement statement = null;
        try {
            statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT DISTINCT vertex FROM Location;");

            while (resultSet.next()) {
                String vertex = resultSet.getString("vertex");
                listDistinctRooms.add(vertex);
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listDistinctRooms;

    }

    public static HashMap<Integer, List<SingleDBResult>> getResultQueryMap(String query, Statement statement) throws SQLException {

        HashMap<Integer, List<SingleDBResult>> dbQueryMap = new HashMap<>();

        List<SingleDBResult> singleDBResultList = new ArrayList<>();

        ResultSet resultSet = statement.executeQuery(query);

        int previousFingerprint = 0;
        boolean first = true;

        while (resultSet.next()) {

            int fingerprint = resultSet.getInt("fp_ID");
            String bssid = resultSet.getString("bssid");
            String vertex = resultSet.getString("vertex");
            int strength = resultSet.getInt("strength");

            if (first) {
                previousFingerprint = fingerprint;
                first = false;
            }

            if (fingerprint == previousFingerprint) {
                SingleDBResult singleDBResult = new SingleDBResult(fingerprint, vertex, bssid, strength);
                singleDBResultList.add(singleDBResult);

            } else {
                dbQueryMap.put(fingerprint, singleDBResultList);
                singleDBResultList = new ArrayList<>();
            }
            previousFingerprint = fingerprint;
        }
        resultSet.close();

        return dbQueryMap;
    }

    public static WekaResultSet getWekaResultSets(HashMap<Integer, List<SingleDBResult>> map) {

        HashMap<Integer, List<SingleDBResult>> testSet = new HashMap<>();
        HashMap<Integer, List<SingleDBResult>> trainingSet = new HashMap<>();

        String previousVertex = null;
        boolean first = true;
        for (Map.Entry<Integer, List<SingleDBResult>> entry : map.entrySet()) {

            int fingerprint = entry.getKey();

            List<SingleDBResult> resultList = map.get(fingerprint);

            String vertex = resultList.get(0).getVertex();

            if (first) {
                previousVertex = vertex;
                first = false;
            }

            if (vertex.equals(previousVertex)) {
                testSet.put(fingerprint, resultList);
            } else {
                trainingSet.put(fingerprint, resultList);
            }
            previousVertex = vertex;
        }
        WekaResultSet wekaResultSet = new WekaResultSet(testSet, trainingSet);

        return wekaResultSet;

    }

    public static void writeARFF(String fileURL, int testNumber, List<String> distinctMacs, List<String> distinctRooms, HashMap<Integer, List<SingleDBResult>> dataset) {

        PrintWriter writer = null;

        try {
            writer = new PrintWriter(fileURL + "" + testNumber + ".arff");

            writer.println("@relation _tm450");
            writer.println("");

            for (String bssid : distinctMacs) {
                writer.println("@attribute " + bssid + " integer");
            }
            writer.println("");

            writer.print("@attribute ROOM {");
            for (String room : distinctRooms) {
                writer.print(room + ",");
            }
            writer.println("}");
            writer.println("");
            writer.println("@data");
            writer.println("");

            int sizeOfAllMacs = distinctMacs.size();
            for (Map.Entry<Integer, List<SingleDBResult>> entry : dataset.entrySet()) {

                int fingerprint = entry.getKey();
                String bssid = null;

                List<SingleDBResult> singleDBResultList = dataset.get(fingerprint);

                List<Integer> strengthList = new ArrayList<>(Collections.nCopies(sizeOfAllMacs, 0));

                for (SingleDBResult singleDBResult : singleDBResultList) {

                    bssid = singleDBResult.getBssid();

                    if (distinctMacs.contains(bssid)) {
                        int index = distinctMacs.indexOf(bssid);

                        strengthList.set(index, singleDBResult.getStrength());
                    }
                }

                for (int strength : strengthList) {
                    writer.print(strength + ",");
                }
                writer.print(singleDBResultList.get(0).getVertex());
                writer.println("");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }
}