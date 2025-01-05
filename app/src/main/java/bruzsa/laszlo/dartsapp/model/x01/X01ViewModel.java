package bruzsa.laszlo.dartsapp.model.x01;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.RED;
import static java.lang.String.valueOf;
import static bruzsa.laszlo.dartsapp.model.Team.TEAM1;
import static bruzsa.laszlo.dartsapp.model.Team.TEAM2;
import static bruzsa.laszlo.dartsapp.ui.webgui.WebServer.getWebServer;

import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import bruzsa.laszlo.dartsapp.enties.Player;
import bruzsa.laszlo.dartsapp.model.Team;
import bruzsa.laszlo.dartsapp.model.TeamPlayer;
import bruzsa.laszlo.dartsapp.ui.webgui.WebGuiX01;
import bruzsa.laszlo.dartsapp.ui.x01.X01ThrowAdapter;
import lombok.Getter;

@SuppressWarnings("ConstantConditions")
public class X01ViewModel extends ViewModel {

    @Getter
    private final Map<Team, LiveDatas> liveDatasMap = Map.of(
            TEAM1, new LiveDatas(),
            TEAM2, new LiveDatas()
    );
    @Getter
    private final MutableLiveData<Boolean> set = new MutableLiveData<>();
    @Getter
    private final MutableLiveData<TeamPlayer> activePlayer = new MutableLiveData<>();
    @Getter
    private final MutableLiveData<Boolean> teamPlay = new MutableLiveData<>();

    private Map<Team, X01ThrowAdapter> throwAdapterMap;
    private boolean gameStarted;
    private X01Service service;
    private WebGuiX01 webGUI;


    public void startGameOrContinue(Map<TeamPlayer, Player> activePlayersMap, X01Settings settings, String htmlTemplate) {
        if (gameStarted) return;
        gameStarted = true;

        service = new X01Service(activePlayersMap, settings);

        activePlayer.setValue(service.getActive());

        teamPlay.setValue(activePlayersMap.size() == 4);

        set.setValue(settings.getMatchType() == X01MatchType.SET);

        throwAdapterMap = new EnumMap<>(Map.of(
                TEAM1, new X01ThrowAdapter(service.getThrowList(TEAM1), this::removeThrow, TEAM1),
                TEAM2, new X01ThrowAdapter(service.getThrowList(TEAM2), this::removeThrow, TEAM2)
        ));
        liveDatasMap.get(TEAM1).getScore().setValue(valueOf(settings.getStartScore()));
        liveDatasMap.get(TEAM2).getScore().setValue(valueOf(settings.getStartScore()));

        set.setValue(settings.getMatchType() == X01MatchType.SET);

        webGUI = new WebGuiX01(htmlTemplate, activePlayersMap, service.getTeamScores(), settings);
        updateWebGui(getStats());
    }

    public void newThrow(int throwValue, BiConsumer<Darts, Consumer<Integer>> onDartsCount, Consumer<Team> onGameOverEventListener) {
        if (service.isCheckout(throwValue)) {
            if (throwValue > 110 || List.of(109, 108, 106, 105, 103, 102).contains(throwValue)) {
                newThrow(throwValue, 3, onGameOverEventListener);
            } else if ((throwValue > 40 && throwValue != 50) || throwValue % 2 == 1) {
                onDartsCount.accept(Darts.TWO, dartsCount ->
                        newThrow(throwValue, dartsCount, onGameOverEventListener));
            } else {
                onDartsCount.accept(Darts.THREE, dartsCount ->
                        newThrow(throwValue, dartsCount, onGameOverEventListener));
            }
        } else {
            newThrow(throwValue, 3, onGameOverEventListener);
        }
    }

    public void newPartialValue(int value) {
        service.newPartialValue(value);
        refreshGui();
    }

    public enum Darts {TWO, THREE}

    private void newThrow(int throwValue, int dartsCount, Consumer<Team> onGameOverEventListener) {
        if (service.gameOver) return;
        if (service.isCheckout(throwValue)) {
            newCheckoutThrow(throwValue, dartsCount, onGameOverEventListener);
        } else {
            TeamPlayer player = service.newThrow(throwValue);
            refreshGuiAfterNewThrow(player);
        }
    }


    private void newCheckoutThrow(int throwValue, int dartCount, Consumer<Team> onGameOverEventListener) {
        TeamPlayer player = service.newThrow(throwValue, dartCount, onGameOverEventListener);
        throwAdapterMap.get(player.team).inserted();
        activePlayer.setValue(service.getActive());
        refreshGui();
    }

    private void refreshGuiAfterNewThrow(TeamPlayer player) {
        throwAdapterMap.get(player.team).inserted();
        refreshGui();
        activePlayer.setValue(service.getActive());
    }


    public void refreshGui() {
        Map<Team, Stat> stats = getStats();
        stats.forEach((team, stat) -> {
            liveDatasMap.get(team).score.setValue(valueOf(stat.getScore()));
            liveDatasMap.get(team).set.setValue(service.getSet(team));
            liveDatasMap.get(team).leg.setValue(service.getLeg(team));
            liveDatasMap.get(team).stat.setValue(getStatAsString(stat));
            liveDatasMap.get(team).possibleCheckout.setValue(isOut(stat.getScore()));
        });
        updateWebGui(stats);
    }

    private Map<Team, Stat> getStats() {
        return Map.of(TEAM1, service.getStat(TEAM1), TEAM2, service.getStat(TEAM2));
    }

    private void updateWebGui(Map<Team, Stat> stats) {
        String html = webGUI.createHtml(stats, service.getActive());
        getWebServer().setWebServerContent(html);
    }

    public void setActive(TeamPlayer player) {
        if (service.gameOver) return;
        service.setActive(player);
        activePlayer.setValue(player);
        service.newPartialValue(0);
        refreshGui();
    }


    public boolean removeThrow(X01Throw x01Throw, Team team) {
        return service.removeThrow(x01Throw, this::refreshGui);
    }

    public boolean isOut(int score) {
        return (score > 1 && score < 159) || List.of(160, 161, 164, 167, 170).contains(score);
    }

    private String getStatAsString(Stat stat) {
        if (stat.isEmpty()) return "";
        return String.format(Locale.ENGLISH, """
                        %d
                        %d
                        %d
                        %d
                        %d
                        %s""",
                stat.getPlus100(),
                stat.getPlus60(),
                (int) stat.getAverage(),
                stat.getMax(),
                stat.getMin(),
                stat.getHighestCheckout().map(Object::toString).orElse("-"));
    }

    public X01ThrowAdapter getThrowsAdapterTeam(Team team) {
        return throwAdapterMap.get(team);
    }

    @BindingAdapter(value = {"active", "player"}, requireAll = false)
    public static void setColorAndFaceTypeForNameButtons(View view, MutableLiveData<TeamPlayer> active, TeamPlayer player) {
        if (view instanceof Button button) {
            if (active.getValue() == player) {
                button.setTextColor(RED);
                button.setTypeface(Typeface.DEFAULT_BOLD);
            } else {
                button.setTextColor(BLACK);
                button.setTypeface(Typeface.DEFAULT);
            }
        }
    }

//    public void getGameInfo(View view){
//        new AlertDialog.Builder(view.getContext())
//                .setTitle("Game info")
//                .setTitle(String.format(US,
//                        """
//                               %s"""),)
//    }

    @Getter
    public static class LiveDatas {

        private final MutableLiveData<String> set = new MutableLiveData<>("0");

        private final MutableLiveData<String> leg = new MutableLiveData<>("0");

        private final MutableLiveData<String> stat = new MutableLiveData<>("");

        private final MutableLiveData<String> score = new MutableLiveData<>();

        private final MutableLiveData<Boolean> possibleCheckout = new MutableLiveData<>(false);

    }

}