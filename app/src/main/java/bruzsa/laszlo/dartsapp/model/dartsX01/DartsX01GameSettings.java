package bruzsa.laszlo.dartsapp.model.dartsX01;

public class DartsX01GameSettings {

    private int legSetOf = 1;
    private int handicap = 181;
    private int startScore = 501;
    private DartsX01MatchType dartsX01MatchType = DartsX01MatchType.SINGLE_LEG;

    public DartsX01GameSettings(int legSetOf, int handicap, int startScore, DartsX01MatchType dartsX01MatchType) {
        this.legSetOf = legSetOf;
        this.handicap = handicap;
        this.startScore = startScore;
        this.dartsX01MatchType = dartsX01MatchType;
    }

    private DartsX01GameSettings() {
    }

    public static DartsX01GameSettings getDefault() {
        return new DartsX01GameSettings();
    }

    public int getLegSetOf() {
        return legSetOf;
    }

    public DartsX01MatchType getDartsX01MatchType() {
        return dartsX01MatchType;
    }

    public int getHandicap() {
        return handicap;
    }

    public int getStartScore() {
        return startScore;
    }
}
