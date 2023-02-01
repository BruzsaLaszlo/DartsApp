package bruzsa.laszlo.dartsapp.model.singlex01;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bruzsa.laszlo.dartsapp.dao.Player;
import bruzsa.laszlo.dartsapp.model.x01.X01SummaryStatistics;
import bruzsa.laszlo.dartsapp.model.x01.X01Throw;
import bruzsa.laszlo.dartsapp.ui.x01.X01ThrowAdapter;

public class SingleX01ViewModel extends ViewModel {

    private Player player;
    private final MutableLiveData<Integer> leg = new MutableLiveData<>(0);
    private int startScore;
    private final MutableLiveData<Integer> score = new MutableLiveData<>();
    private final List<X01Throw> throwsList = new ArrayList<>();
    private final X01ThrowAdapter throwAdapter = new X01ThrowAdapter(throwsList, this::removeThrow);
    private final X01SummaryStatistics stat = new X01SummaryStatistics(throwsList);

    public void start(Player player, int startScore) {
        this.player = player;
        this.startScore = startScore;
        score.setValue(startScore);
    }

    public X01Throw newThrow(int throwValue, int dartCount) {
        int newScore = getScore().getValue() - throwValue;
        boolean checkedOut = newScore == 0;
        X01Throw newThrow = new X01Throw(
                throwValue, newScore > 1 || checkedOut, leg(), dartCount, checkedOut, player);
        throwsList.add(newThrow);
        if (checkedOut) {
            leg.setValue(leg() + 1);
        }
        score.setValue(recalculateScore());
        throwAdapter.inserted();

        return newThrow;
    }

    // only test
    public X01Throw newThrow(int throwValue) {
        return newThrow(throwValue, 3);
    }

    public boolean removeThrow(X01Throw x01Throw) {
        if (x01Throw.getLeg() == leg() && !x01Throw.isRemoved()) {
            x01Throw.setRemoved();
            score.setValue(recalculateScore());
            return true;
        }
        return false;
    }

    public X01SummaryStatistics getStat() {
        return stat;
    }

    private int recalculateScore() {
        return startScore - stat.getSum(leg());
    }

    public LiveData<Integer> getScore() {
        return score;
    }

    public List<X01Throw> getThrowsList() {
        return Collections.unmodifiableList(throwsList);
    }

    public LiveData<Integer> getLegs() {
        return leg;
    }

    private Integer leg() {
        //noinspection ConstantConditions
        return leg.getValue();
    }

    public X01ThrowAdapter getThrowsAdapter() {
        return throwAdapter;
    }
}