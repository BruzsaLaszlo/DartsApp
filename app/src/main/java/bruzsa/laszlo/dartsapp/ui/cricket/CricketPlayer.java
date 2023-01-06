package bruzsa.laszlo.dartsapp.ui.cricket;

import androidx.annotation.NonNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CricketPlayer {

    private long id;
    private final String name;
    private int points;
    private final Map<Integer, Integer> scores = new HashMap<>();

    public CricketPlayer(String name) {
        this.name = name.substring(0, 2);
    }

    public void addShoot(CricketShoot shoot, Integer opponentMultiplier) {
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
        return name;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }
}
