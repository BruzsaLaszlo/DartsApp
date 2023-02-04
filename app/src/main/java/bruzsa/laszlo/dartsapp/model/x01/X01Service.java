package bruzsa.laszlo.dartsapp.model.x01;

import static bruzsa.laszlo.dartsapp.model.Team.TEAM1;
import static bruzsa.laszlo.dartsapp.model.Team.TEAM2;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import bruzsa.laszlo.dartsapp.dao.Player;
import bruzsa.laszlo.dartsapp.model.Team;
import bruzsa.laszlo.dartsapp.model.TeamPlayer;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("ConstantConditions")
public class X01Service {

    @Getter
    @Setter
    private TeamPlayer active = TEAM1.player1();
    private TeamPlayer startLeg = TEAM1.player1();
    private TeamPlayer startSet = TEAM1.player1();
    private int currentLeg;
    private final X01GameSettings settings;
    private final boolean teamPlay;
    private final Map<Team, X01TeamScores> teamScores;
    private final Map<TeamPlayer, Player> teamPlayerMap;

    public X01Service(Map<TeamPlayer, Player> teamPlayerMap, X01GameSettings settings) {
        this.teamPlayerMap = teamPlayerMap;
        X01TeamScores team1 = new X01TeamScores(settings.getStartScore(), teamPlayerMap.get(TEAM1.player1()), teamPlayerMap.get(TEAM1.player2()));
        X01TeamScores team2 = new X01TeamScores(settings.getStartScore(), teamPlayerMap.get(TEAM2.player1()), teamPlayerMap.get(TEAM2.player2()));
        teamScores = Map.of(TEAM1, team1, TEAM2, team2);
        teamPlay = teamPlayerMap.size() == TeamPlayer.values().length;
        this.settings = settings;
    }


    public void newThrow(int throwValue, BiConsumer<Boolean, TeamPlayer> callback) {
        Team team = active.team;
        X01TeamScores aPlayer = teamScores.get(team);
        X01TeamScores opponent = teamScores.get(team.opponent());
        int newScore = teamScores.get(team).getStat().getScore(currentLeg) - throwValue;

        boolean checkedOut = newScore == 0;
        X01Throw newThrow = new X01Throw(
                throwValue, newScore > 1 || checkedOut, currentLeg, 3, checkedOut, teamPlayerMap.get(active));
        aPlayer.addThrow(newThrow);

        if (checkedOut) {
            boolean gameOver = checkout(aPlayer, opponent, settings.getFirstTo());
            callback.accept(gameOver, active);
        }
        callback.accept(false, active);
        active = active.nextPlayer(teamPlay);
    }

    private boolean checkout(X01TeamScores aPlayer, X01TeamScores opponent, int maxLegSet) {
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

    public boolean removeThrow(X01Throw x01Throw, Runnable onThrowRemovedEvent) {
        if (x01Throw.getLeg() == currentLeg && !x01Throw.isRemoved()) {
            x01Throw.setRemoved();
            onThrowRemovedEvent.run();
            return true;
        }
        return false;
    }

    public int getScore(Team team) {
        return teamScores.get(team).getStat().getScore(currentLeg);
    }

    public X01SummaryStatistics getStat(Team team) {
        return teamScores.get(team).getStat();
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
}
