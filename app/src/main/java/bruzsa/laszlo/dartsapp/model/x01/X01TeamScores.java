package bruzsa.laszlo.dartsapp.model.x01;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bruzsa.laszlo.dartsapp.dao.Player;
import lombok.Getter;

@Getter
public class X01TeamScores {

    private final List<X01Throw> throwsList = new ArrayList<>();
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

    public List<X01Throw> getThrowsList() {
        return Collections.unmodifiableList(throwsList);
    }

    public int wonLeg() {
        return ++legs;
    }

    public void wonSet() {
        legs = 0;
        sets++;
    }

    public void loseSet() {
        legs = 0;
    }
}
