package bruzsa.laszlo.dartsapp.model.x01;

import static bruzsa.laszlo.dartsapp.model.Team.TEAM1;
import static bruzsa.laszlo.dartsapp.model.Team.TEAM2;
import static bruzsa.laszlo.dartsapp.model.x01.Status.PARTIAL;
import static bruzsa.laszlo.dartsapp.model.x01.Status.getStatus;
import static bruzsa.laszlo.dartsapp.model.x01.ThrowValidator.isValidCheckout;
import static bruzsa.laszlo.dartsapp.model.x01.ThrowValidator.isValidThrow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.inject.Inject;

import bruzsa.laszlo.dartsapp.enties.Player;
import bruzsa.laszlo.dartsapp.enties.x01.X01TeamScores;
import bruzsa.laszlo.dartsapp.model.PlayersEnum;
import bruzsa.laszlo.dartsapp.model.Team;
import bruzsa.laszlo.dartsapp.model.home.AppSettings;
import dagger.hilt.android.scopes.ViewModelScoped;
import lombok.Getter;

@SuppressWarnings("ConstantConditions")
@ViewModelScoped
public class X01Service {

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

    @Inject
    public X01Service(AppSettings appSettings) {
        this.PlayersEnumMap = appSettings.getSelectedPlayers();
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

        var actual = active;
        Team team = actual.team;
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
                PlayersEnumMap.get(active));
        aPlayer.addThrow(newThrow);

        if (checkedOut) {
            checkout(aPlayer, opponent, settings.getCount());
            if (gameOver) onGameOverEvent.accept(active.team);
        } else {
            active = active.nextPlayer(teamPlay);
        }
        return actual;
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

    public boolean removeThrow(X01Throw x01Throw, Runnable onThrowRemovedEvent) {
        if (x01Throw.getLeg() == legCount && x01Throw.isNotRemoved()) {
            x01Throw.setRemoved();
            onThrowRemovedEvent.run();
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
        if (teamScores.get(TEAM1).getThrowsList().isEmpty() && teamScores.get(TEAM2).getThrowsList().isEmpty()) {
            startLeg = active;
            startSet = active;
        }
    }

    public void newPartialValue(int value) {
        partialThrow = partialThrow == null
                ? new X01Throw(value, PARTIAL, legCount, 0, false, PlayersEnumMap.get(active))
                : null;
    }
}
