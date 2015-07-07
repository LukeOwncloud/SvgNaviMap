package fixer;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruibrito on 06/07/15.
 */
public class DatabaseFixer {

    private static String DATABASE_FILENAME = "fingerprintsDB_06-07-2015 18:32:51.db3";
    private static String DATABASE_LOCATION = "resources/Databases/" + DATABASE_FILENAME + "/";

    private static String allDatesQuery = "SELECT fp_ID, date FROM Fingerprint";

    public static void main(String[] args) throws SQLException {

        Connection connection = null;
        Statement statement = null;

        ResultSet resultSet = null;
        try {
            // Load the sqlite-JDBC driver using the current class loader
            Class.forName("org.sqlite.JDBC");

            connection = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_LOCATION);

            statement = connection.createStatement();
//            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
//                    ResultSet.CONCUR_UPDATABLE);

            resultSet = statement.executeQuery(allDatesQuery);

            List<FixerResult> fixerResults = new ArrayList<>();
            List<String> queries = new ArrayList<>();

            while (resultSet.next()) {
                int fingerprint = resultSet.getInt("fp_ID");
                String date = resultSet.getString("date");

                String newDate = formatDate(date);

//                FixerResult temp = new FixerResult(fingerprint, newDate);

//                fixerResults.add(temp);

//                resultSet.updateString("date", newDate);
//                resultSet.updateRow();

                String sql = "UPDATE Fingerprint SET date = '"
                        + newDate +
                        "' WHERE fp_ID = " + fingerprint + ";";

                queries.add(sql);
            }


            PreparedStatement st1 = connection.prepareStatement(
                            "UPDATE Fingerprint " +
                            "SET date = ? " +
                            "WHERE fp_ID = ?");



            int i = 0;
            for (String s : queries) {

                int execute = statement.executeUpdate(s);

                System.out.println(i + " - " + execute);
                i++;
            }
//            st1.close();


        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {

        }
    }

    private static String formatDate(String inputDate) throws ParseException {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        java.util.Date date = inputFormat.parse(inputDate);
        String formatDate = outputFormat.format(date);

        return formatDate;
    }
}
