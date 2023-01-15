package bruzsa.laszlo.dartsapp.model.darts501;

import static bruzsa.laszlo.dartsapp.model.darts501.ChangeType.ADD_SHOOT;
import static bruzsa.laszlo.dartsapp.model.darts501.ChangeType.ADD_SHOOTS;
import static bruzsa.laszlo.dartsapp.model.darts501.ChangeType.CHANGE_ACTIVE_PLAYER;
import static bruzsa.laszlo.dartsapp.model.darts501.ChangeType.GAME_OVER;
import static bruzsa.laszlo.dartsapp.model.darts501.ChangeType.NEW_GAME;
import static bruzsa.laszlo.dartsapp.model.darts501.ChangeType.NO_GAME;
import static bruzsa.laszlo.dartsapp.model.darts501.ChangeType.REMOVE_SHOOT;
import static bruzsa.laszlo.dartsapp.model.darts501.Darts501MatchType.SETS;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import bruzsa.laszlo.dartsapp.dao.Player;

public class Darts501ViewModel extends ViewModel {

    public static final int PLAYER_1 = 0;
    public static final int PLAYER_2 = 1;
    private int active = PLAYER_1;
    private int startLeg = PLAYER_1;
    private int startSet = PLAYER_1;
    private Darts501GameSettings settings = Darts501GameSettings.getDefault();

    private List<Darts501Player> players = new ArrayList<>(2);
    private int lastRemovedIndex = -1;

    private final MutableLiveData<ChangeType> state = new MutableLiveData<>(NO_GAME);


    public void newThrow(int dartsThrow) {
        if (state.getValue() == GAME_OVER) return;
        Log.e("TAG", "newThrowException: " + state.getValue());
        if (dartsThrow > settings.getHandicap()) {
            newThrow(PLAYER_1, dartsThrow);
            newThrow(PLAYER_2, dartsThrow);
            state.setValue(ADD_SHOOTS);
        } else {
            if (newThrow(active, dartsThrow)) {
                state.setValue(GAME_OVER);
            } else {
                active = getOpponent(active);
                state.setValue(ADD_SHOOT);
            }
        }
    }

    private boolean newThrow(int player, int shootValue) {
        Darts501Player aPlayer = players.get(player);
        Darts501Player opponent = players.get(getOpponent(player));
        int maxLegSet = settings.getLegSetOf() / 2 + 1;
        int newScore = getScore(player) - shootValue;
        Darts501Throw newThrow = new Darts501Throw(shootValue, newScore > 1 || newScore == 0);
        aPlayer.addThrow(newThrow);
        if (newScore == 0) {
            if (settings.getDarts501MatchType() == SETS) {
                if (aPlayer.getSets() == maxLegSet - 1 && opponent.getSets() == maxLegSet - 1) {
                    aPlayer.wonLeg();
                    return aPlayer.getLegs() - 2 >= opponent.wonLeg() || aPlayer.getLegs() == 6;
                } else if (aPlayer.getLegs() == 2) {
                    opponent.loseSet();
                    return maxLegSet == aPlayer.wonSet();
                } else {
                    aPlayer.wonLeg();
                    return false;
                }
            } else {
                return maxLegSet == aPlayer.wonLeg();
            }
        }
        return false;
    }

    public void newGame(Player player1, Player player2) {
        players.clear();
        players.add(PLAYER_1, new Darts501Player(player1));
        players.add(PLAYER_2, new Darts501Player(player2));
        active = PLAYER_1;
        state.setValue(NEW_GAME);
    }

    public void restartGame() {
        newGame(Objects.requireNonNull(players.get(PLAYER_1)).getPlayer(),
                Objects.requireNonNull(players.get(PLAYER_2)).getPlayer());
    }

    public String getPlayer(int player) {
        return players.get(player).getPlayerName();
    }

    public int getActivePlayer() {
        return active;
    }

    public void setActivePlayer(int player) {
        active = player;
        state.setValue(CHANGE_ACTIVE_PLAYER);
    }

    private int getOpponent(int player) {
        return player == PLAYER_1 ? PLAYER_2 : PLAYER_1;
    }


    public LiveData<ChangeType> onPlayerChange() {
        return state;
    }

    public void removeThrow(Darts501Throw darts501Throw, int player) {
        lastRemovedIndex = players.get(player).removeThrow(darts501Throw);
        state.setValue(REMOVE_SHOOT);
    }

    public int getLastRemovedIndex() {
        return lastRemovedIndex;
    }

    public int getScore(int player) {
        return settings.getStartScore() - players.get(player).getSum();
    }

    public boolean isOut(int player) {
        int score = getScore(player);
        return (score > 1 && score < 159) || List.of(160, 161, 164, 167, 170).contains(score);
    }

    public String getStat(int player) {
        return players.get(player).getStat();
    }

    public List<Darts501Throw> getThrows(int player) {
        return players.get(player).getThrowsList();
    }

    public void setSettings(@NonNull Darts501GameSettings settings) {
        this.settings = settings;
    }
}