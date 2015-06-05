import java.util.HashMap;
import java.util.List;

/**
 * Created by ruibrito on 04/06/15.
 */
public class WekaResultSet {

    private HashMap<Integer, List<SingleDBResult>> testSet;
    private HashMap<Integer, List<SingleDBResult>> trainingSet;

    public WekaResultSet() {
    }

    public WekaResultSet(HashMap<Integer, List<SingleDBResult>> testSet, HashMap<Integer, List<SingleDBResult>> trainingSet) {
        this.testSet = testSet;
        this.trainingSet = trainingSet;
    }

    public HashMap<Integer, List<SingleDBResult>> getTestSet() {
        return testSet;
    }

    public void setTestSet(HashMap<Integer, List<SingleDBResult>> testSet) {
        this.testSet = testSet;
    }

    public HashMap<Integer, List<SingleDBResult>> getTrainingSet() {
        return trainingSet;
    }

    public void setTrainingSet(HashMap<Integer, List<SingleDBResult>> trainingSet) {
        this.trainingSet = trainingSet;
    }
}

