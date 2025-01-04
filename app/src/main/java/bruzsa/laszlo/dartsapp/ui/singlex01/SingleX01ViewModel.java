package bruzsa.laszlo.dartsapp.ui.singlex01;

import static bruzsa.laszlo.dartsapp.model.Team.TEAM1;
import static bruzsa.laszlo.dartsapp.model.TeamPlayer.PLAYER_1_1;
import static bruzsa.laszlo.dartsapp.ui.webgui.WebServer.getWebServer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Locale;
import java.util.Map;
import java.util.function.IntConsumer;

import bruzsa.laszlo.dartsapp.enties.Player;
import bruzsa.laszlo.dartsapp.model.Team;
import bruzsa.laszlo.dartsapp.model.singlex01.X01SingleService;
import bruzsa.laszlo.dartsapp.model.x01.Stat;
import bruzsa.laszlo.dartsapp.model.x01.X01Settings;
import bruzsa.laszlo.dartsapp.model.x01.X01Throw;
import bruzsa.laszlo.dartsapp.ui.webgui.WebGuiX01;
import bruzsa.laszlo.dartsapp.ui.x01.X01ThrowAdapter;
import lombok.Getter;

public class SingleX01ViewModel extends ViewModel {

    private final MutableLiveData<Integer> leg = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> score = new MutableLiveData<>();
    private final MutableLiveData<String> stat = new MutableLiveData<>("");

    @Getter
    private X01ThrowAdapter throwsAdapter;
    private X01SingleService service;
    @Getter
    private boolean gameStarted;

    private WebGuiX01 webGui;


    public void startOrCountinue(Player player, int startScore, String htmlTemplate) {
        if (gameStarted) return;
        gameStarted = true;

        service = new X01SingleService(player, startScore);

        score.setValue(startScore);
        throwsAdapter = new X01ThrowAdapter(service.getThrowsList(), this::removeThrow, TEAM1);

        webGui = new WebGuiX01(htmlTemplate, Map.of(PLAYER_1_1, player), Map.of(TEAM1, service.getScores()), new X01Settings());
        updateWebGui(Stat.getEmptyStat(startScore));
    }

    @SuppressWarnings("ConstantConditions")
    public void newThrow(int throwValue, IntConsumer onCheckoutEventListener) {
        if (score.getValue() == throwValue) {
            onCheckoutEventListener.accept(throwValue);
        } else {
            newThrow(throwValue, 3);
        }
    }

    public void newThrow(int throwValue, int dartCount) {
        service.newThrow(throwValue, dartCount, (actualLeg, actualStat) -> {
            leg.setValue(actualLeg);
            updateGui(actualStat);
            throwsAdapter.inserted();
        });
    }

    public boolean removeThrow(X01Throw x01Throw, Team team) {
        return service.removeThrow(x01Throw, this::updateGui);
    }

    private void updateGui(Stat stat) {
        score.setValue(stat.getScore());
        if (stat.isEmpty()) return;
        this.stat.setValue(String.format(Locale.ENGLISH, """
                        %d
                        %d
                        %.1f
                        %d
                        %d
                        %s""",
                stat.getPlus100(),
                stat.getPlus60(),
                stat.getAverage(),
                stat.getMax(),
                stat.getMin(),
                stat.getHighestCheckout().map(String::valueOf).orElse("-")));
        updateWebGui(stat);
    }

    private void updateWebGui(Stat stat) {
        assert stat != null : "n u l l";
        String html = webGui.createHtml(Map.of(TEAM1, stat), PLAYER_1_1);
        getWebServer().setWebServerContent(html);
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

    public String getPlayerName() {
        return service.getScores().getPlayer1().getName();
    }

}