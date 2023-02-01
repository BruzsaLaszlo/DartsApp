package bruzsa.laszlo.dartsapp.model.cricket;

import androidx.annotation.NonNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import bruzsa.laszlo.dartsapp.dao.Player;
import lombok.Getter;

public class CricketTeam {

    private final Player player1;
    private Player player2;

    private final boolean teamPlay;
    @Getter
    private int points;
    private final Map<Integer, Integer> scores = new HashMap<>();

    public CricketTeam(Player... players) {
        player1 = players[0];
        teamPlay = players.length == 2 && players[1] != null;
        if (teamPlay)
            player2 = players[1];
    }

    public void addThrow(CricketThrow cricketThrow, Integer opponentMultiplier) {
        if (cricketThrow.isRemoved()) return;

        int number = cricketThrow.getValue();
        int multiplier = cricketThrow.getMultiply();

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

}
