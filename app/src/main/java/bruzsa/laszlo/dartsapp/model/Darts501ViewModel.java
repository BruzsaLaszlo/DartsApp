package bruzsa.laszlo.dartsapp.model;

import static bruzsa.laszlo.dartsapp.model.ChangeType.ADD_SHOOT;
import static bruzsa.laszlo.dartsapp.model.ChangeType.ADD_SHOOTS;
import static bruzsa.laszlo.dartsapp.model.ChangeType.CHANGE_ACTIVE_PLAYER;
import static bruzsa.laszlo.dartsapp.model.ChangeType.GAME_OVER;
import static bruzsa.laszlo.dartsapp.model.ChangeType.NO_GAME;
import static bruzsa.laszlo.dartsapp.model.ChangeType.REMOVE_SHOOT;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bruzsa.laszlo.dartsapp.dao.Player;
import bruzsa.laszlo.dartsapp.ui.darts501.Darts501Throw;
import bruzsa.laszlo.dartsapp.ui.darts501.Darts501SummaryStatistics;

public class Darts501ViewModel extends ViewModel {

    public static final int PLAYER_1 = 0;
    public static final int PLAYER_2 = 1;
    private int active = PLAYER_1;
    private final List<Player> players = new ArrayList<>();
    private final List<List<Darts501Throw>> shoots = new ArrayList<>();
    public static final int HANDICAP = 181;
    private int startScore;
    private Darts501Throw lastRemoved;

    private final MutableLiveData<ChangeType> onPlayerChange = new MutableLiveData<>(NO_GAME);

    public List<Darts501Throw> getThrows(int player) {
        return Collections.unmodifiableList(shoots.get(player));
    }

    public void newThrow(int shoot) {
        if (shoot > HANDICAP) {
            this.newThrow(PLAYER_1, shoot);
            this.newThrow(PLAYER_2, shoot);
            onPlayerChange.setValue(ADD_SHOOTS);
        } else {
            if (newThrow(active, shoot)) {
                onPlayerChange.setValue(GAME_OVER);
            } else {
                nextPlayer();
                onPlayerChange.setValue(ADD_SHOOT);
            }
        }
    }

    private boolean newThrow(int player, int shootValue) {
        int newScore = getStat(player).getScore() - shootValue;
        Darts501Throw shoot = new Darts501Throw(shootValue, newScore > 1 || newScore == 0);
        shoots.get(player).add(shoot);
        return newScore == 0;
    }

    public void newGame(Player player1, Player player2) {
        players.clear();
        players.add(PLAYER_1, player1);
        players.add(PLAYER_2, player2);
        restartGame();
    }

    public void restartGame() {
        shoots.clear();
        shoots.add(new ArrayList<>());
        shoots.add(new ArrayList<>());
        active = PLAYER_1;
        onPlayerChange.setValue(CHANGE_ACTIVE_PLAYER);
    }

    public Player getPlayer(int player) {
        return players.get(player);
    }

    public int getActivePlayer() {
        return active;
    }

    public void setActivePlayer(int player) {
        active = player;
    }

    private void nextPlayer() {
        active = active == PLAYER_1 ? PLAYER_2 : PLAYER_1;
    }

    public Darts501SummaryStatistics getStat(int player) {
        return new Darts501SummaryStatistics(getThrows(player), startScore);
    }

    public LiveData<ChangeType> onPlayerChange() {
        return onPlayerChange;
    }

    public void removeThrow(Darts501Throw shoot) {
        shoots.get(PLAYER_1).remove(shoot);
        shoots.get(PLAYER_2).remove(shoot);
        lastRemoved = shoot;
        onPlayerChange.setValue(REMOVE_SHOOT);
    }

    public Darts501Throw getLastRemoved() {
        return lastRemoved;
    }

    public void setStartScore(int startScore) {
        this.startScore = startScore;
    }
}