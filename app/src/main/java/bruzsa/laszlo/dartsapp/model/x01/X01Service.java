package bruzsa.laszlo.dartsapp.model.x01;

import static bruzsa.laszlo.dartsapp.model.Team.TEAM1;
import static bruzsa.laszlo.dartsapp.model.Team.TEAM2;
import static bruzsa.laszlo.dartsapp.model.x01.Status.PARTIAL;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import bruzsa.laszlo.dartsapp.enties.Player;
import bruzsa.laszlo.dartsapp.enties.x01.X01TeamScores;
import bruzsa.laszlo.dartsapp.model.Team;
import bruzsa.laszlo.dartsapp.model.TeamPlayer;
import lombok.Getter;

@SuppressWarnings("ConstantConditions")
public class X01Service {

    @Getter
    private TeamPlayer active = TEAM1.player1();
    private TeamPlayer startLeg = TEAM1.player1();
    private TeamPlayer startSet = TEAM1.player1();
    private int legCount;
    private final X01Settings settings;
    private final boolean teamPlay;
    @Getter
    private final Map<Team, X01TeamScores> teamScores;
    private X01Throw partialThrow;
    private final Map<TeamPlayer, Player> teamPlayerMap;
    @Getter
    boolean gameOver;

    public X01Service(Map<TeamPlayer, Player> teamPlayerMap, X01Settings settings) {
        this.teamPlayerMap = teamPlayerMap;
        X01TeamScores team1 = new X01TeamScores(teamPlayerMap.get(TEAM1.player1()), teamPlayerMap.get(TEAM1.player2()));
        X01TeamScores team2 = new X01TeamScores(teamPlayerMap.get(TEAM2.player1()), teamPlayerMap.get(TEAM2.player2()));
        teamScores = Map.of(TEAM1, team1, TEAM2, team2);
        teamPlay = teamPlayerMap.size() == TeamPlayer.values().length;
        this.settings = settings;
    }

    public TeamPlayer newThrow(int throwValue) {
        return newThrow(throwValue, 3, null);
    }

    public TeamPlayer newThrow(int throwValue, int dartCount, Consumer<Team> onGameOverEvent) {
        partialThrow = null;

        var actual = active;
        Team team = actual.team;
        X01TeamScores aPlayer = teamScores.get(team);
        X01TeamScores opponent = teamScores.get(team.opponent());

        int newScore = getStat(team).getScore() - throwValue;

        boolean checkedOut = newScore == 0;
        X01Throw newThrow = new X01Throw(
                throwValue,
                Status.getStatus(newScore > 1 || checkedOut),
                legCount,
                dartCount,
                checkedOut,
                teamPlayerMap.get(active));
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
        if (x01Throw.getLeg() == legCount && !x01Throw.isRemoved()) {
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

    public void setActive(TeamPlayer active) {
        partialThrow = null;
        this.active = active;
        if (teamScores.get(TEAM1).getThrowsList().isEmpty() && teamScores.get(TEAM2).getThrowsList().isEmpty()) {
            startLeg = active;
            startSet = active;
        }
    }

    public void newPartialValue(int value) {
        partialThrow = partialThrow == null
                ? new X01Throw(value, PARTIAL, legCount, 0, false, teamPlayerMap.get(active))
                : null;
    }
}
