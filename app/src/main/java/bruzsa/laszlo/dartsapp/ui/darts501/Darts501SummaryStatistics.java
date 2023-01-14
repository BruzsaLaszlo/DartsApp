package bruzsa.laszlo.dartsapp.ui.darts501;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class Darts501SummaryStatistics {

    private int score;
    private double average;
    private int max;
    private int min;
    private int sixtyPlus;
    private int hundredPlus;
    private int count;
    private boolean out;

    public Darts501SummaryStatistics(List<Darts501Throw> shoots, int startScore) {
        this.score = startScore;
        if (shoots.isEmpty()) return;
        List<Darts501Throw> countedScores = shoots.stream()
                .filter(darts501Throw -> darts501Throw.isValid() && darts501Throw.isNotHandicap())
                .collect(Collectors.toList());
        long count60 = countedScores.stream()
                .filter(value -> value.getThrow() >= 60)
                .count();
        long count100 = countedScores.stream()
                .filter(value -> value.getThrow() >= 100)
                .count();
        IntSummaryStatistics stat = countedScores.stream()
                .mapToInt(Darts501Throw::getThrow)
                .summaryStatistics();
        score = (int) (501 - shoots.stream()
                .filter(Darts501Throw::isValid)
                .mapToInt(Darts501Throw::getThrow)
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

    public boolean isEmpty() {
        return count == 0;
    }

}
