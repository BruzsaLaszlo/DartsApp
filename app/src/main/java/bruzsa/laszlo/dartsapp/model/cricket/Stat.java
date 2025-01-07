package bruzsa.laszlo.dartsapp.model.cricket;

import static java.util.Map.of;
import static java.util.stream.Collectors.toMap;
import static bruzsa.laszlo.dartsapp.model.Team.TEAM1;
import static bruzsa.laszlo.dartsapp.model.Team.TEAM2;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bruzsa.laszlo.dartsapp.model.Team;
import lombok.Getter;

@SuppressWarnings("ConstantConditions")
@Getter
public class Stat {

    private final Map<Team, Map<Integer, Integer>> statMap;
    private final Map<Team, Integer> scores = new EnumMap<>(of(
            TEAM1, 0,
            TEAM2, 0));

    public Stat(List<CricketThrow> throwList, List<Integer> activeNumbers) {
        var def = activeNumbers.stream().collect(toMap(number -> number, n -> 0));
        statMap = new EnumMap<>(of(TEAM1, def, TEAM2, new HashMap<>(def)));
        calculateStat(throwList);
    }

    private void calculateStat(List<CricketThrow> throwList) {
        for (CricketThrow cricketThrow : throwList) {
            if (!cricketThrow.isRemoved()) {
                int addScore = addThrow(cricketThrow);
                scores.compute(cricketThrow.getTeam(), (team, score) ->
                        score == null ? addScore : score + addScore);
            }
        }
    }

    private int addThrow(CricketThrow cricketThrow) {
        int number = cricketThrow.getNumber();
        int multiplier = cricketThrow.getMultiply();
        Team team = cricketThrow.getTeam();

        int sumMultiplier = statMap.get(team).compute(number, (n, m) ->
                m == null ? multiplier : m + multiplier);

        Integer opponentMultiplier = statMap.get(team.opponent()).get(number);
        if (sumMultiplier > 3 && (opponentMultiplier == null || opponentMultiplier < 3)) {
            int sm = multiplier >= 3 ? multiplier : sumMultiplier - 3;
            return sm * number;
        }
        return 0;
    }

    public Map<Integer, State> getStateMap(Team team) {
        return statMap.get(team).entrySet().stream()
                .collect(toMap(
                        Map.Entry::getKey,
                        entry -> State.getState(entry.getValue(), statMap.get(team.opponent()).get(entry.getKey()))));
    }

    public boolean isGameEnd() {
        boolean player1ThrowAll = statMap.get(TEAM1).size() == 7 &&
                statMap.get(TEAM1).values().stream().allMatch(m -> m >= 3);
        boolean player2ThrowAll = statMap.get(TEAM2).size() == 7 &&
                statMap.get(TEAM2).values().stream().allMatch(m -> m >= 3);
        return (player1ThrowAll && scores.get(TEAM1) > scores.get(TEAM2)) ||
                (player2ThrowAll && scores.get(TEAM2) > scores.get(TEAM1));
    }

}
