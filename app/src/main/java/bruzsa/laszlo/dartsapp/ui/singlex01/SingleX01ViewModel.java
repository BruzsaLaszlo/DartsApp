package bruzsa.laszlo.dartsapp.ui.singlex01;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.function.BiConsumer;

import bruzsa.laszlo.dartsapp.dao.Player;
import bruzsa.laszlo.dartsapp.model.Team;
import bruzsa.laszlo.dartsapp.model.singlex01.X01SingleService;
import bruzsa.laszlo.dartsapp.model.x01.X01SummaryStatistics;
import bruzsa.laszlo.dartsapp.model.x01.X01Throw;
import bruzsa.laszlo.dartsapp.ui.x01.X01ThrowAdapter;
import lombok.Getter;
import lombok.Setter;

public class SingleX01ViewModel extends ViewModel {

    private final MutableLiveData<Integer> leg = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> score = new MutableLiveData<>();
    private final MutableLiveData<String> stat = new MutableLiveData<>("");
    @Getter
    private X01ThrowAdapter throwsAdapter;
    private X01SingleService service;
    @Getter
    private boolean gameStarted;

    @Setter
    private BiConsumer<Player, X01SummaryStatistics> onGuiChangeEvent;

    public void startOrCountinue(Player player, int startScore) {
        if (gameStarted) return;
        service = new X01SingleService(player, startScore);
        score.setValue(startScore);
        throwsAdapter = new X01ThrowAdapter(service.getThrowsList(), this::removeThrow, null);
        gameStarted = true;
        onGuiChangeEvent.accept(service.getPlayer(), service.getStat());
    }

    public void newThrow(int throwValue, int dartCount) {
        service.newThrow(throwValue, dartCount, (actualLeg, actualScore) -> {
            updateGui();
            throwsAdapter.inserted();
        });
    }

    public boolean removeThrow(X01Throw x01Throw, Team team) {
        boolean removed = service.removeThrow(x01Throw);
        if (removed) updateGui();
        return removed;
    }

    private void updateGui() {
        score.setValue(service.getScore());
        leg.setValue(service.getLeg());
        stat.setValue(service.getStat().toString());
        onGuiChangeEvent.accept(service.getPlayer(), service.getStat());
    }

    public X01SummaryStatistics getSummaryStatistics() {
        return service.getStat();
    }


    public LiveData<Integer> getScore() {
        return score;
    }

    public LiveData<Integer> getLegs() {
        return leg;
    }

    public LiveData<String> getStat() {
        return stat;
    }

}