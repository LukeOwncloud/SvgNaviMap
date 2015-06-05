/**
 * Created by ruibrito on 05/06/15.
 */
public class TestQuery {

    private String query;
    private String note;

    private static String initialQuery = "SELECT F.fp_ID, M.mac_ID, M.bssid, M.strength, L.vertex, F.date\n" +
            "  FROM \n" +
            "      Fingerprint AS F \n" +
            "          INNER JOIN\n" +
            "      Location AS L\n" +
            "          on F.fp_Location = L.loc_ID\n" +
            "          INNER JOIN\n" +
            "      Mac as M\n" +
            "          on M.mac_FP = F.fp_ID\n";

    private static String where = "WHERE ";

    public TestQuery() {
    }

    public TestQuery(String query, String note) {
        this.query = initialQuery + where + query;
        this.note = note;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = initialQuery + where + query;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
