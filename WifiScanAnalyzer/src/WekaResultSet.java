import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ruibrito on 04/06/15.
 */
public class WekaResultSet {

    private Map<Integer, List<SingleDBResult>> testSet;
    private Map<Integer, List<SingleDBResult>> trainingSet;

    public WekaResultSet() {
    }

    public WekaResultSet(HashMap<Integer, List<SingleDBResult>> testSet, HashMap<Integer, List<SingleDBResult>> trainingSet) {
        this.testSet = testSet;
        this.trainingSet = trainingSet;
    }

    public Map<Integer, List<SingleDBResult>> getTestSet() {
        return testSet;
    }

    public void setTestSet(Map<Integer, List<SingleDBResult>> testSet) {
        this.testSet = testSet;
    }

    public Map<Integer, List<SingleDBResult>> getTrainingSet() {
        return trainingSet;
    }

    public void setTrainingSet(Map<Integer, List<SingleDBResult>> trainingSet) {
        this.trainingSet = trainingSet;
    }
}

