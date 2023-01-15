package bruzsa.laszlo.dartsapp.model.darts501;

public class Darts501GameSettings {

    private int legSetOf = 1;
    private   int handicap = 181;
    private int startScore = 501;
    private Darts501MatchType darts501MatchType = Darts501MatchType.SINGLE_LEG;

    public Darts501GameSettings(int legSetOf, int handicap, int startScore, Darts501MatchType darts501MatchType) {
        this.legSetOf = legSetOf;
        this.handicap = handicap;
        this.startScore = startScore;
        this.darts501MatchType = darts501MatchType;
    }

    private Darts501GameSettings() {
    }

    public static Darts501GameSettings getDefault() {
        return new Darts501GameSettings();
    }

    public int getLegSetOf() {
        return legSetOf;
    }

    public Darts501MatchType getDarts501MatchType() {
        return darts501MatchType;
    }

    public int getHandicap() {
        return handicap;
    }

    public int getStartScore() {
        return startScore;
    }
}
