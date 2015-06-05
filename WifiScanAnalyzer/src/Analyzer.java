import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * Created by ruibrito on 04/06/15.
 */
public class Analyzer {

    private static List<String> queriesForTesting;
    private static List<String> distinctMacs;
    private static List<String> distinctRooms;

    private static JavaInternalDatabase database;

    private static String DATABASE_LOCATION = "resources/Databases/fingerprintsDB_05-06-2015 18:51:15.db3";

    private static String RESULTS_FOLDER = "resources/Weka_Results/";
    private static String TEST_SET_FOLDER = "resources/ARFF_Testset/";
    private static String TRAINING_SET_FOLDER = "resources/ARFF_Trainingset/";

    public static void main(String[] args) throws Exception {

        // Load the sqlite-JDBC driver using the current class loader
        Class.forName("org.sqlite.JDBC");

        Connection connection = null;
        Statement statement = null;

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_LOCATION);

            statement = connection.createStatement();

            queriesForTesting = getQueriesForTesting();

            // distinctMacs = DBUtil.getDistinctMacs(connection);

            // distinctRooms = DBUtil.getDistinctRooms(connection);

            for (int testNumber = 0; testNumber < queriesForTesting.size(); testNumber++) {

                System.out.print("Test " + testNumber + "...........");
                String testQuery = queriesForTesting.get(testNumber);

                createARFF(statement, testQuery, testNumber);

                wekaAnalysis(testQuery, testNumber);

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

    public static List<String> getQueriesForTesting() {

        List<String> list = new ArrayList<>();

        String initialQuery = "SELECT F.fp_ID, M.mac_ID, M.bssid, M.strength, L.vertex, F.date\n" +
                "  FROM \n" +
                "      Fingerprint AS F \n" +
                "          INNER JOIN\n" +
                "      Location AS L\n" +
                "          on F.fp_Location = L.loc_ID\n" +
                "          INNER JOIN\n" +
                "      Mac as M\n" +
                "          on M.mac_FP = F.fp_ID\n";

        String where = "WHERE ";

        String query1 = "1 = 1";
        String query2 = "1 = 1";
        String query3 = "1 = 1";
        String query4 = "1 = 1";
        String query5 = "1 = 1";
        String query6 = "1 = 1";
        String query7 = "1 = 1";
        String query8 = "1 = 1";
        String query9 = "1 = 1";

        list.add(initialQuery + where + query1);
//        list.add(initialQuery + where + query2);
//        list.add(initialQuery + where + query3);
//        list.add(initialQuery + where + query4);
//        list.add(initialQuery + where + query5);
//        list.add(initialQuery + where + query6);
//        list.add(initialQuery + where + query7);
//        list.add(initialQuery + where + query8);
//        list.add(initialQuery + where + query9);

        return list;
    }

    private static void wekaAnalysis(String testQuery, int testNumber) throws Exception {

        String trainingDataPath = TRAINING_SET_FOLDER + "training" + testNumber + ".arff";
        String testDataPath = TEST_SET_FOLDER + "test" + testNumber + ".arff";

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

        Remove remove = new Remove();
        remove.setOptions(options);
        remove.setInputFormat(trainingData);

        Instances newTrainingData = Filter.useFilter(trainingData, remove);

        RandomForest randomForest = new RandomForest();
        randomForest.setOptions(options);
        randomForest.buildClassifier(newTrainingData);

        Evaluation eval = new Evaluation(newTrainingData);
        eval.evaluateModel(randomForest, testData);

        String evalSummary = eval.toSummaryString("\nResults\n==================\n", false);
        String evalClassDetails = eval.toClassDetailsString("\n\n\nDetails\n==================\n");
        String evalConfusionMatrix = eval.toMatrixString("\n\n\nConfusion Matrix\n==================\n");

        double[][] confusionMatrix = eval.confusionMatrix();

        Map<String, Double> matrixCorrectMap = new HashMap<>();
        Map<String, Double> matrixIncorrectMap = new HashMap<>();


        for (int i = 0; i < confusionMatrix.length; i++) {

            double nDiagonal = confusionMatrix[i][i];

            String room = distinctRooms.get(i);
            if (nDiagonal != 0) {
                matrixCorrectMap.put(room, nDiagonal);
            } else {
                matrixIncorrectMap.put(room, nDiagonal);
            }
        }
        writeResultFile(
                testNumber,
                testQuery,
                evalSummary,
                evalClassDetails,
                evalConfusionMatrix,
                matrixCorrectMap,
                matrixIncorrectMap);
    }

    private static void writeResultFile(int testNumber, String query, String summary, String classDetails, String confusionMatrix, Map<String, Double> matrixCorrectMap, Map<String, Double> matrixIncorrectMap) {

        try {
            String fileName = RESULTS_FOLDER + "result" + testNumber + ".txt";

            FileWriter outFile = new FileWriter(fileName);
            PrintWriter out = new PrintWriter(outFile);

            // Write text to file
            out.println("RESULT #" + testNumber);
            out.println("\nQUERY: \n" + query);
            out.print("\n \n \n");
            out.println(summary);
            out.println(classDetails);
            out.println(confusionMatrix);

            out.print("\n");

            out.println("\n\nCorrect Matrix Results \n==================\n");
            if (matrixCorrectMap.isEmpty()) {
                out.println("\tEMPTY");
            } else {
                for (String room : matrixCorrectMap.keySet()) {
                    Double aDouble = matrixCorrectMap.get(room);
                    out.println(room + " : " + aDouble);
                }
            }

            out.println("\n\nIncorrect Matrix Results \n==================\n");
            if (matrixIncorrectMap.isEmpty()) {
                out.println("\tEMPTY");
            } else {
                for (String room : matrixIncorrectMap.keySet()) {
                    Double aDouble = matrixIncorrectMap.get(room);
                    out.println(room + " : " + aDouble);
                }
            }

            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void createARFF(Statement statement, String testQuery, int testNumber) throws ClassNotFoundException, SQLException {

        database = DBUtil.buildDatabase(testQuery, statement);

        distinctMacs = database.getAllBSSIDs();
        distinctRooms = database.getAllRooms();

        WekaResultSet wekaResultSets = DBUtil.getWekaResultSet(database);

        DBUtil.writeARFF(TEST_SET_FOLDER + "test", testNumber, distinctMacs, distinctRooms, wekaResultSets.getTestSet());
        DBUtil.writeARFF(TRAINING_SET_FOLDER + "training", testNumber, distinctMacs, distinctRooms, wekaResultSets.getTrainingSet());
    }


}
