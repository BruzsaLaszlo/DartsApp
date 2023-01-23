package bruzsa.laszlo.dartsapp.model.dartsX01;

import androidx.annotation.NonNull;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

public class X01SummaryStatistics {

    private final List<X01Throw> throwList;

    public X01SummaryStatistics(List<X01Throw> throwList) {
        this.throwList = throwList;
    }

    @NonNull
    private Stream<X01Throw> validAndNotHandicapThrows() {
        return throwList.stream()
                .filter(X01Throw::isValid)
                .filter(X01Throw::isNotHandicap);
    }

    @NonNull
    private Stream<X01Throw> noHandicapThrows() {
        return throwList.stream().filter(X01Throw::isNotHandicap);
    }

    @NonNull
    @Override
    public String toString() {
        if (validAndNotHandicapThrows().count() == 0) return "";
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
                getHighestCheckout().map(Object::toString).orElse(""));
    }

    public double getAverage() {
        return getSum() / (getDartCount() / 3d);
    }

    public int getSum() {
        return validAndNotHandicapThrows()
                .mapToInt(X01Throw::getThrow)
                .sum();
    }

    public int getSum(int leg) {
        return throwList.stream()
                .filter(X01Throw::isValid)
                .filter(x01Throw -> x01Throw.getLeg() == leg)
                .mapToInt(X01Throw::getThrow)
                .sum();
    }

    public int getSixtyPlus() {
        return (int) validAndNotHandicapThrows()
                .filter(value -> value.getThrow() >= 60)
                .count();
    }

    public int getHundredPlus() {
        return (int) validAndNotHandicapThrows()
                .filter(value -> value.getThrow() >= 100)
                .count();
    }

    public IntSummaryStatistics getAllStat() {
        return validAndNotHandicapThrows()
                .mapToInt(X01Throw::getThrow)
                .summaryStatistics();
    }

    public Optional<Integer> getHighestCheckout() {
        return validAndNotHandicapThrows()
                .filter(X01Throw::isCheckout)
                .map(X01Throw::getThrow)
                .max(Integer::compareTo);
    }

    public int getThrowCountExceptHandicap() {
        return (int) noHandicapThrows().count();
    }

    public int getDartCount() {
        return noHandicapThrows()
                .mapToInt(X01Throw::getDart)
                .sum();
    }
}
