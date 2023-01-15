package bruzsa.laszlo.dartsapp.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import bruzsa.laszlo.dartsapp.dao.Player;
import bruzsa.laszlo.dartsapp.model.darts501.Darts501GameSettings;

public class SharedViewModel extends ViewModel {

    private Player player1 = new Player("Default 1\nDefault 3");
    private Player player2 = new Player("Default 2\nDefault 4");

    private Darts501GameSettings settings = Darts501GameSettings.getDefault();

    private MutableLiveData<Boolean> newGameCricket = new MutableLiveData<>();
    private MutableLiveData<Boolean> newGame501 = new MutableLiveData<>();


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

    public void newGameCricket() {
        newGameCricket.setValue(true);
    }

    public LiveData<Boolean> startNewGameCricket() {
        return newGameCricket;
    }

    public void setNewGameCricket() {
        newGameCricket.setValue(true);
    }

    public LiveData<Boolean> getNewGame501() {
        return newGame501;
    }

    public void startNewGame501() {
        newGame501.setValue(true);
    }

    public Darts501GameSettings getSettings() {
        return settings;
    }

    public void setSettings(Darts501GameSettings settings) {
        this.settings = settings;
    }
}
