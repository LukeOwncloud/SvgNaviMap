import java.util.*;

/**
 * Created by ruibrito on 04/06/15.
 */
public class JavaInternalDatabase {

    private Map<String, Set<Integer>> oneLocationToAllFingerprintsMap;
    private Map<Integer, List<QueryRowResult>> oneFingerPrintToAllMacsMap;
    private List<String> allRooms;
    private List<String> allBSSIDs;

    public JavaInternalDatabase() {
        oneLocationToAllFingerprintsMap = new HashMap<>();
        oneFingerPrintToAllMacsMap = new HashMap<>();
        allBSSIDs = new ArrayList<>();
        allRooms = new ArrayList<>();
    }

    public JavaInternalDatabase(Map<String, Set<Integer>> oneLocationToAllFingerprintsMap, Map<Integer, List<QueryRowResult>> oneFingerPrintToAllMacsMap, List<String> allRooms, List<String> allBSSIDs) {
        this.oneLocationToAllFingerprintsMap = oneLocationToAllFingerprintsMap;
        this.oneFingerPrintToAllMacsMap = oneFingerPrintToAllMacsMap;
        this.allRooms = allRooms;
        this.allBSSIDs = allBSSIDs;
    }

    public Map<String, Set<Integer>> getOneLocationToAllFingerprintsMap() {
        return oneLocationToAllFingerprintsMap;
    }

    public void setOneLocationToAllFingerprintsMap(Map<String, Set<Integer>> oneLocationToAllFingerprintsMap) {
        this.oneLocationToAllFingerprintsMap = oneLocationToAllFingerprintsMap;
    }

    public Map<Integer, List<QueryRowResult>> getOneFingerPrintToAllMacsMap() {
        return oneFingerPrintToAllMacsMap;
    }

    public void setOneFingerPrintToAllMacsMap(Map<Integer, List<QueryRowResult>> oneFingerPrintToAllMacsMap) {
        this.oneFingerPrintToAllMacsMap = oneFingerPrintToAllMacsMap;
    }

    public List<String> getAllRooms() {
        return allRooms;
    }

    public void setAllRooms(List<String> allRooms) {
        this.allRooms = allRooms;
    }

    public List<String> getAllBSSIDs() {
        return allBSSIDs;
    }

    public void setAllBSSIDs(List<String> allBSSIDs) {
        this.allBSSIDs = allBSSIDs;
    }
}


