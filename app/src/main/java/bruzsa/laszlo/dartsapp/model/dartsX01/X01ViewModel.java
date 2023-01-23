package bruzsa.laszlo.dartsapp.model.dartsX01;

import static bruzsa.laszlo.dartsapp.model.Team.TEAM1;
import static bruzsa.laszlo.dartsapp.model.Team.TEAM2;
import static bruzsa.laszlo.dartsapp.model.dartsX01.X01ChangeType.ADD_SHOOT;
import static bruzsa.laszlo.dartsapp.model.dartsX01.X01ChangeType.ADD_SHOOTS;
import static bruzsa.laszlo.dartsapp.model.dartsX01.X01ChangeType.CHANGE_ACTIVE_PLAYER;
import static bruzsa.laszlo.dartsapp.model.dartsX01.X01ChangeType.GAME_OVER;
import static bruzsa.laszlo.dartsapp.model.dartsX01.X01ChangeType.NEW_GAME;
import static bruzsa.laszlo.dartsapp.model.dartsX01.X01ChangeType.NO_GAME;
import static bruzsa.laszlo.dartsapp.model.dartsX01.X01ChangeType.REMOVE_SHOOT;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.Map;

import bruzsa.laszlo.dartsapp.dao.Player;
import bruzsa.laszlo.dartsapp.model.Team;
import bruzsa.laszlo.dartsapp.model.TeamPlayer;

public class X01ViewModel extends ViewModel {

    private TeamPlayer active = TEAM1.player1();
    private TeamPlayer startLeg = TEAM1.player1();
    private TeamPlayer startSet = TEAM1.player1();
    private X01GameSettings settings = X01GameSettings.getDefault();
    private boolean teamPlay;
    private Map<Team, X01TeamScores> teamScores;
    private int lastRemovedIndex = -1;
    private int leg;

    private final MutableLiveData<X01ChangeType> state = new MutableLiveData<>(NO_GAME);


    public void newThrow(int dartsThrow) {
        if (state.getValue() == GAME_OVER) return;
        if (dartsThrow > settings.getHandicap()) {
            newThrow(TEAM1, dartsThrow);
            newThrow(TEAM2, dartsThrow);
            state.setValue(ADD_SHOOTS);
        } else {
            if (newThrow(active.team, dartsThrow)) {
                state.setValue(GAME_OVER);
            } else {
                active = active.nextPlayer(teamPlay);
                state.setValue(ADD_SHOOT);
            }
        }
    }

    private boolean newThrow(Team team, int throwValue) {
        X01TeamScores aPlayer = teamScores.get(team);
        X01TeamScores opponent = teamScores.get(team.opponent());
        int maxLegSet = settings.getLegSetOf() / 2 + 1;
        int newScore = getScore(team) - throwValue;

        boolean checkedOut = newScore == 0;
        X01Throw newThrow = new X01Throw(
                throwValue, newScore > 1 || checkedOut, leg, 3, checkedOut);
        aPlayer.addThrow(newThrow);
        if (!checkedOut) return false;
        return isGameOver(aPlayer, opponent, maxLegSet);
    }

    private boolean isGameOver(X01TeamScores aPlayer, X01TeamScores opponent, int maxLegSet) {
        switch (settings.getDartsX01MatchType()) {
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
        leg++;
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

    public void removeThrow(X01Throw x01Throw, Team team) {
        lastRemovedIndex = teamScores.get(team).removeThrow(x01Throw);
        state.setValue(REMOVE_SHOOT);
    }

    public int getLastRemovedIndex() {
        return lastRemovedIndex;
    }

    public int getScore(Team team) {
        return settings.getStartScore() - teamScores.get(team).getSum(leg);
    }

    public boolean isOut(Team team) {
        int score = getScore(team);
        return (score > 1 && score < 159) || List.of(160, 161, 164, 167, 170).contains(score);
    }

    public String getStat(Team team) {
        return teamScores.get(team).getStat();
    }

    public List<X01Throw> getThrows(Team team) {
        return teamScores.get(team).getThrowsList();
    }

    public void setSettings(@NonNull X01GameSettings settings) {
        this.settings = settings;
    }

    public void newGame(Map<TeamPlayer, Player> activePlayersMap) {
        X01TeamScores team1 = new X01TeamScores(activePlayersMap.get(TEAM1.player1()), activePlayersMap.get(TEAM1.player2()));
        X01TeamScores team2 = new X01TeamScores(activePlayersMap.get(TEAM2.player1()), activePlayersMap.get(TEAM2.player2()));
        teamScores = Map.of(TEAM1, team1, TEAM2, team2);
        active = TEAM1.player1();
        teamPlay = activePlayersMap.size() == TeamPlayer.values().length;
        state.setValue(NEW_GAME);
    }
}