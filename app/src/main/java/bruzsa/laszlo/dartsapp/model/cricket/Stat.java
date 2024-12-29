package bruzsa.laszlo.dartsapp.model.cricket;

import static java.util.Map.of;
import static java.util.stream.Collectors.toMap;
import static bruzsa.laszlo.dartsapp.model.Team.TEAM1;
import static bruzsa.laszlo.dartsapp.model.Team.TEAM2;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import bruzsa.laszlo.dartsapp.model.Team;
import lombok.Getter;

@Getter
@SuppressWarnings("ConstantConditions")
public class Stat {

    private final Map<Team, Map<Integer, Integer>> statMap;
    private final Map<Team, Integer> scores = new EnumMap<>(of(TEAM1, 0, TEAM2, 0));

    public Stat(List<CricketThrow> throwList, List<Integer> activeNumbers) {
        var def = activeNumbers.stream().collect(toMap(number -> number, n -> 0));
        statMap = new EnumMap<>(of(TEAM1, def, TEAM2, new HashMap<>(def)));

        throwList.forEach(cricketThrow -> addThrow(cricketThrow)
                .ifPresent(addScore -> scores.compute(cricketThrow.getTeam(), (team, score) ->
                        score == null ? addScore : score + addScore)));
    }

    private Optional<Integer> addThrow(CricketThrow cricketThrow) {
        if (cricketThrow.isRemoved()) return Optional.empty();

        int number = cricketThrow.getNumber();
        int multiplier = cricketThrow.getMultiply();
        Team team = cricketThrow.getTeam();

        AtomicInteger addScore = new AtomicInteger(0);
        statMap.get(team).compute(number, (n, m) -> {
            if (m == null) {
                return multiplier;
            } else {
                int sum = m + multiplier;
                Integer opponentMultiplier = statMap.get(team.opponent()).get(number);
                if (sum > 3 && (opponentMultiplier == null || opponentMultiplier < 3)) {
                    int sm = m >= 3 ? multiplier : sum - 3;
                    addScore.set(sm * number);
                }
                return sum;
            }
        });

        if (addScore.get() == 0) return Optional.empty();
        return Optional.of(addScore.get());
    }

    public Map<Integer, State> getStateMap(Team team) {
        return statMap.get(team).entrySet().stream()
                .collect(toMap(
                        Map.Entry::getKey,
                        entry -> State.getState(entry.getValue(), statMap.get(team.opponent()).get(entry.getKey()))));
    }

}
