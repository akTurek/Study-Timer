package sample.stimer;

public class PredmetHolder {
    private String predmet;
    private final static PredmetHolder INSTANCE = new PredmetHolder();

    private PredmetHolder() {}

    public static PredmetHolder getInstance() {
        return INSTANCE;
    }

    public void setPredmet(String predmet) {
        this.predmet = predmet;
    }

    public String getPredmet() {
        return this.predmet;
    }
}
