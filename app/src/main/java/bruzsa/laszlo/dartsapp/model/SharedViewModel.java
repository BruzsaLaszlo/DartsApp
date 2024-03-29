package bruzsa.laszlo.dartsapp.model;

import static bruzsa.laszlo.dartsapp.model.TeamPlayer.PLAYER_1_1;
import static bruzsa.laszlo.dartsapp.model.TeamPlayer.PLAYER_1_2;
import static bruzsa.laszlo.dartsapp.model.TeamPlayer.PLAYER_2_1;
import static bruzsa.laszlo.dartsapp.model.TeamPlayer.PLAYER_2_2;

import android.util.Log;

import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import bruzsa.laszlo.dartsapp.dao.Player;
import bruzsa.laszlo.dartsapp.model.home.GameMode;
import bruzsa.laszlo.dartsapp.model.home.GameType;
import bruzsa.laszlo.dartsapp.model.x01.X01GameSettings;
import bruzsa.laszlo.dartsapp.repository.home.PlayersRepository;
import bruzsa.laszlo.dartsapp.ui.Nano;
import bruzsa.laszlo.dartsapp.ui.x01.input.InputType;
import lombok.Getter;
import lombok.Setter;

public class SharedViewModel extends ViewModel {


    private SavedStateHandle state;
    public static final String VOICE_INPUT_ENABLED = "isVoiceInputEnabled";
    @Getter
    private InputType inputType;

    PlayersRepository playersRepository = new PlayersRepository() {
    };

    private final Map<TeamPlayer, Player> selectedPlayers = new EnumMap<>(TeamPlayer.class);
    @Setter
    private TeamPlayer selectedPlayer;

    @Getter
    private final X01GameSettings settings = X01GameSettings.getDefault();

    @Getter
    @Setter
    private GameType gameType = GameType.NO_GAME;
    @Getter
    @Setter
    private GameMode gameMode = GameMode.PLAYER;
    private static Nano nano = new Nano(9000);

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
        startNano();
    }


    public Collection<Player> getSelectedPlayersCollection() {
        return getSelectedPlayersMap().values();
    }

    public Map<TeamPlayer, Player> getSelectedPlayersMap() {
        if (gameMode == GameMode.SINGLE) removePLayers(PLAYER_1_2, PLAYER_2_1, PLAYER_2_2);
        else if (gameMode == GameMode.PLAYER) removePLayers(PLAYER_1_2, PLAYER_2_2);

        return Collections.unmodifiableMap(selectedPlayers);
    }

    public Player getPlayer(TeamPlayer player) {
        return selectedPlayers.get(player);
    }

    public void addPlayer(Player player) {
        selectedPlayers.put(selectedPlayer, player);
    }

    public void removePlayerEnum(TeamPlayer teamPlayer) {
        this.selectedPlayers.remove(teamPlayer);
    }


    public List<Player> getAllPlayers() {
        return new ArrayList<>(playersRepository.getAllPlayers());
    }

    private void removePLayers(TeamPlayer... players) {
        for (TeamPlayer player : players) {
            this.selectedPlayers.remove(player);
        }
    }

    public void startNano() {
        try {
            if (!nano.wasStarted())
                nano.start();
        } catch (IOException e) {
            throw new IllegalStateException("Nano webserver can not start!", e);
        }
    }

    public void setWebServerContent(String html) {
        nano.setResponse(html);
    }

    public void stopNano() {
        nano.stop();
    }

    @Override
    protected void onCleared() {
        stopNano();
    }
}
