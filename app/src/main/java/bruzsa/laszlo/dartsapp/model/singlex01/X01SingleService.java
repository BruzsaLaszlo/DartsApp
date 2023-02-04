package bruzsa.laszlo.dartsapp.model.singlex01;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

import bruzsa.laszlo.dartsapp.dao.Player;
import bruzsa.laszlo.dartsapp.model.x01.X01SummaryStatistics;
import bruzsa.laszlo.dartsapp.model.x01.X01Throw;
import lombok.Getter;

public class X01SingleService {

    private final Player player;
    private final int startScore;
    @Getter
    private int leg;
    private final List<X01Throw> throwsList = new ArrayList<>();
    @Getter
    private final X01SummaryStatistics stat;

    public X01SingleService(Player player, int startScore) {
        this.player = player;
        this.startScore = startScore;
        stat = new X01SummaryStatistics(throwsList, startScore);
    }

    public void newThrow(int throwValue, int dartCount, BiConsumer<Integer, Integer> andThen) {
        int newScore = stat.getScore(leg) - throwValue;
        boolean checkedOut = newScore == 0;
        X01Throw newThrow = new X01Throw(
                throwValue, newScore > 1 || checkedOut, leg, dartCount, checkedOut, player);
        throwsList.add(newThrow);
        if (checkedOut) andThen.accept(++leg, startScore);
        else andThen.accept(leg, newScore);
    }

    public boolean removeThrow(X01Throw x01Throw) {
        boolean removable = x01Throw.getLeg() == leg && !x01Throw.isRemoved();
        if (removable) {
            x01Throw.setRemoved();
        }
        return removable;
    }

    public List<X01Throw> getThrowsList() {
        return Collections.unmodifiableList(throwsList);
    }

    public int getScore() {
        return stat.getScore(leg);
    }
}
