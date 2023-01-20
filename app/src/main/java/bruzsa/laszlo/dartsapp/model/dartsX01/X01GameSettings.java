package bruzsa.laszlo.dartsapp.model.dartsX01;

public class X01GameSettings {

    private int legSetOf = 1;
    private int handicap = 181;
    private int startScore = 501;
    private X01MatchType x01MatchType = X01MatchType.SINGLE_LEG;

    public X01GameSettings(int legSetOf, int handicap, int startScore, X01MatchType x01MatchType) {
        this.legSetOf = legSetOf;
        this.handicap = handicap;
        this.startScore = startScore;
        this.x01MatchType = x01MatchType;
    }

    private X01GameSettings() {
    }

    public static X01GameSettings getDefault() {
        return new X01GameSettings();
    }

    public int getLegSetOf() {
        return legSetOf;
    }

    public X01MatchType getDartsX01MatchType() {
        return x01MatchType;
    }

    public int getHandicap() {
        return handicap;
    }

    public int getStartScore() {
        return startScore;
    }
}
