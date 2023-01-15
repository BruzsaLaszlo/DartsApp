package bruzsa.laszlo.dartsapp.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import bruzsa.laszlo.dartsapp.dao.Player;

public class SharedViewModel extends ViewModel {

    private Player player1 = new Player("Default 1\nDefault 3");
    private Player player2 = new Player("Default 2\nDefault 4");

    private int startScore = 501;
    private Darts501MatchType darts501MatchType;

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public int getStartScore() {
        return startScore;
    }

    public void setStartScore(int startScore) {
        this.startScore = startScore;
    }

    public Darts501MatchType getDarts501MatchType() {
        return darts501MatchType;
    }

    public void setDarts501MatchType(Darts501MatchType darts501MatchType) {
        this.darts501MatchType = darts501MatchType;
    }
}
