package bruzsa.laszlo.dartsapp.model.darts501;

import androidx.annotation.NonNull;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class Darts501SummaryStatistics {

    private final List<Darts501Throw> throwList;

    public Darts501SummaryStatistics(List<Darts501Throw> throwList) {
        this.throwList = throwList;
    }

    @NonNull
    private List<Darts501Throw> validAndNotHandicapThrows() {
        return throwList.stream()
                .filter(darts501Throw -> darts501Throw.isValid() && darts501Throw.isNotHandicap())
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
                .filter(Darts501Throw::isValid)
                .mapToInt(Darts501Throw::getThrow)
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
                .mapToInt(Darts501Throw::getThrow)
                .summaryStatistics();
    }

}
