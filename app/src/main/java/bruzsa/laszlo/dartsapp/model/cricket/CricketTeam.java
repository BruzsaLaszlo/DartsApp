package bruzsa.laszlo.dartsapp.model.cricket;

import androidx.annotation.NonNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import bruzsa.laszlo.dartsapp.dao.Player;

public class CricketTeam {

    private final Player player1;
    private Player player2;

    private final boolean teamPlay;
    private int points;
    private final Map<Integer, Integer> scores = new HashMap<>();

    public CricketTeam(Player... players) {
        player1 = players[0];
        teamPlay = players.length == 2 && players[1] != null;
        if (teamPlay)
            player2 = players[1];
    }

    public void addThrow(CricketThrow shoot, Integer opponentMultiplier) {
        if (shoot.isRemoved()) return;

        int number = shoot.getValue();
        int multiplier = shoot.getMultiply();

        scores.computeIfPresent(number, (n, m) -> {
            int sum = m + multiplier;
            if (sum > 3 && (opponentMultiplier == null || opponentMultiplier < 3)) {
                int sm = m >= 3 ? multiplier : sum - 3;
                points += sm * number;
            }
            return sum;
        });

        scores.putIfAbsent(number, multiplier);
    }

    public Map<Integer, Integer> getScores() {
        return Collections.unmodifiableMap(scores);
    }

    public void clearPoints() {
        scores.clear();
        points = 0;
    }

    @NonNull
    @Override
    public String toString() {
        return player1.getNickName() + (teamPlay ? player2.getNickName() : "");
    }

    public int getPoints() {
        return points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CricketTeam that)) return false;
        if (!super.equals(o)) return false;

        if (getPoints() != that.getPoints()) return false;
        return getScores() != null ? getScores().equals(that.getScores()) : that.getScores() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getPoints();
        result = 31 * result + (getScores() != null ? getScores().hashCode() : 0);
        return result;
    }
}
