import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ruibrito on 04/06/15.
 */
public class WekaResultSet {

    private Map<Integer, List<QueryRowResult>> testSet;
    private Map<Integer, List<QueryRowResult>> trainingSet;

    public WekaResultSet() {
    }

    public WekaResultSet(HashMap<Integer, List<QueryRowResult>> testSet, HashMap<Integer, List<QueryRowResult>> trainingSet) {
        this.testSet = testSet;
        this.trainingSet = trainingSet;
    }

    public Map<Integer, List<QueryRowResult>> getTestSet() {
        return testSet;
    }

    public void setTestSet(Map<Integer, List<QueryRowResult>> testSet) {
        this.testSet = testSet;
    }

    public Map<Integer, List<QueryRowResult>> getTrainingSet() {
        return trainingSet;
    }

    public void setTrainingSet(Map<Integer, List<QueryRowResult>> trainingSet) {
        this.trainingSet = trainingSet;
    }
}

