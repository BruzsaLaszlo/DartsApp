package bruzsa.laszlo.dartsapp.model.cricket;

import static bruzsa.laszlo.dartsapp.model.Team.TEAM1;
import static bruzsa.laszlo.dartsapp.model.Team.TEAM2;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import bruzsa.laszlo.dartsapp.AppSettings;
import bruzsa.laszlo.dartsapp.model.PlayersEnum;
import bruzsa.laszlo.dartsapp.model.Team;
import bruzsa.laszlo.dartsapp.ui.cricket.CricketThrowsAdapter;
import bruzsa.laszlo.dartsapp.ui.webgui.WebGuiCricket;
import bruzsa.laszlo.dartsapp.ui.webgui.WebGuiServer;
import dagger.hilt.android.lifecycle.HiltViewModel;
import lombok.Getter;

@HiltViewModel
public class CricketViewModel extends ViewModel {

    @Getter
    private final MutableLiveData<String> score = new MutableLiveData<>("0:0");
    @Getter
    private final MutableLiveData<Stat> stat = new MutableLiveData<>();
    private final WebGuiCricket webGUI;
    private final CricketService service;
    @Getter
    private final CricketThrowsAdapter throwsAdapter;
    private final WebGuiServer webGuiServer;
    @Getter
    private final CricketSettings settings;

    @Inject
    public CricketViewModel(WebGuiCricket webGUI, WebGuiServer webGuiServer,
                            CricketService service, AppSettings settings) {
        this.webGUI = webGUI;
        this.service = service;
        this.webGuiServer = webGuiServer;
        this.settings = settings.getCricketSettings();

        throwsAdapter = getThrowsAdapter(service);

        stat.setValue(service.getEmptyStat());
        refreshWebGui(service.getEmptyStat());
    }

    @NonNull
    private CricketThrowsAdapter getThrowsAdapter(CricketService service) {
        return new CricketThrowsAdapter(service.getThrows(), cricketThrow ->
                service.removeThrow(cricketThrow, this::getOnScoreChangeListener));
    }

    public void newThrow(int multiplier, int value, Team team, Runnable gameOverListener) {
        service.newThrow(multiplier, value, team, gameOverListener,
                statistics -> {
                    getOnScoreChangeListener(statistics);
                    throwsAdapter.addNewThrow();
                });
    }

    private void getOnScoreChangeListener(Stat stat) {
        score.setValue(stat.getScores().get(TEAM1) + " : " + stat.getScores().get(TEAM2));
        this.stat.setValue(stat);
        refreshWebGui(stat);
    }

    private void refreshWebGui(Stat stat) {
        String html = webGUI.createHtml(stat);
        webGuiServer.setContent(html);
    }

    public String getPlayerName(PlayersEnum player) {
        return service.getPlayerName(player);
    }

}