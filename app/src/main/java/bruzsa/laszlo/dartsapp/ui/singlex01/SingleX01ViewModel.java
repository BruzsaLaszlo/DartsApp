package bruzsa.laszlo.dartsapp.ui.singlex01;

import static bruzsa.laszlo.dartsapp.model.PlayersEnum.PLAYER_1_1;
import static bruzsa.laszlo.dartsapp.model.Team.TEAM1;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import bruzsa.laszlo.dartsapp.enties.Player;
import bruzsa.laszlo.dartsapp.model.Team;
import bruzsa.laszlo.dartsapp.model.home.AppSettings;
import bruzsa.laszlo.dartsapp.model.singlex01.X01SingleService;
import bruzsa.laszlo.dartsapp.model.x01.Stat;
import bruzsa.laszlo.dartsapp.model.x01.X01Settings;
import bruzsa.laszlo.dartsapp.model.x01.X01Throw;
import bruzsa.laszlo.dartsapp.ui.webgui.WebGuiServer;
import bruzsa.laszlo.dartsapp.ui.webgui.WebGuiX01;
import bruzsa.laszlo.dartsapp.ui.x01.X01ThrowAdapter;
import bruzsa.laszlo.dartsapp.util.X01Checkout;
import dagger.hilt.android.lifecycle.HiltViewModel;
import lombok.Getter;

@HiltViewModel
public class SingleX01ViewModel extends ViewModel {

    private final MutableLiveData<Integer> leg = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> score = new MutableLiveData<>(0);
    private final MutableLiveData<String> stat = new MutableLiveData<>("");

    @Getter
    private X01ThrowAdapter throwsAdapter;
    private final X01SingleService service;
    private final WebGuiX01 webGui;
    private final WebGuiServer webGuiServer;
    private final X01Checkout x01Checkout;

    @Inject
    public SingleX01ViewModel(WebGuiServer webGuiServer, WebGuiX01 webGui,
                              AppSettings appSettings, X01SingleService service,
                              X01Checkout x01Checkout) {
        this.webGui = webGui;
        this.service = service;
        this.x01Checkout = x01Checkout;
        X01Settings settings = appSettings.getX01Settings();
        Player player = appSettings.getSelectedPlayers().get(PLAYER_1_1);
        int startScore = settings.getStartScore();

        this.webGuiServer = webGuiServer;

        score.setValue(startScore);
        throwsAdapter = new X01ThrowAdapter(service.getThrowsList(), this::removeThrow, TEAM1);

        updateWebGui(Stat.getEmptyStat(startScore));
    }

    @SuppressWarnings("ConstantConditions")
    public void newThrow(int throwValue, Context context) {
        if (score.getValue() == throwValue) {
            x01Checkout.getCheckoutDartsCount(throwValue, context, count -> newThrow(throwValue, count));
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
        String html = webGui.createHtml(Map.of(TEAM1, stat), PLAYER_1_1);
        webGuiServer.setContent(html);
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