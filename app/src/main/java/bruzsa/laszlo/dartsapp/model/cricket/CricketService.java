package bruzsa.laszlo.dartsapp.model.cricket;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toMap;
import static bruzsa.laszlo.dartsapp.model.Team.TEAM1;
import static bruzsa.laszlo.dartsapp.model.Team.TEAM2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import bruzsa.laszlo.dartsapp.enties.Player;
import bruzsa.laszlo.dartsapp.model.PlayersEnum;
import bruzsa.laszlo.dartsapp.model.Team;
import bruzsa.laszlo.dartsapp.model.home.AppSettings;
import dagger.hilt.android.scopes.ViewModelScoped;

@SuppressWarnings("ConstantConditions")
@ViewModelScoped
public class CricketService {

    private final Map<Team, CricketTeam> cricketTeamMap;
    private final Map<PlayersEnum, Player> players;
    private final List<Integer> activeNumbers;
    private final List<CricketThrow> throwList = new ArrayList<>();

    @Inject
    public CricketService(AppSettings settings) {
        players = settings.getSelectedPlayers();
        this.activeNumbers = settings.getCricketSettings().getActiveNumbers();
        var team1 = new CricketTeam(players.get(TEAM1.player1()), players.get(TEAM1.player2()));
        var team2 = new CricketTeam(players.get(TEAM2.player1()), players.get(TEAM2.player2()));
        this.cricketTeamMap = Map.of(TEAM1, team1, TEAM2, team2);
    }

    public void newThrow(int multiplier, int number, Team team,
                         Runnable gameOverListener,
                         OnScoreChangeListener onScoreChangeListener) {
        if (!isThrowValid(number, team)) return;

        throwList.add(new CricketThrow(multiplier, number, cricketTeamMap.get(team), team));

        Stat stat = recalculateStat();
        onScoreChangeListener.onChange(stat);
        if (stat.isGameEnd()) gameOverListener.run();
    }

    private boolean isThrowValid(int number, Team team) {
        int value = calculateTeamStat(team).getOrDefault(number, 0);
        int valueOpponent = calculateTeamStat(team.opponent()).getOrDefault(number, 0);
        return value < 3 || (value >= 3 && valueOpponent < 3);
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

    public String getPlayerName(PlayersEnum player) {
        Player p = players.get(player);
        if (p == null) return "";
        else return p.getName();
    }
}
