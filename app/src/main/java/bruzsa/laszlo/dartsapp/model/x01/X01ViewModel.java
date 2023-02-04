package bruzsa.laszlo.dartsapp.model.x01;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.RED;
import static java.lang.String.valueOf;
import static bruzsa.laszlo.dartsapp.model.Team.TEAM1;
import static bruzsa.laszlo.dartsapp.model.Team.TEAM2;

import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import bruzsa.laszlo.dartsapp.dao.Player;
import bruzsa.laszlo.dartsapp.model.Team;
import bruzsa.laszlo.dartsapp.model.TeamPlayer;
import bruzsa.laszlo.dartsapp.ui.x01.X01ThrowAdapter;
import lombok.Getter;

@SuppressWarnings("ConstantConditions")
public class X01ViewModel extends ViewModel {

    @Getter
    private final Map<Team, LiveDatas> liveDatasMap = Map.of(
            TEAM1, new LiveDatas(),
            TEAM2, new LiveDatas()
    );

    private Map<Team, X01ThrowAdapter> throwAdapterMap;

    private boolean gameStarted;
    @Getter
    private final MutableLiveData<Boolean> set = new MutableLiveData<>();
    @Getter
    private final MutableLiveData<TeamPlayer> activePlayer = new MutableLiveData<>();
    @Getter
    private final MutableLiveData<Boolean> teamPlay = new MutableLiveData<>();

    private X01Service service;


    public void startGameOrContinue(Map<TeamPlayer, Player> activePlayersMap, X01GameSettings settings) {
        if (!gameStarted) {
            firstInic(activePlayersMap, settings);
            gameStarted = true;
        }
        set.setValue(settings.getX01MatchType() == X01MatchType.SETS);
    }

    private void firstInic(Map<TeamPlayer, Player> activePlayersMap, X01GameSettings settings) {
        service = new X01Service(activePlayersMap, settings);

        activePlayer.setValue(service.getActive());

        teamPlay.setValue(activePlayersMap.size() == 4);

        set.setValue(settings.getX01MatchType() == X01MatchType.SETS);

        throwAdapterMap = new EnumMap<>(Map.of(
                TEAM1, new X01ThrowAdapter(service.getThrowList(TEAM1), this::removeThrow, TEAM1),
                TEAM2, new X01ThrowAdapter(service.getThrowList(TEAM2), this::removeThrow, TEAM2)
        ));
        liveDatasMap.get(TEAM1).getScore().setValue(valueOf(settings.getStartScore()));
        liveDatasMap.get(TEAM2).getScore().setValue(valueOf(settings.getStartScore()));
    }

    public void newThrow(int throwValue, Runnable onGameOverEvent) {
        service.newThrow(throwValue, (gameOver, player) -> {
            throwAdapterMap.get(player.team).inserted();
            updateGUI(player);
            if (Boolean.TRUE.equals(gameOver)) onGameOverEvent.run();
        });
        Log.d("X01ViewModel", "newThrow: " + liveDatasMap.get(TEAM1).getScore().getValue() + " " + liveDatasMap.get(TEAM2).getScore().getValue());
    }

    public boolean kiir(View view) {
        Log.d("X01ViewModel", "kiir: " + ((Button) view).getText());
        return true;
    }

    public String fasom() {
        return "sdfsd";
    }

    private void updateGUI(TeamPlayer player) {
        activePlayer.setValue(player.nextPlayer(teamPlay.getValue()));
        updateGUI(player.team);
    }

    private void updateGUI(Team team) {
        liveDatasMap.get(team).set.setValue(service.getSet(team));
        liveDatasMap.get(team).leg.setValue(service.getLeg(team));
        liveDatasMap.get(team).stat.setValue(getStatAsString(team));
        liveDatasMap.get(team).score.setValue(valueOf(service.getScore(team)));
    }

    public void setActive(TeamPlayer player) {
        service.setActive(player);
        activePlayer.setValue(player);
    }


    public boolean removeThrow(X01Throw x01Throw, Team team) {
        return service.removeThrow(x01Throw, () -> updateGUI(team));
    }

    public boolean isOut(Team team) {
        int score = service.getScore(team);
        return (score > 1 && score < 159) || List.of(160, 161, 164, 167, 170).contains(score);
    }

    private String getStatAsString(Team team) {
        X01SummaryStatistics stat = service.getStat(team);
        if (stat.getDartCount() == 0) return "";
        return String.format(Locale.ENGLISH, """
                        %d
                        %d
                        %d
                        %d
                        %d
                        %s""",
                stat.getHundredPlus(),
                stat.getSixtyPlus(),
                (int) stat.getAverage(),
                stat.getMax(),
                stat.getMin(),
                stat.getHighestCheckout().map(Object::toString).orElse("-"));
    }

    public X01ThrowAdapter getThrowsAdapterTeam(Team team) {
        return throwAdapterMap.get(team);
    }

//    @BindingAdapter(value = {"active", "player"}, requireAll = false)
//    public static void setColorAndFaceTypeForNameButtons(View view, MutableLiveData<TeamPlayer> active, TeamPlayer player) {
//        if (player == null) {
//            Log.d("X01ViewModel", "setColorAndFaceTypeForNameButtons: player null");
//        }
//        if (active == null)
//            Log.d("X01ViewModel", "setColorAndFaceTypeForNameButtons: active null");
//        if (player != null && active != null)
//            Log.d("X01ViewModel", "setColorAndFaceTypeForNameButtons: " + player.name() + " " + active.getValue().name());
//
//        Button button;
//        if (view instanceof Button b) button = b;
//        else return;
//
//        if (active.getValue() == player) {
//            button.setTextColor(RED);
//            button.setTypeface(Typeface.DEFAULT_BOLD);
//        } else {
//            button.setTextColor(BLACK);
//            button.setTypeface(Typeface.DEFAULT);
//        }
//    }

    @BindingAdapter("active")
    public static void setColorAndFaceTypeForNameButtons(View view, MutableLiveData<TeamPlayer> active) {
        Button button;
        if (view instanceof Button b) button = b;
        else return;
        TeamPlayer actual = (TeamPlayer) view.getTag();
        Log.d("X01ViewModel", "setColorAndFaceTypeForNameButtons: " + actual + " " + active.getValue());

        if (active.getValue() == actual) {
            button.setTextColor(RED);
            button.setTypeface(Typeface.DEFAULT_BOLD);
        } else {
            button.setTextColor(BLACK);
            button.setTypeface(Typeface.DEFAULT);
        }
    }

    public int getThrowsAdapterCount(Team team) {
        return throwAdapterMap.get(team).getItemCount();
    }


    public static class LiveDatas {

        @Getter
        private final MutableLiveData<String> set = new MutableLiveData<>("0");

        @Getter
        private final MutableLiveData<String> leg = new MutableLiveData<>("0");

        @Getter
        private final MutableLiveData<String> stat = new MutableLiveData<>("");

        @Getter
        private final MutableLiveData<String> score = new MutableLiveData<>();

    }

}