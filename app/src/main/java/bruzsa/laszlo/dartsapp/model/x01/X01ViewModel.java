package bruzsa.laszlo.dartsapp.model.x01;

import static bruzsa.laszlo.dartsapp.model.Team.TEAM1;
import static bruzsa.laszlo.dartsapp.model.Team.TEAM2;
import static bruzsa.laszlo.dartsapp.model.x01.X01ChangeType.CHANGE_ACTIVE_PLAYER;
import static bruzsa.laszlo.dartsapp.model.x01.X01ChangeType.GAME_OVER;
import static bruzsa.laszlo.dartsapp.model.x01.X01ChangeType.NEW_GAME;
import static bruzsa.laszlo.dartsapp.model.x01.X01ChangeType.NO_GAME;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import bruzsa.laszlo.dartsapp.dao.Player;
import bruzsa.laszlo.dartsapp.model.Team;
import bruzsa.laszlo.dartsapp.model.TeamPlayer;
import bruzsa.laszlo.dartsapp.ui.x01.X01ThrowAdapter;

public class X01ViewModel extends ViewModel {

    private Map<TeamPlayer, Player> teamPlayerMap;
    private TeamPlayer active = TEAM1.player1();
    private TeamPlayer startLeg = TEAM1.player1();
    private TeamPlayer startSet = TEAM1.player1();
    private X01GameSettings settings = X01GameSettings.getDefault();
    private boolean teamPlay;
    private Map<Team, X01TeamScores> teamScores;
    private int currentLeg;

    private final MutableLiveData<X01ChangeType> state = new MutableLiveData<>(NO_GAME);

    private Map<Team, X01ThrowAdapter> throwAdapterMap;
    private final Map<Team, MutableLiveData<Integer>> scores = new EnumMap<>(Map.of(
            TEAM1, new MutableLiveData<>(10),
            TEAM2, new MutableLiveData<>(10)
    ));


    public void newThrow(int throwValue) {
        if (state.getValue() == GAME_OVER) return;
        X01TeamScores aPlayer = teamScores.get(active.team);
        X01TeamScores opponent = teamScores.get(active.team.opponent());
        int maxLegSet = settings.getLegSetOf() / 2 + 1;
        int newScore = getScore(active.team) - throwValue;
        Log.d("X01ViewModel", "newThrow: " + getScore(active.team));

        boolean checkedOut = newScore == 0;
        X01Throw newThrow = new X01Throw(
                throwValue, newScore > 1 || checkedOut, currentLeg, 3, checkedOut, teamPlayerMap.get(active));
        aPlayer.addThrow(newThrow);
        throwAdapterMap.get(active.team).inserted();
        scores.get(active.team).setValue(getScore(active.team));
        Log.d("X01ViewModel", "newThrow: " + scores.get(TEAM1).getValue() + " " + scores.get(TEAM2).getValue());

        if (checkedOut && isGameOver(aPlayer, opponent, maxLegSet)) {
            state.setValue(GAME_OVER);
        } else {
            active = active.nextPlayer(teamPlay);
            state.setValue(CHANGE_ACTIVE_PLAYER);
        }
    }

    private boolean isGameOver(X01TeamScores aPlayer, X01TeamScores opponent, int maxLegSet) {
        switch (settings.getX01MatchType()) {
            case SINGLE_LEG:
                return true;
            case LEGS:
                return maxLegSet == aPlayer.wonLeg();
            case SETS:
                if (aPlayer.getSets() == maxLegSet - 1 && opponent.getSets() == maxLegSet - 1) {
                    aPlayer.wonLeg();
                    startLeg = startLeg.nextPlayer(teamPlay);
                    return aPlayer.getLegs() - 2 >= opponent.getLegs() || aPlayer.getLegs() == 6;
                } else if (aPlayer.wonLeg() == 3) {
                    opponent.loseSet();
                    startSet = startSet.nextPlayer(teamPlay);
                    startLeg = startSet;
                    return maxLegSet == aPlayer.wonSet();
                } else {
                    aPlayer.wonLeg();
                    startLeg = startLeg.nextPlayer(teamPlay);
                    return false;
                }
        }
        active = startLeg;
        currentLeg++;
        return false;
    }

    public TeamPlayer getActivePlayer() {
        return active;
    }

    public void setActivePlayer(TeamPlayer team) {
        active = team;
        state.setValue(CHANGE_ACTIVE_PLAYER);
    }


    public LiveData<X01ChangeType> onPlayerChange() {
        return state;
    }

    public boolean removeThrow(X01Throw x01Throw) {
        if (x01Throw.getLeg() == currentLeg && !x01Throw.isRemoved()) {
            x01Throw.setRemoved();
            scores.get(TEAM1).setValue(getScore(TEAM1));
            scores.get(TEAM2).setValue(getScore(TEAM2));
            return true;
        }
        return false;
    }

    public int getScore(Team team) {
        return settings.getStartScore() - teamScores.get(team).getSum(currentLeg);
    }

    public boolean isOut(Team team) {
        int score = getScore(team);
        return (score > 1 && score < 159) || List.of(160, 161, 164, 167, 170).contains(score);
    }

    public X01SummaryStatistics getStat(Team team) {
        return teamScores.get(team).getStat();
    }

    public int getLeg(Team team) {
        return teamScores.get(team).getLegs();
    }

    public int getSet(Team team) {
        return teamScores.get(team).getSets();
    }

    public void setSettings(@NonNull X01GameSettings settings) {
        this.settings = settings;
    }

    public void newGame(Map<TeamPlayer, Player> activePlayersMap) {
        teamPlayerMap = activePlayersMap;
        X01TeamScores team1 = new X01TeamScores(activePlayersMap.get(TEAM1.player1()), activePlayersMap.get(TEAM1.player2()));
        X01TeamScores team2 = new X01TeamScores(activePlayersMap.get(TEAM2.player1()), activePlayersMap.get(TEAM2.player2()));
        teamScores = Map.of(TEAM1, team1, TEAM2, team2);
        throwAdapterMap = new EnumMap<>(Map.of(
                TEAM1, new X01ThrowAdapter(teamScores.get(TEAM1).getThrowsList(), this::removeThrow),
                TEAM2, new X01ThrowAdapter(teamScores.get(TEAM2).getThrowsList(), this::removeThrow)
        ));
        active = TEAM1.player1();
        teamPlay = activePlayersMap.size() == TeamPlayer.values().length;
        state.setValue(NEW_GAME);
    }

    public X01ThrowAdapter getThrowsAdapterTeam(Team team) {
        return throwAdapterMap.get(team);
    }

    public LiveData<Integer> getLiveScore(Team team) {
        return scores.get(team);
    }
}