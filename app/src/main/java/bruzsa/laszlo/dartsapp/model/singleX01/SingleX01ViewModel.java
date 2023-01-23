package bruzsa.laszlo.dartsapp.model.singleX01;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bruzsa.laszlo.dartsapp.dao.Player;
import bruzsa.laszlo.dartsapp.model.dartsX01.X01SummaryStatistics;
import bruzsa.laszlo.dartsapp.model.dartsX01.X01Throw;

public class SingleX01ViewModel extends ViewModel {

    private Player player;
    private int leg;
    private int startScore;
    private final List<X01Throw> throwsList = new ArrayList<>();
    private final X01SummaryStatistics stat = new X01SummaryStatistics(throwsList);

    public void start(Player player, int startScore) {
        this.player = player;
        this.startScore = startScore;
    }

    public int newThrow(int throwValue, int dartCount) {
        int newScore = getScore() - throwValue;
        boolean checkedOut = newScore == 0;
        X01Throw newThrow = new X01Throw(
                throwValue, newScore > 1 || checkedOut, leg, dartCount, checkedOut);
        throwsList.add(newThrow);
        if (checkedOut) {
            leg++;
            return startScore;
        } else {
            return getScore();
        }
    }

    public int newThrow(int throwValue) {
        return newThrow(throwValue, 3);
    }

    public int removeThrow(X01Throw x01Throw) {
        int index = throwsList.indexOf(x01Throw);
        throwsList.remove(index);
        return index;
    }

    public String getStat() {
        return stat.toString();
    }

    public Integer getScore() {
        return startScore - stat.getSum(leg);
    }

    public List<X01Throw> getThrowsList() {
        return Collections.unmodifiableList(throwsList);
    }
}