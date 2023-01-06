package bruzsa.laszlo.dartsapp.ui.darts501;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Locale;

public class Darts501SummaryStatistics {

    private int score = 501;
    private double average;
    private int max;
    private int min;
    private int sixtyPlus;
    private int hundredPlus;
    private int count;
    private boolean out;

    public Darts501SummaryStatistics(List<Darts501Shoot> shoots) {
        if (shoots.isEmpty()) return;
        long count60 = shoots.stream()
                .filter(darts501Shoot -> darts501Shoot.isValid() && darts501Shoot.isNotHandicap())
                .mapToInt(Darts501Shoot::getShoot)
                .filter(value -> value >= 60)
                .count();
        long count100 = shoots.stream()
                .filter(darts501Shoot -> darts501Shoot.isValid() && darts501Shoot.isNotHandicap())
                .mapToInt(Darts501Shoot::getShoot)
                .filter(value -> value >= 60)
                .count();
        IntSummaryStatistics stat = shoots.stream()
                .filter(darts501Shoot -> darts501Shoot.isValid() && darts501Shoot.isNotHandicap())
                .mapToInt(Darts501Shoot::getShoot)
                .summaryStatistics();

        score = (int) (501 - shoots.stream()
                .filter(Darts501Shoot::isValid)
                .mapToInt(Darts501Shoot::getShoot)
                .sum());
        average = stat.getAverage();
        max = stat.getMax();
        min = stat.getMin();
        sixtyPlus = (int) count60;
        hundredPlus = (int) count100;
        count = (int) stat.getCount();
        out = (score > 1 && score < 159) || List.of(160, 161, 164, 167, 170).contains(score);
    }

    public String getStat() {
        return String.format(Locale.ENGLISH, """
                %d
                %d
                %d
                %d""", sixtyPlus, (int) average, max, min);
    }

    public int getScore() {
        return score;
    }

    public double getAverage() {
        return average;
    }

    public int getMax() {
        return max;
    }

    public int getMin() {
        return min;
    }

    public int getSixtyPlus() {
        return sixtyPlus;
    }

    public int getHundredPlus() {
        return hundredPlus;
    }

    public int getCount() {
        return count;
    }

    public boolean isOut() {
        return out;
    }
}
