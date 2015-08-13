/**
 * Created by ruibrito on 05/06/15.
 */
public class TestQuery {

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
    private static String Corridor = "L.vertex in (4038, 4039, 4040, 4041)\n" +
            "AND";
    private static String Rooms = "L.vertex in (4038, 4039, 4040, 4041, 4005, 4006, 4007, 4023, 4022)\n" +
            "AND";
    private static String Pool = "L.vertex in (2123, 2124, 2151, 2152, 2153, 2131, 2130)\n" +
            "AND";
    private static String Mensa = "L.vertex in (4137, 4138, 4139, 4141, 4142, 4143, 4144)\n" +
            "AND";
    private String type;
    private int testNumber;
    private String query;
    private String conditions;
    private String note;


    public TestQuery() {
    }

    public TestQuery(int testNumber, String type, String query, String conditions, String note) {
        this.type = type;
        this.testNumber = testNumber;
        this.query = initialQuery + where + switchType(type) + query;
        this.conditions = conditions;
        this.note = note;
    }

    private String switchType(String type) {
        switch (type) {
            case "Corridor":
                return Corridor;
            case "Rooms":
                return Rooms;
            case "Pool":
                return Pool;
            case "Mensa":
                return Mensa;
            default:
                return "";
        }
    }

    public String getCondtions() {
        return conditions;
    }

    public void setCondtions(String conditions) {
        this.conditions = conditions;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public int getTestNumber() {
        return testNumber;
    }

    public void setTestNumber(int testNumber) {
        this.testNumber = testNumber;
    }
}
