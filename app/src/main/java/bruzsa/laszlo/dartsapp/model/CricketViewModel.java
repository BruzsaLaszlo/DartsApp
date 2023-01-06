package bruzsa.laszlo.dartsapp.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import bruzsa.laszlo.dartsapp.ui.cricket.CricketPlayer;
import bruzsa.laszlo.dartsapp.ui.cricket.CricketShoot;

public class CricketViewModel extends ViewModel {

    private CricketPlayer player1 = new CricketPlayer("p1");
    private CricketPlayer player2 = new CricketPlayer("p2");
    private final List<CricketShoot> shoots = new ArrayList<>();
    private final MutableLiveData<Boolean> isGameOver = new MutableLiveData<>();

    public void newGame(CricketPlayer player1, CricketPlayer player2) {
        this.player1 = player1;
        this.player2 = player2;
        shoots.clear();
        isGameOver.setValue(false);
    }

    public void newShoot(int multiplier, int value, int player) {
        if (Boolean.TRUE.equals(isGameOver.getValue())) return;
        shoots.add(new CricketShoot(multiplier, value, player == 1 ? player1 : player2));
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
                (player2ThrowAll && player2.getPoints() > player1.getPoints()) )
            isGameOver.setValue(true);
    }

    public void shootRemove(CricketShoot cricketShoot) {
        shoots.remove(cricketShoot);
    }

    private void reCalculatePoints() {
        for (CricketShoot shoot : shoots) {
            CricketPlayer opponent = shoot.getPlayer() == player1 ? player2 : player1;
            shoot.getPlayer().addShoot(shoot, opponent.getScores().get(shoot.getValue()));
        }
    }

    public List<CricketShoot> getShoots() {
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