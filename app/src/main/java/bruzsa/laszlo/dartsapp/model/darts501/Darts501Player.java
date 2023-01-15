package bruzsa.laszlo.dartsapp.model.darts501;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bruzsa.laszlo.dartsapp.dao.Player;

public class Darts501Player {

    private final Player player;
    private final List<Darts501Throw> throwsList = new ArrayList<>();
    private final Darts501SummaryStatistics stat = new Darts501SummaryStatistics(throwsList);

    private int legs;
    private int sets;


    public Darts501Player(Player player) {
        this.player = player;
    }

    public Darts501Player(Darts501Player darts501Player) {
        player = darts501Player.player;
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

    @NonNull
    @NotNull
    public String getPlayerName() {
        return player.getName();
    }

    @NonNull
    @NotNull
    public Player getPlayer() {
        return player;
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
