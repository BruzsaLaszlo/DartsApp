package bruzsa.laszlo.dartsapp.model.x01;

import androidx.annotation.NonNull;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

public class X01SummaryStatistics {

    private final List<X01Throw> throwList;
    private final int startScore;

    public X01SummaryStatistics(List<X01Throw> throwList, int startScore) {
        this.throwList = throwList;
        this.startScore = startScore;
    }

    @NonNull
    private Stream<X01Throw> validThrows() {
        return notRemovedThrows().filter(X01Throw::isValid);
    }

    @NonNull
    private Stream<X01Throw> notRemovedThrows() {
        return throwList.stream().filter(x01Throw -> x01Throw.isNotRemoved());
    }

    @NonNull
    @Override
    public String toString() {
        if (validThrows().findAny().isEmpty()) return "";
        var allStat = getAllStat();
        return String.format(Locale.ENGLISH, """
                        %d
                        %d
                        %d
                        %d
                        %d
                        %s""",
                getHundredPlus(),
                getSixtyPlus(),
                (int) getAverage(),
                allStat.getMax(),
                allStat.getMin(),
                getHighestCheckout().map(Object::toString).orElse("-"));
    }

    public double getAverage() {
        return getSum() / (getDartCount() / 3d);
    }

    public int getSum() {
        return validThrows()
                .mapToInt(X01Throw::getValue)
                .sum();
    }

    public int getSum(int leg) {
        return validThrows()
                .filter(x01Throw -> x01Throw.getLeg() == leg)
                .mapToInt(X01Throw::getValue)
                .sum();
    }

    public int getSixtyPlus() {
        return (int) validThrows()
                .filter(value -> value.getValue() >= 60)
                .count();
    }

    public int getHundredPlus() {
        return (int) validThrows()
                .filter(value -> value.getValue() >= 100)
                .count();
    }

    private IntSummaryStatistics getAllStat() {
        return validThrows()
                .mapToInt(X01Throw::getValue)
                .summaryStatistics();
    }

    public Optional<Integer> getHighestCheckout() {
        return validThrows()
                .filter(X01Throw::isCheckout)
                .map(X01Throw::getValue)
                .max(Integer::compareTo);
    }

    public int getMax() {
        return getAllStat().getMax();
    }

    public int getMin() {
        return getAllStat().getMin();
    }

    public int getThrowCountExceptHandicap() {
        return (int) notRemovedThrows().count();
    }

    public int getDartCount() {
        return notRemovedThrows()
                .mapToInt(X01Throw::getDartCount)
                .sum();
    }

    public int getScore(int leg) {
        return startScore - getSum(leg);
    }

    public int getScoreLastLeg() {
        Optional<Integer> lastLeg = validThrows().map(X01Throw::getLeg).max(Integer::compareTo);
        return lastLeg.map(integer -> startScore - getSum(integer))
                .orElse(startScore);
    }
}
