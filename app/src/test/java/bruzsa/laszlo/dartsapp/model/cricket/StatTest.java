package bruzsa.laszlo.dartsapp.model.cricket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static bruzsa.laszlo.dartsapp.model.Team.TEAM1;
import static bruzsa.laszlo.dartsapp.model.cricket.CricketSettings.defaultNumbers;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import bruzsa.laszlo.dartsapp.enties.Player;

class StatTest {

    @Test
    void getScores() {

        CricketTeam team1 = new CricketTeam(new Player("John Doe"));
        CricketTeam team2 = new CricketTeam(new Player("Joe Doe"));
        Stat stat;
        List<CricketThrow> throwList = new ArrayList<>();
        throwList.add(new CricketThrow(3, 20, team1, TEAM1));
        throwList.add(new CricketThrow(3, 20, team1, TEAM1));
        throwList.add(new CricketThrow(3, 20, team1, TEAM1));
        stat = new Stat(throwList, defaultNumbers);
        assertEquals(120, stat.getScores().get(TEAM1));

        throwList.add(new CricketThrow(1, 20, team1, TEAM1));
        stat = new Stat(throwList, defaultNumbers);
        assertEquals(140, stat.getScores().get(TEAM1));

        throwList.add(new CricketThrow(1, 20, team1, TEAM1));
        stat = new Stat(throwList, defaultNumbers);
        assertEquals(160, stat.getScores().get(TEAM1));

        throwList.add(new CricketThrow(1, 20, team1, TEAM1));
        stat = new Stat(throwList, defaultNumbers);
        assertEquals(180, stat.getScores().get(TEAM1));

        throwList.add(new CricketThrow(1, 20, team1, TEAM1));
        stat = new Stat(throwList, defaultNumbers);
        assertEquals(200, stat.getScores().get(TEAM1));

        throwList.add(new CricketThrow(1, 20, team1, TEAM1));
        stat = new Stat(throwList, defaultNumbers);
        assertEquals(220, stat.getScores().get(TEAM1));
        assertFalse(stat.isGameEnd());
    }
}