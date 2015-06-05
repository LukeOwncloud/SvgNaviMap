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

    private static List<TestQuery> queriesForTesting;
    private static List<String> distinctMacs;
    private static List<String> distinctRooms;

    private static JavaInternalDatabase database;


    private static String DATABASE_FILENAME = "fingerprintsDB_05-06-2015 19:43:39.db3";


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

            queriesForTesting = getQueriesForTesting();

            // distinctMacs = DBUtil.getDistinctMacs(connection);

            // distinctRooms = DBUtil.getDistinctRooms(connection);

            prepareSaveFolders();

            for (int testNumber = 0; testNumber < queriesForTesting.size(); testNumber++) {

                System.out.print("Test " + testNumber + "...........");

                TestQuery testQuery = queriesForTesting.get(testNumber);
                String query = testQuery.getQuery();
                String queryNote = testQuery.getNote();

                createARFF(statement, query, testNumber);

                wekaAnalysis(query, testNumber, queryNote);

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

    private static void prepareSaveFolders() {

        File testDir = new File(TEST_SET_FOLDER);
        File trainingDir = new File(TRAINING_SET_FOLDER);
        File resultsDir = new File(RESULTS_FOLDER);

        if(!testDir.exists()) {
            try {
                testDir.mkdir();
            } catch (SecurityException se) {
                se.printStackTrace();
            }
        }

        if(!trainingDir.exists()) {
            try {
                trainingDir.mkdir();
            } catch (SecurityException se) {
                se.printStackTrace();
            }
        }

        if(!resultsDir.exists()) {
            try {
                resultsDir.mkdir();
            } catch (SecurityException se) {
                se.printStackTrace();
            }
        }
    }

    public static List<TestQuery> getQueriesForTesting() {

        List<TestQuery> queries = new ArrayList<>();

        queries.add(new TestQuery(" 1 = 1 ", "Corridor First Test"));

        return queries;
    }

    private static void wekaAnalysis(String testQuery, int testNumber, String queryNote) throws Exception {

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
                queryNote,
                evalSummary,
                evalClassDetails,
                evalConfusionMatrix,
                matrixCorrectMap,
                matrixIncorrectMap);
    }

    private static void writeResultFile(int testNumber, String testQuery, String queryNote, String summary, String classDetails, String confusionMatrix, Map<String, Double> matrixCorrectMap, Map<String, Double> matrixIncorrectMap) {

        try {
            String fileName = RESULTS_FOLDER + "result" + testNumber + ".txt";

            FileWriter outFile = new FileWriter(fileName);
            PrintWriter out = new PrintWriter(outFile);

            // Write text to file
            out.println("DATABASE :" + DATABASE_FILENAME);
            out.println("RESULT #" + testNumber);
            out.println("\nQUERY: \n" + testQuery);
            out.print("\nNOTE:\n" + queryNote + " \n \n");
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
