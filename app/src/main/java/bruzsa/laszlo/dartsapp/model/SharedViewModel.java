package bruzsa.laszlo.dartsapp.model;

import static bruzsa.laszlo.dartsapp.model.TeamPlayer.PLAYER_1_1;
import static bruzsa.laszlo.dartsapp.model.TeamPlayer.PLAYER_1_2;
import static bruzsa.laszlo.dartsapp.model.TeamPlayer.PLAYER_2_1;
import static bruzsa.laszlo.dartsapp.model.TeamPlayer.PLAYER_2_2;
import static bruzsa.laszlo.dartsapp.model.home.GameMode.PLAYER;
import static bruzsa.laszlo.dartsapp.model.home.GameType.X01;

import android.util.Log;

import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import bruzsa.laszlo.dartsapp.dao.Player;
import bruzsa.laszlo.dartsapp.model.cricket.CricketSettings;
import bruzsa.laszlo.dartsapp.model.home.GameMode;
import bruzsa.laszlo.dartsapp.model.home.GameType;
import bruzsa.laszlo.dartsapp.model.x01.X01Settings;
import bruzsa.laszlo.dartsapp.repository.home.PlayersRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class SharedViewModel extends ViewModel {


    private SavedStateHandle state;
    public static final String VOICE_INPUT_ENABLED = "isVoiceInputEnabled";

    private final PlayersRepository playersRepository = new PlayersRepository() {
    };

    private final Map<TeamPlayer, Player> selectedPlayers = new EnumMap<>(TeamPlayer.class);
    @Getter
    private final X01Settings x01Settings = X01Settings.getDefault();
    @Getter
    private final CricketSettings cricketSettings = CricketSettings.getDefault();
    @Getter
    private final Settings settings = Settings.getDefault();

    public SharedViewModel(SavedStateHandle state) {
        this();
        this.state = state;
        Log.d("SharedViewModel", "SharedViewModel: " + state.get(VOICE_INPUT_ENABLED));
    }


    public SharedViewModel() {
        selectedPlayers.put(PLAYER_1_1, getAllPlayers().get(0));
        selectedPlayers.put(PLAYER_2_1, getAllPlayers().get(1));
        selectedPlayers.put(PLAYER_1_2, getAllPlayers().get(2));
        selectedPlayers.put(PLAYER_2_2, getAllPlayers().get(3));
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

    public void addPlayer(TeamPlayer teamPlayer, Player player) {
        selectedPlayers.put(teamPlayer, player);
    }

    public List<Player> getAllPlayers() {
        return new ArrayList<>(playersRepository.getAllPlayers());
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
