package bruzsa.laszlo.dartsapp.model.x01;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bruzsa.laszlo.dartsapp.dao.Player;
import lombok.Getter;

public class X01TeamScores {

    private final List<X01Throw> throwsList = new ArrayList<>();
    private final X01SummaryStatistics stat = new X01SummaryStatistics(throwsList);
    @Getter
    private int legs;
    @Getter
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

    public X01SummaryStatistics getStat() {
        return stat;
    }

    public List<X01Throw> getThrowsList() {
        return Collections.unmodifiableList(throwsList);
    }

    public int getSum(int leg) {
        return stat.getSum(leg);
    }

    public int wonLeg() {
        return ++legs;
    }

    public int wonSet() {
        legs = 0;
        return ++sets;
    }

    public void loseSet() {
        legs = 0;
    }
}
