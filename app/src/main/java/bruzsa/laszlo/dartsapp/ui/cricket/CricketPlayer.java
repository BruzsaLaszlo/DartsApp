package bruzsa.laszlo.dartsapp.ui.cricket;

import androidx.annotation.NonNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import bruzsa.laszlo.dartsapp.dao.Player;

public class CricketPlayer extends Player {

    private int points;
    private final Map<Integer, Integer> scores = new HashMap<>();

    public CricketPlayer(String name) {
        super(name.substring(0, 2));
    }

    public void addThrow(CricketThrow shoot, Integer opponentMultiplier) {
        if (shoot.isRemoved()) return;

        int number = shoot.getValue();
        int multiplier = shoot.getMultiply();

        if (scores.containsKey(number)) {
            int m = scores.get(number);
            if (m + multiplier <= 3) {
                scores.replace(number, m + multiplier);
            } else {
                if (opponentMultiplier != null && opponentMultiplier >= 3) return;
                points += (m + multiplier - 3) * number;
                scores.replace(number, 3);
            }
        } else {
            scores.put(number, multiplier);
        }
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
        return getName();
    }

    public String getName() {
        return super.getName();
    }

    public int getPoints() {
        return points;
    }
}
