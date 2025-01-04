package bruzsa.laszlo.dartsapp.model;

import static bruzsa.laszlo.dartsapp.model.TeamPlayer.PLAYER_1_2;
import static bruzsa.laszlo.dartsapp.model.TeamPlayer.PLAYER_2_1;
import static bruzsa.laszlo.dartsapp.model.TeamPlayer.PLAYER_2_2;
import static bruzsa.laszlo.dartsapp.model.home.GameMode.PLAYER;
import static bruzsa.laszlo.dartsapp.model.home.GameType.X01;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import bruzsa.laszlo.dartsapp.dabatase.AppDatabase;
import bruzsa.laszlo.dartsapp.enties.Player;
import bruzsa.laszlo.dartsapp.model.cricket.CricketSettings;
import bruzsa.laszlo.dartsapp.model.home.GameMode;
import bruzsa.laszlo.dartsapp.model.home.GameType;
import bruzsa.laszlo.dartsapp.model.x01.X01Settings;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class SharedViewModel extends ViewModel {


    private static final String TAG = "SharedViewModel";
    private AppDatabase database;
    public static final String VOICE_INPUT_ENABLED = "isVoiceInputEnabled";

    private final Map<TeamPlayer, Player> selectedPlayers = new EnumMap<>(TeamPlayer.class);
    @Getter
    private final X01Settings x01Settings = X01Settings.getDefault();
    @Getter
    private final CricketSettings cricketSettings = CricketSettings.getDefault();
    @Getter
    private final Settings settings = Settings.getDefault();

    public void startDatabase(Context context) {
        AppDatabase.getInstance(context, appDatabase -> {
            database = appDatabase;
//            inicPlayerList();
        });
    }

    private void inicPlayerList() {
        List<Player> allPlayers = getAllPlayers();
        TeamPlayer[] teamPlayer = TeamPlayer.values();
        for (int i = 0; i < 4 && i <= allPlayers.size(); i++) {
            selectedPlayers.put(teamPlayer[i], allPlayers.get(i));
        }
    }

    public Map<TeamPlayer, Player> getSelectedPlayersMap() {
        switch (settings.gameMode) {
            case SINGLE -> clearPLayers(PLAYER_1_2, PLAYER_2_1, PLAYER_2_2);
            case PLAYER -> clearPLayers(PLAYER_1_2, PLAYER_2_2);
            case TEAM -> {/*nothing*/}
        }
        return selectedPlayers;
    }

    public Player getPlayer(TeamPlayer player) {
        return selectedPlayers.get(player);
    }

    public void selectPlayer(TeamPlayer teamPlayer, Player player) {
        Player foundPlayer = database.playerDao().findByName(player.getName());
        if (foundPlayer == null) {
            database.playerDao().insert(player);
            selectedPlayers.put(teamPlayer, player);
        } else {
            selectedPlayers.put(teamPlayer, foundPlayer);
        }
    }

    public List<Player> getAllPlayers() {
        Log.i(TAG, "getAllPlayers: " + database.playerDao().getAll());
        return database.playerDao().getAll();
    }

    public void removePlayer(Player player) {
        database.playerDao().delete(player);
    }

    public void clearPLayers(TeamPlayer... players) {
        for (TeamPlayer player : players) {
            selectedPlayers.remove(player);
        }
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public static class Settings {

        private GameMode gameMode;
        private GameType gameType;

        public static Settings getDefault() {
            return new Settings(PLAYER, X01);
        }

    }
}
