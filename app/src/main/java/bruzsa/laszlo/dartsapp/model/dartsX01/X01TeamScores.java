package bruzsa.laszlo.dartsapp.model.dartsX01;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bruzsa.laszlo.dartsapp.dao.Player;

public class X01TeamScores {

    private final List<X01Throw> throwsList = new ArrayList<>();
    private final X01SummaryStatistics stat = new X01SummaryStatistics(throwsList);
    private int legs;
    private int sets;

    private final Player player1;
    private final Player player2;

    public X01TeamScores(Player... players) {
        player1 = players[0];
        player2 = players.length == 2 ? players[1] : null;
    }

    public void addThrow(X01Throw dartsThrow) {
        throwsList.add(dartsThrow);
    }

    public int removeThrow(X01Throw dartsThrow) {
        int position = throwsList.indexOf(dartsThrow);
        throwsList.remove(position);
        return position;
    }

    public String getStat() {
        return stat.toString();
    }

    public List<X01Throw> getThrowsList() {
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
