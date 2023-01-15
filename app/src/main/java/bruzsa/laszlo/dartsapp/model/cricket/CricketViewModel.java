package bruzsa.laszlo.dartsapp.model.cricket;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import bruzsa.laszlo.dartsapp.dao.Player;
import bruzsa.laszlo.dartsapp.ui.cricket.CricketThrow;

public class CricketViewModel extends ViewModel {

    private CricketPlayer player1 = new CricketPlayer("p1");
    private CricketPlayer player2 = new CricketPlayer("p2");
    private final List<CricketThrow> shoots = new ArrayList<>();
    private final MutableLiveData<Boolean> isGameOver = new MutableLiveData<>();

    public void newGame(Player player1, Player player2) {
        this.player1 = new CricketPlayer(player1.getName());
        this.player2 = new CricketPlayer(player2.getName());
        shoots.clear();
        isGameOver.setValue(false);
    }

    public void newThrow(int multiplier, int value, int player) {
        if (Boolean.TRUE.equals(isGameOver.getValue())) return;
        shoots.add(new CricketThrow(multiplier, value, player == 1 ? player1 : player2));
        updatePoints();
    }

    public String updatePoints() {
        player1.clearPoints();
        player2.clearPoints();
        reCalculatePoints();
        isGameEnd();
        return player1.getPoints() + " : " + player2.getPoints();
    }

    private void isGameEnd() {
        boolean player1ThrowAll = player1.getScores().size() == 7 &&
                player1.getScores().values().stream().allMatch(m -> m >= 3);
        boolean player2ThrowAll = player2.getScores().size() == 7 &&
                player2.getScores().values().stream().allMatch(m -> m >= 3);
        if ((player1ThrowAll && player1.getPoints() > player2.getPoints()) ||
                (player2ThrowAll && player2.getPoints() > player1.getPoints()))
            isGameOver.setValue(true);
    }

    public void shootRemove(CricketThrow cricketThrow) {
        shoots.remove(cricketThrow);
    }

    private void reCalculatePoints() {
        for (CricketThrow shoot : shoots) {
            CricketPlayer opponent = shoot.getPlayer() == player1 ? player2 : player1;
            shoot.getPlayer().addThrow(shoot, opponent.getScores().get(shoot.getValue()));
        }
    }

    public List<CricketThrow> getThrows() {
        return Collections.unmodifiableList(shoots);
    }

    public boolean isGameNotStarted() {
        return shoots.isEmpty();
    }

    public Map<Integer, Integer> getPlayerScore(int player) {
        return player == 1 ? player1.getScores() : player2.getScores();
    }

    public LiveData<Boolean> isGameOver() {
        return isGameOver;
    }
}