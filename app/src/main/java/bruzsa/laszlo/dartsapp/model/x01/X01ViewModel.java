package bruzsa.laszlo.dartsapp.model.x01;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.RED;
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.util.Locale.US;
import static bruzsa.laszlo.dartsapp.model.Team.TEAM1;
import static bruzsa.laszlo.dartsapp.model.Team.TEAM2;
import static bruzsa.laszlo.dartsapp.model.x01.ThrowValidator.isValidCheckout;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.inject.Inject;

import bruzsa.laszlo.dartsapp.enties.Player;
import bruzsa.laszlo.dartsapp.model.PlayersEnum;
import bruzsa.laszlo.dartsapp.model.Team;
import bruzsa.laszlo.dartsapp.model.home.AppSettings;
import bruzsa.laszlo.dartsapp.ui.webgui.WebGuiServer;
import bruzsa.laszlo.dartsapp.ui.webgui.WebGuiX01;
import bruzsa.laszlo.dartsapp.ui.x01.X01ThrowAdapter;
import bruzsa.laszlo.dartsapp.ui.x01.input.InputValidator;
import bruzsa.laszlo.dartsapp.util.X01Checkout;
import dagger.hilt.android.lifecycle.HiltViewModel;
import lombok.Getter;

/**
 * @noinspection DataFlowIssue
 */
@HiltViewModel
public class X01ViewModel extends ViewModel {
    public static final int DELETE = -1;

    private static final String TAG = X01ViewModel.class.getName();
    @Getter
    private final MutableLiveData<Team> winnerTeam = new MutableLiveData<>();
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
                TEAM1, new X01ThrowAdapter(service.getThrowList(TEAM1), this::changeThrow),
                TEAM2, new X01ThrowAdapter(service.getThrowList(TEAM2), this::changeThrow)));
        liveDatasMap.get(TEAM1).getScore().setValue(valueOf(settings.getStartScore()));
        liveDatasMap.get(TEAM2).getScore().setValue(valueOf(settings.getStartScore()));

        set.setValue(settings.getMatchType() == X01MatchType.SET);

        activePlayer.observeForever(service::setActive);

        updateWebGui(getStats());
    }

    public void newThrow(int throwValue, Context context) {
        if (service.isCheckout(throwValue)) {
            x01Checkout.getCheckoutDartsCount(throwValue, context, count -> {
                if (Set.of(1, 2, 3).contains(count))
                    newThrow(throwValue, count, winnerTeam::setValue);
                else
                    newThrow(0, 3, winnerTeam::setValue);
            });
        } else {
            newThrow(throwValue, 3, winnerTeam::setValue);
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
            liveDatasMap.get(team).possibleCheckout.setValue(isValidCheckout(stat.getScore()));
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


    public void changeThrow(X01Throw x01Throw, Context context, Consumer<Boolean> callback) {
        showChangeThrowDialog(
                x01Throw,
                context,
                newThrowValue -> {
                    boolean isChanged = changeThrow(x01Throw, context, newThrowValue);
                    callback.accept(isChanged);
                    return isChanged;
                });
    }

    private boolean changeThrow(X01Throw x01Throw, Context context, Integer newThrowValue) {
        boolean isRemoved = service.removeThrow(x01Throw);
        if (isRemoved)
            if (newThrowValue == DELETE) {
                refreshGui();
                return isRemoved;
            }

        if (isRemoved) {
            PlayersEnum current = activePlayer.getValue();
            var player = getPlayerByThrowId(x01Throw.getPlayerId());
            activePlayer.setValue(player);
            newThrow(newThrowValue, context);
            activePlayer.setValue(current);
        }
        return isRemoved;
    }

    @NonNull
    private PlayersEnum getPlayerByThrowId(Long id) {
        return playerMap.entrySet().stream()
                .filter(map -> id.equals(map.getValue().getId()))
                .findFirst()
                .map(Map.Entry::getKey)
                .get();
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
                (int) stat.getAverage(),
                stat.getPlus100(),
                stat.getPlus60(),
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

    private void showChangeThrowDialog(X01Throw x01Throw, Context context, Predicate<Integer> callback) {
        EditText editText = new EditText(context);
        editText.setText(valueOf(x01Throw.getValue()));
        editText.selectAll();
        editText.requestFocus();
        new MaterialAlertDialogBuilder(context)
                .setTitle("Change: " + x01Throw)
                .setView(editText)
                .setNegativeButton("DELETE", (dialog, which) -> {
                    if (callback.test(DELETE)) {
                        new MaterialAlertDialogBuilder(context)
                                .setTitle("Throw was DELETED!")
                                .setPositiveButton("OK", null)
                                .show();
                    }
                })
                .setPositiveButton("CHANGE", (dialog, which) -> {
                    new InputValidator()
                            .getValidThrow(editText.getText().toString())
                            .ifPresentOrElse(
                                    integer -> {
                                        if (callback.test(integer)) {
                                            new MaterialAlertDialogBuilder(context)
                                                    .setTitle("Throw was changed to: " + integer)
                                                    .setPositiveButton("OK", null)
                                                    .show();
                                        }
                                    },
                                    () -> new MaterialAlertDialogBuilder(context)
                                            .setTitle("Invalid score!")
                                            .setMessage("Nothing changed!")
                                            .setPositiveButton("OK", null)
                                            .show());
                })
                .show();
    }

    public void showGameInfo(View view) {
        X01Settings settings = service.getSettings();
        new MaterialAlertDialogBuilder(view.getContext())
                .setTitle("Game info")
                .setMessage(format(US,
                        """
                                %s %d %s
                                Start from %d
                                WinByTwoClearLeg %s
                                WebUI ip address:%n%s""",
                        settings.getFirstToBestOf(), settings.getCount(), settings.getMatchType(),
                        settings.getStartScore(),
                        settings.isWinByTwoClearLeg(),
                        webGuiServer.getHostIpAddress()))
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