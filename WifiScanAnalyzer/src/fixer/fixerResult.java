package fixer;

/**
 * Created by ruibrito on 06/07/15.
 */
public class FixerResult {

    private int fp;
    private String date;

    public FixerResult(int fp, String date) {
        this.fp = fp;
        this.date = date;
    }

    public int getFp() {
        return fp;
    }

    public void setFp(int fp) {
        this.fp = fp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
