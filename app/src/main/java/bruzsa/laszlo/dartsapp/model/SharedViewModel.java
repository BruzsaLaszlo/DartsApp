package bruzsa.laszlo.dartsapp.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import bruzsa.laszlo.dartsapp.dao.Player;

public class SharedViewModel extends ViewModel {

    private Player player1 = new Player("Default 1");
    private Player player2 = new Player("Default 2");

    private int startScore = 501;


    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    private MutableLiveData<Boolean> newGame = new MutableLiveData<>();

    public void newGameCricket() {
        newGame.setValue(true);
    }

    public LiveData<Boolean> getNewGame() {
        return newGame;
    }

    public int getStartScore() {
        return startScore;
    }
}
