package bruzsa.laszlo.dartsapp.model.singlex01;

import static bruzsa.laszlo.dartsapp.model.PlayersEnum.PLAYER_1_1;
import static bruzsa.laszlo.dartsapp.model.x01.Status.getStatus;
import static bruzsa.laszlo.dartsapp.model.x01.ThrowValidator.isValidCheckout;
import static bruzsa.laszlo.dartsapp.model.x01.ThrowValidator.isValidThrow;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.inject.Inject;

import bruzsa.laszlo.dartsapp.enties.x01.X01TeamScores;
import bruzsa.laszlo.dartsapp.model.home.AppSettings;
import bruzsa.laszlo.dartsapp.model.x01.Stat;
import bruzsa.laszlo.dartsapp.model.x01.X01Throw;
import dagger.hilt.android.scopes.ViewModelScoped;
import lombok.Getter;

@ViewModelScoped
public class X01SingleService {

    private final int startScore;

    @Getter
    private final X01TeamScores scores;


    @Inject
    public X01SingleService(AppSettings appSettings) {
        this.scores = new X01TeamScores(appSettings.getSelectedPlayers().get(PLAYER_1_1));
        this.startScore = appSettings.getX01Settings().getStartScore();
    }

    public void newThrow(int throwValue, int dartCount, BiConsumer<Integer, Stat> onNewThrowListener) {
        int newScore = calculateStat().getScore() - throwValue;
        boolean checkedOut = newScore == 0;
        X01Throw newThrow = new X01Throw(
                throwValue,
                getStatus(checkedOut ? isValidCheckout(throwValue) : newScore > 1 && isValidThrow(throwValue)),
                scores.getLegs(),
                dartCount,
                checkedOut,
                scores.getPlayer1().getId());
        scores.addThrow(newThrow);
        if (checkedOut) scores.wonLeg();
        onNewThrowListener.accept(scores.getLegs(), calculateStat());
    }

    public boolean removeThrow(X01Throw x01Throw, Consumer<Stat> onRemoveThrowListener) {
        boolean removable = x01Throw.getLeg() == scores.getLegs() && x01Throw.isNotRemoved();
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
        return Stat.calculate(startScore, scores.getLegs(), scores.getThrowsList());
    }

}
