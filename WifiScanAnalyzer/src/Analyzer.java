import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ruibrito on 04/06/15.
 */
public class Analyzer {

    public static final int nFingerprintsToTraining = 1; // Used in DBUtil, here for easy access.

    private static List<TestQuery> queriesForTesting;
    private static List<String> distinctMacs;
    private static List<String> distinctRooms;
    private static JavaInternalDatabase database;

    // private static String DATABASE_FILENAME = "fingerprintsDB_05-06-2015 19:43:39.db3";
    // private static String DATABASE_FILENAME = "fingerprintsDB_05-06-2015 20:01:25.db3";

    private static String DATABASE_FILENAME = "fingerprintsDB_06-07-2015 18:32:51.db3";
    private static String DATABASE_LOCATION = "resources/Databases/" + DATABASE_FILENAME + "/";
    private static String RESULTS_FOLDER = "resources/Weka_Results/" + DATABASE_FILENAME + "/";

    private static String TEST_SET_FOLDER = "resources/ARFF_Testset/" + DATABASE_FILENAME + "/";
    private static String TRAINING_SET_FOLDER = "resources/ARFF_Trainingset/" + DATABASE_FILENAME + "/";

    public static void main(String[] args) throws Exception {

        // Load the sqlite-JDBC driver using the current class loader
        Class.forName("org.sqlite.JDBC");

        Connection connection = null;
        Statement statement = null;

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_LOCATION);

            statement = connection.createStatement();

            prepareOutputFolders();

            final List<String> allBSSIDinDB = DBUtil.getDistinctMacs(connection);

            queriesForTesting = getQueriesForTesting();
            for (TestQuery testQuery : queriesForTesting) {
                System.out.print("Test " + testQuery.getType() + "__" + testQuery.getTestNumber() + "...........");

                createARFF(statement, testQuery, testQuery.getType());
                try {
                    wekaAnalysis(testQuery);
                } catch (Exception e) {
                    // e.printStackTrace();
                    System.err.println(" FINGERPINTS ERROR --- " + e.getMessage());
                    continue;
                }
                System.out.println(" done.");
            }
            statement.close();
            connection.close();

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static List<TestQuery> getQueriesForTesting() {
        List<TestQuery> queries = new ArrayList<>();

        // queries.add(new TestQuery(1, "All",     " 1 = 1 ", null, "All the results"));

        // Time of day - Corridor
        queries.add(new TestQuery(2, "Corridor", " F.date >= '2015-06-08 08:00:00' AND F.date <= '2015-07-01 19:00:00' ", "Corridor, E4", "All the Corridor tests"));
        queries.add(new TestQuery(3, "Corridor", " strftime('%H', F.date) >= '08' AND strftime('%H', F.date) <= '11' ", "Corridor, E4", "Corridor - Morning"));
        queries.add(new TestQuery(4, "Corridor", " strftime('%H', F.date) >= '11' AND strftime('%H', F.date) <= '15' ", "Corridor, E4", "Corridor - Lunch"));
        queries.add(new TestQuery(5, "Corridor", " strftime('%H', F.date) >= '15' AND strftime('%H', F.date) <= '17' ", "Corridor, E4", "Corridor - Afternoon"));
        queries.add(new TestQuery(6, "Corridor", " strftime('%H', F.date) >= '17' AND strftime('%H', F.date) <= '19' ", "Corridor, E4", "Corridor - Night"));

        // Corridor Tests
        queries.add(new TestQuery(7, "Corridor", " F.date >= '2015-06-08 15:12:00' AND F.date <= '2015-06-08 15:19:00' ", "Corridor, E4, Clear, 2, No, Unknown", "Corridor First Test"));
        queries.add(new TestQuery(8, "Corridor", " F.date >= '2015-06-24 14:48:00' AND F.date <= '2015-06-24 15:08:00' ", "Corridor, E4, Cloudy, 2, No 1-2, Yes, Unknown", "No notes"));
        queries.add(new TestQuery(9, "Corridor", " F.date >= '2015-06-24 17:00:00' AND F.date <= '2015-06-24 18:00:00' ", "Corridor, E4, Cloudy, 2, No 1-2, Yes, Unknown", "No notes"));
        queries.add(new TestQuery(10, "Corridor", " F.date >= '2015-06-29 18:00:00' AND F.date <= '2015-06-29 19:00:00' ", "Corridor, E4, Clear, 2, No, Unknown", "Late in the evening"));
        queries.add(new TestQuery(11, "Corridor", " F.date >= '2015-06-30 08:00:00' AND F.date <= '2015-06-30 10:10:00' ", "Corridor, E4, Clear, 2, No, Unknown", "Normal morning"));
        queries.add(new TestQuery(12, "Corridor", " F.date >= '2015-06-30 11:00:00' AND F.date <= '2015-06-30 13:30:00' ", "Corridor, E4, Clear, 2, No, Unknown", "Lunch Hour"));
        queries.add(new TestQuery(13, "Corridor", " F.date >= '2015-06-30 11:00:00' AND F.date <= '2015-06-30 13:00:00' ", "Corridor, E4, Clear, 2, No, Unknown", "Early Lunch Hour"));
        queries.add(new TestQuery(14, "Corridor", " F.date >= '2015-06-30 13:00:00' AND F.date <= '2015-06-30 13:30:00' ", "Corridor, E4, Clear, 2, No, Unknown", "Late Lunch Hour"));
        queries.add(new TestQuery(15, "Corridor", " F.date >= '2015-06-30 16:00:00' AND F.date <= '2015-06-30 17:00:00' ", "Corridor, E4, Clear, 2, No, Yes, Unknown", "Only Ohrt has in the ofice, so almost no people"));

        // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        // Time of day - Rooms
        queries.add(new TestQuery(16, "Rooms", " F.date >= '2015-06-08 08:00:00' AND F.date <= '2015-07-01 19:00:00' ", "Rooms, E4", "All the Rooms tests"));
        queries.add(new TestQuery(17, "Rooms", " strftime('%H', F.date) >= '08' AND strftime('%H', F.date) <= '11' ", "Rooms, E4", "Rooms - Morning"));
        queries.add(new TestQuery(18, "Rooms", " strftime('%H', F.date) >= '11' AND strftime('%H', F.date) <= '15' ", "Rooms, E4", "Rooms - Lunch"));
        queries.add(new TestQuery(19, "Rooms", " strftime('%H', F.date) >= '15' AND strftime('%H', F.date) <= '17' ", "Rooms, E4", "Rooms - Afternoon"));
        queries.add(new TestQuery(20, "Rooms", " strftime('%H', F.date) >= '17' AND strftime('%H', F.date) <= '19' ", "Rooms, E4", "Rooms - Night"));

        // Rooms Tests
        queries.add(new TestQuery(21, "Rooms", " F.date >= '2015-06-08 15:22:00' AND F.date <= '2015-06-08 15:33:00' ", "Rooms, E4, Clear, 2, No, Unknown", "Rooms First Real Test"));
        queries.add(new TestQuery(22, "Rooms", " F.date >= '2015-06-24 14:48:00' AND F.date <= '2015-06-24 15:08:00' ", "Rooms, E4, Cloudy, 2 4, No 1-2, Yes, Unknown", "Fiz merda, App crash, no"));
        queries.add(new TestQuery(23, "Rooms", " F.date >= '2015-06-30 11:00:00' AND F.date <= '2015-06-30 13:30:00' ", "Rooms, E4, Clear, 2 4, No 1-2, Yes, Unknown", "Lunch Hour"));
        queries.add(new TestQuery(24, "Rooms", " F.date >= '2015-06-30 16:00:00' AND F.date <= '2015-06-30 17:00:00' ", "Rooms, E4, Clear, 2 4 , No 1-2, Yes, Unknown", "Middle of the afternoon"));

        // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        // Time of day - Pool
        queries.add(new TestQuery(25, "Pool", " F.date >= '2015-06-08 08:00:00' AND F.date <= '2015-07-01 19:00:00' ", "Pool, E2", "All the Pool tests"));
        queries.add(new TestQuery(26, "Pool", " strftime('%H', F.date) >= '08' AND strftime('%H', F.date) <= '11' ", "Pool, E2", "Pool - Morning"));
        queries.add(new TestQuery(27, "Pool", " strftime('%H', F.date) >= '11' AND strftime('%H', F.date) <= '15' ", "Pool, E2", "Pool - Lunch"));
        queries.add(new TestQuery(28, "Pool", " strftime('%H', F.date) >= '15' AND strftime('%H', F.date) <= '17' ", "Pool, E2", "Pool - Afternoon"));
        queries.add(new TestQuery(29, "Pool", " strftime('%H', F.date) >= '17' AND strftime('%H', F.date) <= '19' ", "Pool, E2", "Pool - Night"));

        // Corridor Pool
        queries.add(new TestQuery(30, "Pool", " F.date >= '2015-06-24 15:08:00' AND F.date <= '2015-06-24 15:20:00' ", "Pool, E2, Cloudy, 2 4, No, 1-2, 3-6, Yes, Unknown", " No notes"));
        queries.add(new TestQuery(31, "Pool", " F.date >= '2015-06-24 17:00:00' AND F.date <= '2015-06-24 18:00:00' ", "Pool, E2, Cloudy, 2 4, No, 1-2, 3-6, Yes, Unknown", "Very few people, good test"));
        queries.add(new TestQuery(32, "Pool", " F.date >= '2015-06-29 18:00:00' AND F.date <= '2015-06-29 19:00:00' ", "Pool, E2, Clear, 2 4, No, 1-2 3-6, Yes, Unknown", "A lot of people in locations 2123, 2131, 2130 ---- only 2 people at 2124\""));
        queries.add(new TestQuery(33, "Pool", " F.date >= '2015-06-30 08:00:00' AND F.date <= '2015-06-30 10:00:00' ", "Pool, E2, Clear, 2 4, No, 1-2 3-6, Yes, Unknown", "Normal morning"));
        queries.add(new TestQuery(34, "Pool", " F.date >= '2015-06-30 11:00:00' AND F.date <= '2015-06-30 13:30:00' ", "Pool, E2, Clear, 2 4, No, 1-2 3-6, Yes, Unknown", "Lunch Hour"));
        queries.add(new TestQuery(35, "Pool", " F.date >= '2015-06-30 17:50:00' AND F.date <= '2015-06-30 19:00:00' ", "Pool, E2, Clear, 2 4, No, 1-2 3-6, Yes, Unknown", "Worse scan ever, made 2 mistakes at 2124 and 2153"));

        // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        // Time of day - Mensa
        queries.add(new TestQuery(36, "Mensa", " F.date >= '2015-06-08 08:00:00' AND F.date <= '2015-07-01 19:00:00' ", "Mensa, E2", "All the Mensa tests"));
        queries.add(new TestQuery(37, "Mensa", " strftime('%H', F.date) >= '08' AND strftime('%H', F.date) <= '11' ", "Mensa, E2", "Mensa - Morning"));
        queries.add(new TestQuery(38, "Mensa", " strftime('%H', F.date) >= '11' AND strftime('%H', F.date) <= '15' ", "Mensa, E2", "Mensa - Lunch"));
        queries.add(new TestQuery(39, "Mensa", " strftime('%H', F.date) >= '15' AND strftime('%H', F.date) <= '17' ", "Mensa, E2", "Mensa - Afternoon"));
        queries.add(new TestQuery(40, "Mensa", " strftime('%H', F.date) >= '17' AND strftime('%H', F.date) <= '19'  ", "Mensa, E2", "Mensa - Night"));

        // Mensa Tests
        queries.add(new TestQuery(41, "Mensa", " F.date >= '2015-06-30 08:00:00' AND F.date <= '2015-06-30 10:00:00' ", "Mensa, M0, Clear, - ,1-2 6+, Yes, Unknown", "Morning in the mensa, normal thing"));
        queries.add(new TestQuery(42, "Mensa", " F.date >= '2015-06-30 11:00:00' AND F.date <= '2015-06-30 13:30:00' ", "Mensa, M0, Clear, - ,1-2 6+, Yes, Unknown", "Lunch Hour"));
        queries.add(new TestQuery(43, "Mensa", " F.date >= '2015-06-30 17:50:00' AND F.date <= '2015-06-30 19:00:00' ", "Mensa, M0, Clear, - ,No 1-2, Yes, Unknown", "No Loc. 168 (4144), also I think I made a mistake"));
        queries.add(new TestQuery(44, "Mensa", " F.date >= '2015-0-01 10:50:00' AND F.date <= '2015-07-01 19:00:00' ", "Mensa, M0, Clear, - ,No 1-2, Yes, Unknown", "Last Mensa test"));

        return queries;
    }

    private static void prepareOutputFolders() {

        File testDir = new File(TEST_SET_FOLDER);
        File trainingDir = new File(TRAINING_SET_FOLDER);
        File resultsDir = new File(RESULTS_FOLDER);

        if (!testDir.exists()) {
            try {
                testDir.mkdir();
            } catch (SecurityException se) {
                se.printStackTrace();
            }
        }

        if (!trainingDir.exists()) {
            try {
                trainingDir.mkdir();
            } catch (SecurityException se) {
                se.printStackTrace();
            }
        }

        if (!resultsDir.exists()) {
            try {
                resultsDir.mkdir();
            } catch (SecurityException se) {
                se.printStackTrace();
            }
        }
    }

    private static void wekaAnalysis(TestQuery testQuery) throws Exception {

        final String type = testQuery.getType();

        String  trainingDataPath = TRAINING_SET_FOLDER + type + "__training" + testQuery.getTestNumber() + ".arff";
        String testDataPath = TEST_SET_FOLDER + type + "__test" + testQuery.getTestNumber() + ".arff";

        DataSource trainingSource = new DataSource(trainingDataPath);
        Instances trainingData = trainingSource.getDataSet();

        if (trainingData.classIndex() == -1)
            trainingData.setClassIndex(trainingData.numAttributes() - 1);

        DataSource testSource = new DataSource(testDataPath);
        Instances testData = testSource.getDataSet();

        if (testData.classIndex() == -1)
            testData.setClassIndex(testData.numAttributes() - 1);

        String optionString = "-I 20 -K 0 -S 1";
        String[] options = weka.core.Utils.splitOptions(optionString);

        String[] options2 = new String[3];
            options2[0] = "-I 100";
            options2[1] = "-K 0";
            options2[2] = "-S 1";

        Remove remove = new Remove();
        remove.setOptions(options);
        remove.setInputFormat(trainingData);

        Instances newTrainingData = Filter.useFilter(trainingData, remove);

        RandomForest randomForest = new RandomForest();
        randomForest.setOptions(options);
        randomForest.buildClassifier(newTrainingData);

        Evaluation eval = new Evaluation(newTrainingData);
        eval.evaluateModel(randomForest, testData);

        String evalSummary = eval.toSummaryString("\n================== Results ==================\n", false);
        String evalClassDetails = eval.toClassDetailsString("\n================== Details ==================\n");
        String evalConfusionMatrix = eval.toMatrixString("\n ================== Confusion Matrix ==================\n");

        double[][] confusionMatrix = eval.confusionMatrix();

        Map<String, Double> matrixCorrectMap = new HashMap<>();
        Map<String, Double> matrixIncorrectMap = new HashMap<>();


        for (int i = 0; i < confusionMatrix.length; i++) {
            double nDiagonal = confusionMatrix[i][i];
            String room = distinctRooms.get(i);
            matrixCorrectMap.put(room, nDiagonal);
        }

        int matrixSize = confusionMatrix.length;
        for (int i = 0; i < matrixSize; i++) {
            String room = distinctRooms.get(i);
            for (int j = 0; j < matrixSize; j++) {
                if (i == j) {
                    continue;
                }
                double value = confusionMatrix[i][j];
                Double total = matrixIncorrectMap.get(room);
                if (total == null) {
                    total = 0.0;
                }
                total += value;
                matrixIncorrectMap.put(room, total);
            }
        }

        writeResultFile(
                testQuery,
                evalSummary,
                evalClassDetails,
                evalConfusionMatrix,
                matrixCorrectMap,
                matrixIncorrectMap);
    }

    private static void writeResultFile(TestQuery testQuery, String summary, String classDetails, String confusionMatrix, Map<String, Double> matrixCorrectMap, Map<String, Double> matrixIncorrectMap) {

        try {
            String fileName = RESULTS_FOLDER + testQuery.getType() + "__" + testQuery.getTestNumber() + ".txt";

            FileWriter outFile = new FileWriter(fileName);
            PrintWriter out = new PrintWriter(outFile);

            // Write text to file
            out.println("DATABASE : " + DATABASE_FILENAME);
            out.println("\nRESULT n_" + testQuery.getTestNumber() + " on " + DBUtil.getCurrentTime());
            out.println("\nQUERY : \n" + testQuery.getQuery());
            out.print("\nCONDITIONS & NOTE : \n" + "" + testQuery.getCondtions() + "\n" + testQuery.getNote() + "\n\n");
            out.println(summary);
            out.println(classDetails);
            out.println(confusionMatrix);

            out.println("\nMatrix Results \n==================\n");

            String sFormater = "%s\t%f\t%f\t%s";

            out.printf("%s\t%s\t%s\t%s", "Room:", "Correct:", "Incorrect:", "Results:");
            out.println("");
            for (String room : distinctRooms) {

                Double correct = matrixCorrectMap.get(room);
                Double incorrect = matrixIncorrectMap.get(room);

                double total = correct - incorrect;
                if (total <= 0) {
                    out.printf(sFormater, room, correct, incorrect, "ATTENTION\n");
                } else {
                    out.printf(sFormater, room, correct, incorrect, "Normal\n");
                }
            }

            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void createARFF(Statement statement, TestQuery testQuery, String type) throws ClassNotFoundException, SQLException {

        database = DBUtil.buildDatabase(testQuery.getQuery(), statement);

        distinctMacs = database.getAllBSSIDs();
        distinctRooms = database.getAllRooms();

        WekaResultSet wekaResultSets = DBUtil.generateWekaResultSet(database);

        if (wekaResultSets == null) {
            writeResultFile(testQuery, "NO SUMMARY", "NO DETAILS", "NO CONFUSION MATRIX", null, null);
        } else {

            DBUtil.writeARFF(TEST_SET_FOLDER + type + "__test", testQuery.getTestNumber(),
                    distinctMacs, distinctRooms, wekaResultSets.getTestSet());
            DBUtil.writeARFF(TRAINING_SET_FOLDER + type + "__training", testQuery.getTestNumber(),
                    distinctMacs, distinctRooms, wekaResultSets.getTrainingSet());
        }
    }

}
