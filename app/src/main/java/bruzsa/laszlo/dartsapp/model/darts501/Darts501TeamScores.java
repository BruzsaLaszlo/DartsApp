package bruzsa.laszlo.dartsapp.model.darts501;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bruzsa.laszlo.dartsapp.dao.Player;

public class Darts501TeamScores {

    private final List<Darts501Throw> throwsList = new ArrayList<>();
    private final Darts501SummaryStatistics stat = new Darts501SummaryStatistics(throwsList);
    private int legs;
    private int sets;

    private final Player player1;
    private final Player player2;

    public Darts501TeamScores(Player... players) {
        player1 = players[0];
        player2 = players.length == 2 ? players[1] : null;
    }

    public void addThrow(Darts501Throw dartsThrow) {
        throwsList.add(dartsThrow);
    }

    public int removeThrow(Darts501Throw dartsThrow) {
        int position = throwsList.indexOf(dartsThrow);
        throwsList.remove(position);
        return position;
    }

    public String getStat() {
        return stat.toString();
    }

    public List<Darts501Throw> getThrowsList() {
        return Collections.unmodifiableList(throwsList);
    }

    public int getSum() {
        return stat.getSum();
    }

    public int getLegs() {
        return legs;
    }

    public int wonLeg() {
        return ++legs;
    }

    public int getSets() {
        return sets;
    }

    public int wonSet() {
        legs = 0;
        return ++sets;
    }

    public void loseSet() {
        legs = 0;
    }
}
