package bruzsa.laszlo.dartsapp.model.cricket;

import static bruzsa.laszlo.dartsapp.model.Team.TEAM1;
import static bruzsa.laszlo.dartsapp.model.Team.TEAM2;
import static bruzsa.laszlo.dartsapp.ui.webgui.WebServer.getWebServer;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Map;

import bruzsa.laszlo.dartsapp.enties.Player;
import bruzsa.laszlo.dartsapp.model.Team;
import bruzsa.laszlo.dartsapp.model.TeamPlayer;
import bruzsa.laszlo.dartsapp.ui.cricket.CricketThrowsAdapter;
import bruzsa.laszlo.dartsapp.ui.webgui.WebGuiCricket;
import lombok.Getter;

public class CricketViewModel extends ViewModel {

    @Getter
    private final MutableLiveData<String> score = new MutableLiveData<>("0:0");
    @Getter
    private final MutableLiveData<Stat> stat = new MutableLiveData<>();
    private WebGuiCricket webGUI;

    private CricketService service;
    @Getter
    private CricketThrowsAdapter throwsAdapter;
    private Map<TeamPlayer, Player> players;

    public void startNewGameOrContinue(Map<TeamPlayer, Player> players, CricketSettings settings,
                                       OnNewGameCallback callback, String htmlTemplate) {
        if (service != null) return;

        this.players = players;
        var team1 = new CricketTeam(players.get(TEAM1.player1()), players.get(TEAM1.player2()));
        var team2 = new CricketTeam(players.get(TEAM2.player1()), players.get(TEAM2.player2()));
        service = new CricketService(Map.of(TEAM1, team1, TEAM2, team2), settings.getActiveNumbers());
        throwsAdapter = new CricketThrowsAdapter(service.getThrows(), cricketThrow ->
                service.removeThrow(cricketThrow, this::getOnScoreChangeListener));
        callback.onNewGame(throwsAdapter);

        stat.setValue(service.getEmptyStat());

        webGUI = new WebGuiCricket(htmlTemplate, players, settings.getActiveNumbers());
        refreshWebGui(service.getEmptyStat());
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
        getWebServer().setWebServerContent(html);
    }

    public interface OnNewGameCallback {
        void onNewGame(CricketThrowsAdapter throwsAdapter);
    }

    public String getPlayerName(TeamPlayer player) {
        Player p = players.get(player);
        if (p == null) return "";
        else return p.getName();
    }

}