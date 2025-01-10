package bruzsa.laszlo.dartsapp.model.home;

import static bruzsa.laszlo.dartsapp.model.PlayersEnum.PLAYER_1_2;
import static bruzsa.laszlo.dartsapp.model.PlayersEnum.PLAYER_2_1;
import static bruzsa.laszlo.dartsapp.model.PlayersEnum.PLAYER_2_2;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import bruzsa.laszlo.dartsapp.dabatase.AppDatabase;
import bruzsa.laszlo.dartsapp.enties.Player;
import bruzsa.laszlo.dartsapp.model.PlayersEnum;
import dagger.hilt.android.lifecycle.HiltViewModel;
import lombok.Getter;

@HiltViewModel
public class HomeViewModel extends ViewModel {

    private static final String TAG = "SharedViewModel";
    private final AppDatabase database;
    @Getter
    private final AppSettings settings;


    @Inject
    public HomeViewModel(AppDatabase database, AppSettings settings) {
        this.database = database;
        this.settings = settings;
        inicPlayerList();
    }

    private void inicPlayerList() {
        List<Player> allPlayers = getAllPlayers();
        PlayersEnum[] playersEnum = PlayersEnum.values();
        for (int i = 0; i < 2 && i < allPlayers.size(); i++) {
            settings.setSelectedPlayer(playersEnum[i], allPlayers.get(i));
        }
    }

    public Map<PlayersEnum, Player> getSelectedPlayersMap() {
        switch (settings.getGeneralSettings().getGameMode()) {
            case SINGLE -> settings.removePlayersForSelection(PLAYER_1_2, PLAYER_2_1, PLAYER_2_2);
            case PLAYER -> settings.removePlayersForSelection(PLAYER_1_2, PLAYER_2_2);
            case TEAM -> {/*nothing*/}
        }
        return settings.getSelectedPlayers();
    }

    public Player getPlayer(PlayersEnum player) {
        return settings.getSelectedPlayer(player);
    }


    public List<Player> getAllPlayers() {
        Log.d(TAG, "getAllPlayers: " + database.playerDao().getAll());
        return database.playerDao().getAll();
    }


}
