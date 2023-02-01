package bruzsa.laszlo.dartsapp.model.cricket;

import static bruzsa.laszlo.dartsapp.model.Team.TEAM1;
import static bruzsa.laszlo.dartsapp.model.Team.TEAM2;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import bruzsa.laszlo.dartsapp.dao.Player;
import bruzsa.laszlo.dartsapp.model.TeamPlayer;

public class CricketViewModel extends ViewModel {

    private CricketTeam team1;
    private CricketTeam team2;
    private final List<CricketThrow> shoots = new ArrayList<>();
    private final MutableLiveData<Boolean> isGameOver = new MutableLiveData<>(false);

    public void newGame(Map<TeamPlayer, Player> players) {
        this.team1 = new CricketTeam(players.get(TEAM1.player1()), players.get(TEAM1.player2()));
        this.team2 = new CricketTeam(players.get(TEAM2.player1()), players.get(TEAM2.player2()));
    }

    public void newThrow(int multiplier, int value, int player) {
        if (Boolean.TRUE.equals(isGameOver.getValue())) return;
        shoots.add(new CricketThrow(multiplier, value, player == 1 ? team1 : team2));
        updatePoints();
    }

    public String updatePoints() {
        team1.clearPoints();
        team2.clearPoints();
        reCalculatePoints();
        isGameEnd();
        return team1.getPoints() + " : " + team2.getPoints();
    }

    private void isGameEnd() {
        boolean player1ThrowAll = team1.getScores().size() == 7 &&
                team1.getScores().values().stream().allMatch(m -> m >= 3);
        boolean player2ThrowAll = team2.getScores().size() == 7 &&
                team2.getScores().values().stream().allMatch(m -> m >= 3);
        if ((player1ThrowAll && team1.getPoints() > team2.getPoints()) ||
                (player2ThrowAll && team2.getPoints() > team1.getPoints()))
            isGameOver.setValue(true);
    }

    public boolean shootRemove(CricketThrow cricketThrow) {
        return cricketThrow.setRemoved() && !isGameOver.getValue();
    }

    private void reCalculatePoints() {
        for (CricketThrow shoot : shoots) {
            CricketTeam opponent = shoot.getPlayer() == team1 ? team2 : team1;
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
        return player == 1 ? team1.getScores() : team2.getScores();
    }

    public LiveData<Boolean> isGameOver() {
        return isGameOver;
    }
}