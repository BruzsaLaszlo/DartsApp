package bruzsa.laszlo.dartsapp.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import bruzsa.laszlo.dartsapp.ui.darts501.Darts501Player;
import bruzsa.laszlo.dartsapp.ui.darts501.Darts501Shoot;

public class Darts501ViewModel extends ViewModel {

    private Darts501Player player1;
    private Darts501Player player2;
    private final MutableLiveData<Darts501Player> active = new MutableLiveData<>();
    private final MutableLiveData<Boolean> gameOver = new MutableLiveData<>();
    public static final int HANDICAP = 181;
    private OnChangeListener listener;

    public List<Darts501Shoot> getShoots(int player) {
        return player == 1 ? player1.getShoots() : player2.getShoots();
    }

    public void newShoot(int shoot) {
        if (shoot > HANDICAP) {
            player1.newShoot(shoot);
            player2.newShoot(shoot);
            listener.changeOnPlayer(1);
            listener.changeOnPlayer(2);
        } else {
            boolean winner = getActivePlayer().newShoot(shoot);
            listener.changeOnPlayer(whichActive());
            if (winner) gameOver.setValue(true);
            else nextPlayer();
        }
    }

    public void newGame(Darts501Player player1, Darts501Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public void startGame() {
        active.setValue(player1);
        player1.resetShoots();
        player2.resetShoots();
        gameOver.setValue(false);
    }

    public Darts501Player getPlayer1() {
        return player1;
    }

    public Darts501Player getPlayer2() {
        return player2;
    }

    public int whichActive() {
        return player1 == active.getValue() ? 1 : 2;
    }

    public void setActivePlayer(int i) {
        active.setValue(i == 1 ? player1 : player2);
    }

    public Darts501Player getActivePlayer() {
        return active.getValue();
    }

    private void nextPlayer() {
        active.setValue(active.getValue() == player1 ? player2 : player1);
    }

    public void setPlayerShootListener(OnChangeListener listener) {
        this.listener = listener;
    }

    public LiveData<Darts501Player> getActive() {
        return active;
    }

    public interface OnChangeListener {
        void changeOnPlayer(int player);
    }

    public MutableLiveData<Boolean> isGameOver() {
        return gameOver;
    }
}