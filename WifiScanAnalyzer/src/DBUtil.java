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

    private static final int nFingerprintsToTraining = 1;

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

    public static JavaInternalDatabase buildDatabase(String query, Statement statement) throws SQLException {

        Map<String, Set<Integer>> oneLocationToAllFingerprintsMap = new HashMap<>();
        Map<Integer, List<SingleDBResult>> oneFingerPrintToAllMacsMap = new HashMap<>();

        List<String> vertexes = new ArrayList<>();
        List<String> bssids = new ArrayList<>();

        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {

            int fingerprint = resultSet.getInt("fp_ID");
            int strength = resultSet.getInt("strength");
            String bssid = resultSet.getString("bssid");
            String vertex = resultSet.getString("vertex");

            SingleDBResult singleDBResult = new SingleDBResult(
                    fingerprint, vertex, bssid, strength);

            vertexes.add(vertex);
            bssids.add(bssid);

            List<SingleDBResult> singleDBResults = oneFingerPrintToAllMacsMap.get(fingerprint);
            if (singleDBResults == null) {
                singleDBResults = new ArrayList<>();
            }
            singleDBResults.add(singleDBResult);
            oneFingerPrintToAllMacsMap.put(fingerprint, singleDBResults);

        }
        resultSet.close();

        for (Map.Entry<Integer, List<SingleDBResult>> entry : oneFingerPrintToAllMacsMap.entrySet()) {

            Integer fingerprint = entry.getKey();

            String vertex = oneFingerPrintToAllMacsMap.get(fingerprint).get(0).getVertex();

            Set<Integer> fingerprintIndexes = oneLocationToAllFingerprintsMap.get(vertex);
            if (fingerprintIndexes == null) {
                fingerprintIndexes = new HashSet<>();
            }
            fingerprintIndexes.add(fingerprint);
            oneLocationToAllFingerprintsMap.put(vertex, fingerprintIndexes);
        }

        bssids = (List<String>) removeListDuplicates(bssids);
        vertexes = (List<String>) removeListDuplicates(vertexes);

        JavaInternalDatabase internalDatabase = new JavaInternalDatabase(
                oneLocationToAllFingerprintsMap,
                oneFingerPrintToAllMacsMap,
                vertexes,
                bssids);

        return internalDatabase;
    }

    public static WekaResultSet getWekaResultSet(Map<Integer, List<SingleDBResult>> map) {

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

    public static WekaResultSet getWekaResultSet(JavaInternalDatabase database) {

        if (nFingerprintsToTraining <= 0) {
            System.err.println("\nNUMBER OF FINGERPRINTS NEEDS TO BE BIGGER THAN 0 (ZERO), SO WE CAN HAVE A TEST SET.");
            System.exit(0);
        }

        Map<Integer, List<SingleDBResult>> fingerprintsMap = database.getOneFingerPrintToAllMacsMap();
        Map<String, Set<Integer>> locationsMap = database.getOneLocationToAllFingerprintsMap();

        HashMap<Integer, List<SingleDBResult>> testSet = new HashMap<>();
        HashMap<Integer, List<SingleDBResult>> trainingSet = new HashMap<>();

        for (Map.Entry<String, Set<Integer>> location : locationsMap.entrySet()) {

            String vertex = location.getKey();

            Set<Integer> fingerprintIndexes = locationsMap.get(vertex);

            int nFpIndexes = fingerprintIndexes.size();

            if (nFingerprintsToTraining >= nFpIndexes) {

                System.err.println("\n\nREDUCE NUMBER OF TEST SET FINGERPRINTS -> Vertex " + vertex + "  only has " + nFpIndexes + " Fingerprint(s)!\n" );
                System.exit(0);

            } else {

                int total = nFpIndexes;
                for (int fp : fingerprintIndexes) {

                    List<SingleDBResult> singleDBResultList = fingerprintsMap.get(fp);

                    if (total > nFingerprintsToTraining) {
                        testSet.put(fp, singleDBResultList);
                    } else {
                        trainingSet.put(fp, singleDBResultList);
                    }
                    total--;
                }
            }
        }
        WekaResultSet wekaResultSet = new WekaResultSet(testSet, trainingSet);

        return wekaResultSet;

    }

    public static void writeARFF(String fileURL, int testNumber, List<String> distinctMacs, List<String> distinctRooms, Map<Integer, List<SingleDBResult>> dataset) {

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

            int numberOfMacs = distinctMacs.size();
            for (Map.Entry<Integer, List<SingleDBResult>> entry : dataset.entrySet()) {

                int fingerprint = entry.getKey();
                String bssid = null;

                List<SingleDBResult> singleDBResultList = dataset.get(fingerprint);
                List<Integer> strengthList = new ArrayList<>(Collections.nCopies(numberOfMacs, 0));

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
                String vertex = singleDBResultList.get(0).getVertex();
                writer.print(vertex);
                writer.println("");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }

    private static List<?> removeListDuplicates(List<?> list) {

        List<Object> newList = new ArrayList<>();
        
        for (Object x : list) {

            if (!newList.contains(x)) {
                newList.add(x);
            }
        }
        return newList;
    }
}