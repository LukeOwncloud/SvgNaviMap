import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
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

        Map<String, Set<Integer>> oneLocationManyFingerprintsMap = new HashMap<>();
        Map<Integer, List<QueryRowResult>> oneFingerprintManyMacsMap = new HashMap<>();

        List<String> listVertex = new ArrayList<>();
        List<String> listBSSID = new ArrayList<>();

        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {

            int fingerprint = resultSet.getInt("fp_ID");
            int strength = resultSet.getInt("strength");
            String bssid = resultSet.getString("bssid");
            String vertex = resultSet.getString("vertex");

            QueryRowResult queryRowResult =
                    new QueryRowResult(fingerprint, vertex, bssid, strength);


            listVertex.add(vertex);
            listBSSID.add(bssid);


            List<QueryRowResult> queryResults = oneFingerprintManyMacsMap.get(fingerprint);
            if (queryResults == null) {
                queryResults = new ArrayList<>();
            }
            queryResults.add(queryRowResult);
            oneFingerprintManyMacsMap.put(fingerprint, queryResults);

        }
        resultSet.close();


        for (Map.Entry<Integer, List<QueryRowResult>> entry : oneFingerprintManyMacsMap.entrySet()) {

            Integer fingerprint = entry.getKey();

            String vertex = oneFingerprintManyMacsMap.get(fingerprint).get(0).getVertex();

            Set<Integer> fingerprintIndexes = oneLocationManyFingerprintsMap.get(vertex);
            if (fingerprintIndexes == null) {
                fingerprintIndexes = new HashSet<>();
            }
            fingerprintIndexes.add(fingerprint);
            oneLocationManyFingerprintsMap.put(vertex, fingerprintIndexes);
        }

        listBSSID = (List<String>) removeListDuplicates(listBSSID);
        listVertex = (List<String>) removeListDuplicates(listVertex);

        JavaInternalDatabase internalDatabase = new JavaInternalDatabase(
                oneLocationManyFingerprintsMap,
                oneFingerprintManyMacsMap,
                listVertex,
                listBSSID);

        return internalDatabase;
    }

    public static WekaResultSet generateWekaResultSet(Map<Integer, List<QueryRowResult>> map) {

        HashMap<Integer, List<QueryRowResult>> testSet = new HashMap<>();
        HashMap<Integer, List<QueryRowResult>> trainingSet = new HashMap<>();

        String previousVertex = null;
        boolean first = true;

        for (Map.Entry<Integer, List<QueryRowResult>> entry : map.entrySet()) {

            int fingerprint = entry.getKey();

            List<QueryRowResult> resultList = map.get(fingerprint);

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

    public static WekaResultSet generateWekaResultSet(JavaInternalDatabase database) {

        if (nFingerprintsToTraining <= 0) {
            System.err.println("\nNUMBER OF FINGERPRINTS NEEDS TO BE BIGGER THAN 0 (ZERO), SO WE CAN HAVE A TEST SET.");
            System.exit(0);
        }

        Map<Integer, List<QueryRowResult>> fingerprintsMap = database.getOneFingerPrintToAllMacsMap();
        Map<String, Set<Integer>> locationsMap = database.getOneLocationToAllFingerprintsMap();

        HashMap<Integer, List<QueryRowResult>> testSet = new HashMap<>();
        HashMap<Integer, List<QueryRowResult>> trainingSet = new HashMap<>();

        for (Map.Entry<String, Set<Integer>> location : locationsMap.entrySet()) {

            String vertex = location.getKey();

            Set<Integer> fingerprintIndexes = locationsMap.get(vertex);

            int nFP = fingerprintIndexes.size();

            if (nFingerprintsToTraining >= nFP) {

                System.err.println("\n\nREDUCE NUMBER OF TEST SET FINGERPRINTS -> Vertex " + vertex + "  only has " + nFP + " Fingerprint(s)!\n");
                System.exit(0);
            } else {
                int total = nFP;
                for (int fp : fingerprintIndexes) {

                    List<QueryRowResult> queryResultList = fingerprintsMap.get(fp);

                    if (total > nFingerprintsToTraining) {
                        testSet.put(fp, queryResultList);
                    } else {
                        trainingSet.put(fp, queryResultList);
                    }
                    total--;
                }
            }
        }
        WekaResultSet wekaResultSet = new WekaResultSet(testSet, trainingSet);

        return wekaResultSet;
    }

    public static void writeARFF(String fileURL, int testNumber, List<String> distinctMacs, List<String> distinctRooms, Map<Integer, List<QueryRowResult>> dataset) {

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
            for (Map.Entry<Integer, List<QueryRowResult>> entry : dataset.entrySet()) {

                int fingerprint = entry.getKey();
                String bssid = null;

                List<QueryRowResult> queryRowResultList = dataset.get(fingerprint);
                List<Integer> strengthList = new ArrayList<>(Collections.nCopies(numberOfMacs, 0));

                for (QueryRowResult queryRowResult : queryRowResultList) {

                    bssid = queryRowResult.getBssid();

                    if (distinctMacs.contains(bssid)) {
                        int index = distinctMacs.indexOf(bssid);
                        strengthList.set(index, queryRowResult.getStrength());
                    }
                }

                for (int strength : strengthList) {
                    writer.print(strength + ",");
                }
                String vertex = queryRowResultList.get(0).getVertex();
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

    public static String getCurrentTime() {
        Calendar rightNow = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String formattedDate = dateFormat.format(rightNow.getTime());

        return formattedDate;
    }
}