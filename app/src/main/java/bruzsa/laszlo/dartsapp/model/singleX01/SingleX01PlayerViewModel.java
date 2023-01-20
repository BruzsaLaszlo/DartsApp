package bruzsa.laszlo.dartsapp.model.singleX01;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import bruzsa.laszlo.dartsapp.dao.Player;
import bruzsa.laszlo.dartsapp.model.dartsX01.X01SummaryStatistics;
import bruzsa.laszlo.dartsapp.model.dartsX01.X01Throw;

public class SingleX01PlayerViewModel extends ViewModel {

    private Player player;
    private int leg;
    private int startScore;
    private List<X01Throw> throwsList = new ArrayList<>();
    private final X01SummaryStatistics stat = new X01SummaryStatistics(throwsList);

    public void start(Player player, int startScore) {
        this.player = player;
        this.startScore = startScore;
    }

    public int newThrow(int throwValue) {
        int newScore = getScore() - throwValue;
        X01Throw newThrow = new X01Throw(throwValue, newScore > 1 || newScore == 0);
        throwsList.add(newThrow);
        return getScore();
    }

    public String getStat() {
        return stat.toString();
    }

    public Integer getScore() {
        return startScore - stat.getSum();
    }

}