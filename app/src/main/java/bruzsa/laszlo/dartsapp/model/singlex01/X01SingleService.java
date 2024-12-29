package bruzsa.laszlo.dartsapp.model.singlex01;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import bruzsa.laszlo.dartsapp.dao.Player;
import bruzsa.laszlo.dartsapp.model.x01.Stat;
import bruzsa.laszlo.dartsapp.model.x01.X01TeamScores;
import bruzsa.laszlo.dartsapp.model.x01.X01Throw;
import lombok.Getter;

public class X01SingleService {

    private final int startScore;

    @Getter
    private final X01TeamScores scores;


    public X01SingleService(Player player, int startScore) {
        this.scores = new X01TeamScores(player);
        this.startScore = startScore;
    }

    public void newThrow(int throwValue, int dartCount, BiConsumer<Integer, Stat> onNewThrowListener) {
        int newScore = calculateStat().getScore() - throwValue;
        boolean checkedOut = newScore == 0;
        X01Throw newThrow = new X01Throw(
                throwValue, newScore > 1 || checkedOut, scores.getLegs(), dartCount, checkedOut, scores.getPlayer1());
        scores.addThrow(newThrow);
        if (checkedOut) scores.wonLeg();
        onNewThrowListener.accept(scores.getLegs(), calculateStat());
    }

    public boolean removeThrow(X01Throw x01Throw, Consumer<Stat> onRemoveThrowListener) {
        boolean removable = x01Throw.getLeg() == scores.getLegs() && !x01Throw.isRemoved();
        if (removable) {
            x01Throw.setRemoved();
            onRemoveThrowListener.accept(calculateStat());
        }
        return removable;
    }

    public List<X01Throw> getThrowsList() {
        return Collections.unmodifiableList(scores.getThrowsList());
    }

    public Stat calculateStat() {
        return new Stat(startScore, scores.getLegs(), scores.getThrowsList());
    }

}
