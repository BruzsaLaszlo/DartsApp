package bruzsa.laszlo.dartsapp.model.x01;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.RED;
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.util.Locale.US;
import static bruzsa.laszlo.dartsapp.model.Team.TEAM1;
import static bruzsa.laszlo.dartsapp.model.Team.TEAM2;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;

import javax.inject.Inject;

import bruzsa.laszlo.dartsapp.enties.Player;
import bruzsa.laszlo.dartsapp.model.PlayersEnum;
import bruzsa.laszlo.dartsapp.model.Team;
import bruzsa.laszlo.dartsapp.model.home.AppSettings;
import bruzsa.laszlo.dartsapp.ui.webgui.WebGuiServer;
import bruzsa.laszlo.dartsapp.ui.webgui.WebGuiX01;
import bruzsa.laszlo.dartsapp.ui.x01.X01ThrowAdapter;
import bruzsa.laszlo.dartsapp.util.X01Checkout;
import dagger.hilt.android.lifecycle.HiltViewModel;
import lombok.Getter;

@HiltViewModel
public class X01ViewModel extends ViewModel {

    @Getter
    private final Map<Team, LiveDatas> liveDatasMap = Map.of(
            TEAM1, new LiveDatas(),
            TEAM2, new LiveDatas()
    );
    @Getter
    private final MutableLiveData<Boolean> set = new MutableLiveData<>();
    @Getter
    private final MutableLiveData<PlayersEnum> activePlayer = new MutableLiveData<>();
    @Getter
    private final MutableLiveData<Boolean> teamPlay = new MutableLiveData<>();
    @Getter
    private final Map<PlayersEnum, Player> playerMap;
    private final Map<Team, X01ThrowAdapter> throwAdapterMap;
    private final X01Service service;
    private final WebGuiX01 webGUI;
    private final WebGuiServer webGuiServer;
    private final X01Checkout x01Checkout;

    @Inject
    public X01ViewModel(WebGuiServer webGuiServer, WebGuiX01 webGUI,
                        AppSettings appSettings, X01Service service,
                        X01Checkout x01Checkout) {
        this.service = service;
        this.webGUI = webGUI;
        this.x01Checkout = x01Checkout;
        X01Settings settings = appSettings.getX01Settings();
        Map<PlayersEnum, Player> activePlayersMap = appSettings.getSelectedPlayers();
        this.webGuiServer = webGuiServer;
        playerMap = appSettings.getSelectedPlayers();

        activePlayer.setValue(service.getActive());

        teamPlay.setValue(activePlayersMap.size() == 4);

        set.setValue(settings.getMatchType() == X01MatchType.SET);

        throwAdapterMap = new EnumMap<>(Map.of(
                TEAM1, new X01ThrowAdapter(service.getThrowList(TEAM1), this::removeThrow, TEAM1),
                TEAM2, new X01ThrowAdapter(service.getThrowList(TEAM2), this::removeThrow, TEAM2)));
        liveDatasMap.get(TEAM1).getScore().setValue(valueOf(settings.getStartScore()));
        liveDatasMap.get(TEAM2).getScore().setValue(valueOf(settings.getStartScore()));

        set.setValue(settings.getMatchType() == X01MatchType.SET);

        updateWebGui(getStats());
    }

    public void newThrow(int throwValue, Consumer<Team> onGameOverEventListener, Context context) {
        if (service.isCheckout(throwValue)) {
            x01Checkout.getCheckoutDartsCount(throwValue, context, count ->
                    newThrow(throwValue, count, onGameOverEventListener));
        } else {
            newThrow(throwValue, 3, onGameOverEventListener);
        }
    }

    public void newPartialValue(int value) {
        service.newPartialValue(value);
        refreshGui();
    }

    private void newThrow(int throwValue, int dartsCount, Consumer<Team> onGameOverEventListener) {
        if (service.gameOver) return;
        if (service.isCheckout(throwValue)) {
            newCheckoutThrow(throwValue, dartsCount, onGameOverEventListener);
        } else {
            PlayersEnum player = service.newThrow(throwValue);
            refreshGuiAfterNewThrow(player);
        }
    }

    private void newCheckoutThrow(int throwValue, int dartCount, Consumer<Team> onGameOverEventListener) {
        PlayersEnum player = service.newThrow(throwValue, dartCount, onGameOverEventListener);
        throwAdapterMap.get(player.team).inserted();
        activePlayer.setValue(service.getActive());
        refreshGui();
    }

    private void refreshGuiAfterNewThrow(PlayersEnum player) {
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
        webGuiServer.setContent(html);
    }

    public void setActive(PlayersEnum player) {
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
        return format(Locale.ENGLISH, """
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
    public static void setColorAndFaceTypeForNameButtons(View view, MutableLiveData<PlayersEnum> active, PlayersEnum player) {
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

    public void showGameInfo(View view) {
        X01Settings settings = service.getSettings();
        new MaterialAlertDialogBuilder(view.getContext())
                .setTitle("Game info")
                .setMessage(format(US,
                        """
                                %s %d %s
                                Start from: %d
                                WinByTwoClearLeg %s""",
                        settings.getFirstToBestOf(), settings.getCount(), settings.getMatchType(),
                        settings.getStartScore(),
                        settings.isWinByTwoClearLeg()))
                .setPositiveButton("OK", null)
                .show();
    }

    @Getter
    public static class LiveDatas {

        private final MutableLiveData<String> set = new MutableLiveData<>("0");

        private final MutableLiveData<String> leg = new MutableLiveData<>("0");

        private final MutableLiveData<String> stat = new MutableLiveData<>("");

        private final MutableLiveData<String> score = new MutableLiveData<>("");

        private final MutableLiveData<Boolean> possibleCheckout = new MutableLiveData<>(false);

    }

}