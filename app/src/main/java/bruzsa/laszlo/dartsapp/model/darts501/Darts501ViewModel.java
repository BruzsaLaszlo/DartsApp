package bruzsa.laszlo.dartsapp.model.darts501;

import static bruzsa.laszlo.dartsapp.model.Team.TEAM1;
import static bruzsa.laszlo.dartsapp.model.Team.TEAM2;
import static bruzsa.laszlo.dartsapp.model.darts501.ChangeType.ADD_SHOOT;
import static bruzsa.laszlo.dartsapp.model.darts501.ChangeType.ADD_SHOOTS;
import static bruzsa.laszlo.dartsapp.model.darts501.ChangeType.CHANGE_ACTIVE_PLAYER;
import static bruzsa.laszlo.dartsapp.model.darts501.ChangeType.GAME_OVER;
import static bruzsa.laszlo.dartsapp.model.darts501.ChangeType.NEW_GAME;
import static bruzsa.laszlo.dartsapp.model.darts501.ChangeType.NO_GAME;
import static bruzsa.laszlo.dartsapp.model.darts501.ChangeType.REMOVE_SHOOT;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.Map;

import bruzsa.laszlo.dartsapp.dao.Player;
import bruzsa.laszlo.dartsapp.model.Team;
import bruzsa.laszlo.dartsapp.model.TeamPlayer;

public class Darts501ViewModel extends ViewModel {

    private TeamPlayer active = TEAM1.player1();
    private TeamPlayer startLeg = TEAM1.player1();
    private TeamPlayer startSet = TEAM1.player1();
    private Darts501GameSettings settings = Darts501GameSettings.getDefault();
    private boolean teamPlay;
    private Map<Team, Darts501TeamScores> teamScores;
    private int lastRemovedIndex = -1;

    private final MutableLiveData<ChangeType> state = new MutableLiveData<>(NO_GAME);


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

    private boolean newThrow(Team team, int shootValue) {
        Darts501TeamScores aPlayer = teamScores.get(team);
        Darts501TeamScores opponent = teamScores.get(team.opponent());
        int maxLegSet = settings.getLegSetOf() / 2 + 1;
        int newScore = getScore(team) - shootValue;

        Darts501Throw newThrow = new Darts501Throw(shootValue, newScore > 1 || newScore == 0);
        aPlayer.addThrow(newThrow);
        if (newScore != 0) return false;
        return isGameOver(aPlayer, opponent, maxLegSet);
    }

    private boolean isGameOver(Darts501TeamScores aPlayer, Darts501TeamScores opponent, int maxLegSet) {
        switch (settings.getDarts501MatchType()) {
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
        return false;
    }

    public TeamPlayer getActivePlayer() {
        return active;
    }

    public void setActivePlayer(TeamPlayer team) {
        active = team;
        state.setValue(CHANGE_ACTIVE_PLAYER);
    }


    public LiveData<ChangeType> onPlayerChange() {
        return state;
    }

    public void removeThrow(Darts501Throw darts501Throw, Team team) {
        lastRemovedIndex = teamScores.get(team).removeThrow(darts501Throw);
        state.setValue(REMOVE_SHOOT);
    }

    public int getLastRemovedIndex() {
        return lastRemovedIndex;
    }

    public int getScore(Team team) {
        return settings.getStartScore() - teamScores.get(team).getSum();
    }

    public boolean isOut(Team team) {
        int score = getScore(team);
        return (score > 1 && score < 159) || List.of(160, 161, 164, 167, 170).contains(score);
    }

    public String getStat(Team team) {
        return teamScores.get(team).getStat();
    }

    public List<Darts501Throw> getThrows(Team team) {
        return teamScores.get(team).getThrowsList();
    }

    public void setSettings(@NonNull Darts501GameSettings settings) {
        this.settings = settings;
    }

    public void newGame(Map<TeamPlayer, Player> activePlayersMap) {
        Darts501TeamScores team1 = new Darts501TeamScores(activePlayersMap.get(TEAM1.player1()), activePlayersMap.get(TEAM1.player2()));
        Darts501TeamScores team2 = new Darts501TeamScores(activePlayersMap.get(TEAM2.player1()), activePlayersMap.get(TEAM2.player2()));
        teamScores = Map.of(TEAM1,team1, TEAM2,team2);
        active = TEAM1.player1();
        teamPlay = activePlayersMap.size() == TeamPlayer.values().length;
        state.setValue(NEW_GAME);
    }
}