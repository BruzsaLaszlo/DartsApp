package bruzsa.laszlo.dartsapp.model.x01;

import static java.lang.String.format;
import static bruzsa.laszlo.dartsapp.model.Team.TEAM1;
import static bruzsa.laszlo.dartsapp.model.Team.TEAM2;
import static bruzsa.laszlo.dartsapp.model.x01.Status.PARTIAL;
import static bruzsa.laszlo.dartsapp.model.x01.Status.getStatus;
import static bruzsa.laszlo.dartsapp.model.x01.ThrowValidator.isValidCheckout;
import static bruzsa.laszlo.dartsapp.model.x01.ThrowValidator.isValidThrow;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.inject.Inject;

import bruzsa.laszlo.dartsapp.dabatase.AppDatabase;
import bruzsa.laszlo.dartsapp.dabatase.dao.X01ThrowDao;
import bruzsa.laszlo.dartsapp.enties.Player;
import bruzsa.laszlo.dartsapp.enties.x01.X01TeamScores;
import bruzsa.laszlo.dartsapp.model.PlayersEnum;
import bruzsa.laszlo.dartsapp.model.Team;
import bruzsa.laszlo.dartsapp.model.home.AppSettings;
import dagger.hilt.android.scopes.ViewModelScoped;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("ConstantConditions")
@ViewModelScoped
public class X01Service {


    private static final String TAG = X01Service.class.getSimpleName();
    @Getter
    private PlayersEnum active = TEAM1.player1();
    private PlayersEnum startLeg = TEAM1.player1();
    private PlayersEnum startSet = TEAM1.player1();
    private int legCount;
    @Getter
    private final X01Settings settings;
    private final boolean teamPlay;
    @Getter
    private final Map<Team, X01TeamScores> teamScores;
    private X01Throw partialThrow;
    private final Map<PlayersEnum, Player> PlayersEnumMap;
    @Getter
    boolean gameOver;
    private final X01ThrowDao x01ThrowDao;

    @Inject
    public X01Service(AppSettings appSettings, AppDatabase appDatabase) {
        this.PlayersEnumMap = appSettings.getSelectedPlayers();
        this.x01ThrowDao = appDatabase.x01ThrowDao();
        teamScores = Map.of(
                TEAM1, new X01TeamScores(PlayersEnumMap.get(TEAM1.player1()), PlayersEnumMap.get(TEAM1.player2())),
                TEAM2, new X01TeamScores(PlayersEnumMap.get(TEAM2.player1()), PlayersEnumMap.get(TEAM2.player2())));
        teamPlay = PlayersEnumMap.size() == PlayersEnum.values().length;
        this.settings = appSettings.getX01Settings();
    }

    public PlayersEnum newThrow(int throwValue) {
        return newThrow(throwValue, 3, null);
    }

    public PlayersEnum newThrow(int throwValue, int dartCount, Consumer<Team> onGameOverEvent) {
        partialThrow = null;

        var currentPlayer = active;
        Team team = currentPlayer.team;
        X01TeamScores aPlayer = teamScores.get(team);
        X01TeamScores opponent = teamScores.get(team.opponent());

        int newScore = getStat(team).getScore() - throwValue;

        boolean checkedOut = newScore == 0;
        X01Throw newThrow = new X01Throw(
                throwValue,
                getStatus(checkedOut ? isValidCheckout(throwValue) : newScore > 1 && isValidThrow(throwValue)),
                legCount,
                dartCount,
                checkedOut,
                PlayersEnumMap.get(currentPlayer).getId());
        aPlayer.addThrow(newThrow);
        x01ThrowDao.insert(newThrow);
        Log.d(TAG, "newThrow: " + newThrow);

        if (checkedOut) {
            checkout(aPlayer, opponent, settings.getCount());
            if (gameOver) onGameOverEvent.accept(currentPlayer.team);
        } else {
            active = active.nextPlayer(teamPlay);
        }
        return currentPlayer;
    }

    private void checkout(X01TeamScores aPlayer, X01TeamScores opponent, int maxLegSet) {
        gameOver = switch (settings.getMatchType()) {
            case LEG -> maxLegSet == aPlayer.wonLeg();
            case SET -> {
                if (settings.isWinByTwoClearLeg() &&
                        aPlayer.getSets() == maxLegSet - 1 && opponent.getSets() == maxLegSet - 1) {
                    startLeg = startLeg.nextPlayer(teamPlay);
                    yield aPlayer.wonLeg() - 2 >= opponent.getLegs() || aPlayer.getLegs() == 6;
                } else if (aPlayer.getLegs() == 2) {
                    aPlayer.wonSet();
                    opponent.loseSet();
                    startSet = startSet.nextPlayer(teamPlay);
                    startLeg = startSet;
                    yield maxLegSet == aPlayer.getSets();
                } else {
                    aPlayer.wonLeg();
                    startLeg = startLeg.nextPlayer(teamPlay);
                    yield false;
                }
            }
        };
        if (!gameOver) {
            legCount++;
            active = startLeg;
        }
    }

    public boolean removeThrow(X01Throw x01Throw) {
        if (x01Throw.getLeg() == legCount && x01Throw.isNotRemoved()) {
            x01Throw.setRemoved();
            x01ThrowDao.update(x01Throw);
            Log.d(TAG, format("changeThrow: %d was deleted from playerId %s", x01Throw.getValue(), x01Throw.getPlayerId()));
            return true;
        }
        return false;
    }


    public Stat getStat(Team team) {
        List<X01Throw> throwList = teamScores.get(team).getThrowsList();
        if (partialThrow != null && team == active.team) {
            throwList = new ArrayList<>(throwList);
            throwList.add(partialThrow);
            partialThrow = null;
        }
        return Stat.calculate(settings.getStartScore(), legCount, throwList);
    }

    public List<X01Throw> getThrowList(Team team) {
        return teamScores.get(team).getThrowsList();
    }

    public String getSet(Team team) {
        return Integer.toString(teamScores.get(team).getSets());
    }

    public String getLeg(Team team) {
        return Integer.toString(teamScores.get(team).getLegs());
    }

    public boolean isCheckout(int throwValue) {
        return getStat(active.team).getScore() == throwValue;
    }

    public void setActive(PlayersEnum active) {
        partialThrow = null;
        this.active = active;
        if (!isGameStarted()) {
            startLeg = active;
            startSet = active;
        }
    }

    public void newPartialValue(int value) {
        partialThrow = partialThrow == null
                ? new X01Throw(value, PARTIAL, legCount, 0, false, PlayersEnumMap.get(active).getId())
                : null;
    }

    private boolean isGameStarted() {
        return teamScores.get(TEAM1)
                .getThrowsList()
                .stream()
                .anyMatch(X01Throw::isValid)
                || teamScores.get(TEAM2)
                .getThrowsList()
                .stream()
                .anyMatch(X01Throw::isValid);
    }
}
