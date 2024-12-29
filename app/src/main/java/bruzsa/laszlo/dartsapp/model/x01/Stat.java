package bruzsa.laszlo.dartsapp.model.x01;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import lombok.Getter;

public class Stat {

    private final int startScore;
    private final int actualLeg;
    @Getter
    private int max;
    @Getter
    private int min = Integer.MAX_VALUE;
    @Getter
    private int plus100;
    @Getter
    private int plus60;
    private int dartCount;
    private int sum;
    private int hc;
    private final Map<Integer, Integer> legSum = new TreeMap<>();

    public Stat(int startScore, int actualLeg, List<X01Throw> throwList) {
        this.startScore = startScore;
        this.actualLeg = actualLeg;
        throwList.forEach(this::add);
    }

    private void add(X01Throw x01Throw) {
        if (x01Throw.isRemoved()) return;

        dartCount += x01Throw.getDartCount();

        if (!x01Throw.isValid()) return;

        int value = x01Throw.getValue();

        if (max < value) max = value;
        if (min > value) min = value;

        if (value >= 100) plus100++;
        else if (value >= 60) plus60++;

        sum += value;

        if (x01Throw.isCheckout() && hc < value) hc = value;

        legSum.compute(x01Throw.getLeg(), (k, v) -> v == null ? value : v + value);
    }

    public double getAverage() {
        return 3.0 * sum / dartCount;
    }

    public Optional<Integer> getHighestCheckout() {
        return hc == 0 ? Optional.empty() : Optional.of(hc);
    }

    public int getScore() {
        //noinspection ConstantConditions
        return startScore - legSum.getOrDefault(actualLeg, 0);
    }

    public boolean isEmpty() {
        return dartCount == 0;
    }

    public static Stat getEmptyStat(int startScore) {
        return new Stat(startScore, 0, Collections.emptyList());
    }

}
