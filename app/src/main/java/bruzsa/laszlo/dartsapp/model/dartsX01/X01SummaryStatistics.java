package bruzsa.laszlo.dartsapp.model.dartsX01;

import androidx.annotation.NonNull;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class X01SummaryStatistics {

    private final List<X01Throw> throwList;

    public X01SummaryStatistics(List<X01Throw> throwList) {
        this.throwList = throwList;
    }

    @NonNull
    private List<X01Throw> validAndNotHandicapThrows() {
        return throwList.stream()
                .filter(dartsX01Throw -> dartsX01Throw.isValid() && dartsX01Throw.isNotHandicap())
                .collect(Collectors.toList());
    }

    @NonNull
    @Override
    public String toString() {
        if (validAndNotHandicapThrows().isEmpty()) return "";
        var allStat = getAllStat();
        return String.format(Locale.ENGLISH, """
                        %d
                        %d
                        %d
                        %d""",
                getSixtyPlus(),
                (int) allStat.getAverage(),
                allStat.getMax(),
                allStat.getMin());
    }

    public int getSum() {
        return throwList.stream()
                .filter(X01Throw::isValid)
                .mapToInt(X01Throw::getThrow)
                .sum();
    }

    public int getSixtyPlus() {
        return (int) validAndNotHandicapThrows().stream()
                .filter(value -> value.getThrow() >= 60)
                .count();
    }

    public int getHundredPlus() {
        return (int) validAndNotHandicapThrows().stream()
                .filter(value -> value.getThrow() >= 100)
                .count();
    }

    public IntSummaryStatistics getAllStat() {
        return validAndNotHandicapThrows().stream()
                .mapToInt(X01Throw::getThrow)
                .summaryStatistics();
    }

}
