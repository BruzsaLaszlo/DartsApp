package bruzsa.laszlo.dartsapp.model.cricket;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toMap;
import static bruzsa.laszlo.dartsapp.model.Team.TEAM1;
import static bruzsa.laszlo.dartsapp.model.Team.TEAM2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import bruzsa.laszlo.dartsapp.model.Team;

@SuppressWarnings("ConstantConditions")
public class CricketService {

    private final Map<Team, CricketTeam> cricketTeamMap;
    private final List<Integer> activeNumbers;
    private final List<CricketThrow> throwList = new ArrayList<>();

    public CricketService(Map<Team, CricketTeam> cricketTeamMap, List<Integer> activeNumbers) {
        this.cricketTeamMap = cricketTeamMap;
        this.activeNumbers = activeNumbers;
    }

    public void newThrow(int multiplier, int number, Team team, Runnable gameOverListener,
                         OnScoreChangeListener onScoreChangeListener) {
        if (!isThrowValid(number, team)) return;

        throwList.add(new CricketThrow(multiplier, number, cricketTeamMap.get(team), team));

        Stat stat = recalculateStat();
        onScoreChangeListener.onChange(stat);
        if (isGameEnd(stat)) gameOverListener.run();
    }

    private boolean isThrowValid(int number, Team team) {
        int value = calculateTeamStat(team).getOrDefault(number, 0);
        int valueOpponent = calculateTeamStat(team.opponent()).getOrDefault(number, 0);
        return value < 3 || (value >= 3 && valueOpponent < 3);
    }


    private boolean isGameEnd(Stat stat) {
        boolean player1ThrowAll = stat.getStatMap().get(TEAM1).size() == 7 &&
                stat.getStatMap().get(TEAM1).values().stream().allMatch(m -> m >= 3);
        boolean player2ThrowAll = stat.getStatMap().get(TEAM2).size() == 7 &&
                stat.getStatMap().get(TEAM2).values().stream().allMatch(m -> m >= 3);
        return (player1ThrowAll && stat.getScores().get(TEAM1) > stat.getScores().get(TEAM2)) ||
                (player2ThrowAll && stat.getScores().get(TEAM2) > stat.getScores().get(TEAM1));
    }

    private Map<Integer, Integer> calculateTeamStat(Team team) {
        return getThrows().stream()
                .filter(cricketThrow -> !cricketThrow.isRemoved())
                .filter(cricketThrow -> cricketThrow.getTeam() == team)
                .collect(toMap(CricketThrow::getNumber, CricketThrow::getMultiply, Integer::sum));
    }

    public void removeThrow(CricketThrow cricketThrow, OnScoreChangeListener onScoreChangeListener) {
        if (cricketThrow.setRemoved()) {
            onScoreChangeListener.onChange(recalculateStat());
        }
    }

    public Stat recalculateStat() {
        return new Stat(throwList, activeNumbers);
    }

    public Stat getEmptyStat() {
        return new Stat(emptyList(), activeNumbers);
    }

    public List<CricketThrow> getThrows() {
        return Collections.unmodifiableList(throwList);
    }

    public interface OnScoreChangeListener {
        void onChange(Stat stat);
    }
}
