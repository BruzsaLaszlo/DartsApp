package bruzsa.laszlo.dartsapp.model.x01;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.util.DoubleComparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class X01SummaryStatisticsTest {

    static final int START_SCORE = 501;

    List<X01SummaryStatistics> stats = List.of(
            new X01SummaryStatistics(List.of(
                    new X01Throw(180, true, 0, 3, false, null),
                    new X01Throw(180, true, 0, 3, false, null),
                    new X01Throw(180, false, 0, 3, false, null),
                    new X01Throw(141, true, 0, 3, true, null)), START_SCORE),
            new X01SummaryStatistics(List.of(
                    new X01Throw(180, true, 0, 3, false, null),
                    new X01Throw(180, true, 0, 3, false, null),
                    new X01Throw(101, true, 0, 3, false, null),
                    new X01Throw(40, true, 0, 1, true, null)), START_SCORE),
            new X01SummaryStatistics(List.of(
                    new X01Throw(170, true, 0, 3, false, null),
                    new X01Throw(170, true, 0, 3, false, null),
                    new X01Throw(161, true, 0, 3, true, null),
                    new X01Throw(100, true, 1, 3, false, null)), START_SCORE),
            new X01SummaryStatistics(List.of(
                    new X01Throw(60, true, 0, 3, false, null),
                    new X01Throw(60, true, 0, 3, false, null),
                    new X01Throw(26, true, 0, 3, false, null)), START_SCORE)
    );

    @BeforeEach
    void setUp() {
    }

    @Test
    void getAllStat() {
        assertThat(stats.stream().map(X01SummaryStatistics::getMax).collect(Collectors.toList()))
                .usingComparatorForType(new DoubleComparator(0.9), Double.class)
                .containsSequence(180, 180, 170, 60);
        assertThat(stats.stream().map(X01SummaryStatistics::getMin).collect(Collectors.toList()))
                .usingComparatorForType(new DoubleComparator(0.9), Double.class)
                .containsSequence(141, 40, 100, 26);
    }

    @Test
    void getAvg() {
        assertThat(stats.stream().map(X01SummaryStatistics::getAverage).collect(Collectors.toList()))
                .usingComparatorForType(new DoubleComparator(0.9), Double.class)
                .containsSequence(501 / 12d * 3, 501 / 10d * 3, 601 / 12d * 3, 146 / 9d * 3);
    }

    @Test
    void getSum() {
        final int leg0 = 0;
        assertThat(stats.stream().map(x01SummaryStatistics -> x01SummaryStatistics.getSum(leg0)).collect(Collectors.toList()))
                .containsExactly(501, 501, 501, 146);
        final int leg1 = 1;
        assertThat(stats.stream().map(x01SummaryStatistics -> x01SummaryStatistics.getSum(leg1)).collect(Collectors.toList()))
                .containsExactly(0, 0, 100, 0);
    }

    @Test
    void getSixtyPlus() {
        assertThat(stats.stream().map(X01SummaryStatistics::getSixtyPlus).collect(Collectors.toList()))
                .containsExactly(3, 3, 4, 2);
    }

    @Test
    void getHundredPlus() {
        assertThat(stats.stream().map(X01SummaryStatistics::getHundredPlus).collect(Collectors.toList()))
                .containsExactly(3, 3, 4, 0);
    }

    @Test
    void getHighestCheckout() {
        assertThat(stats.stream().map(X01SummaryStatistics::getHighestCheckout).collect(Collectors.toList()))
                .containsExactly(Optional.of(141), Optional.of(40), Optional.of(161), Optional.empty());
    }

    @Test
    void getThrowCountExceptHandicap() {
        assertThat(stats.stream().map(X01SummaryStatistics::getThrowCountExceptHandicap).collect(Collectors.toList()))
                .containsExactly(4, 4, 4, 3);
    }

    @Test
    void getDartCount() {
        assertThat(stats.stream().map(X01SummaryStatistics::getDartCount).collect(Collectors.toList()))
                .containsExactly(12, 10, 12, 9);
    }
}