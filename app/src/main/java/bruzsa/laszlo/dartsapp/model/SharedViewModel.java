package bruzsa.laszlo.dartsapp.model;

import static bruzsa.laszlo.dartsapp.model.TeamPlayer.PLAYER_1_2;
import static bruzsa.laszlo.dartsapp.model.TeamPlayer.PLAYER_2_1;
import static bruzsa.laszlo.dartsapp.model.TeamPlayer.PLAYER_2_2;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import bruzsa.laszlo.dartsapp.AppSettings;
import bruzsa.laszlo.dartsapp.dabatase.AppDatabase;
import bruzsa.laszlo.dartsapp.enties.Player;
import dagger.hilt.android.lifecycle.HiltViewModel;
import lombok.Getter;

@HiltViewModel
public class SharedViewModel extends ViewModel {

    private static final String TAG = "SharedViewModel";
    private final AppDatabase database;
    @Getter
    private final AppSettings settings;


    @Inject
    public SharedViewModel(AppDatabase database, AppSettings settings) {
        this.database = database;
        this.settings = settings;
        inicPlayerList();
    }

    private void inicPlayerList() {
        List<Player> allPlayers = getAllPlayers();
        TeamPlayer[] teamPlayer = TeamPlayer.values();
        for (int i = 0; i < 2 && i <= allPlayers.size(); i++) {
            settings.getSelectedPlayers().put(teamPlayer[i], allPlayers.get(i));
        }
    }

    public Map<TeamPlayer, Player> getSelectedPlayersMap() {
        switch (settings.getGeneralSettings().getGameMode()) {
            case SINGLE -> clearPLayers(PLAYER_1_2, PLAYER_2_1, PLAYER_2_2);
            case PLAYER -> clearPLayers(PLAYER_1_2, PLAYER_2_2);
            case TEAM -> {/*nothing*/}
        }
        return settings.getSelectedPlayers();
    }

    public Player getPlayer(TeamPlayer player) {
        return settings.getSelectedPlayers().get(player);
    }

    public void selectPlayer(TeamPlayer teamPlayer, Player player) {
        Player foundPlayer = database.playerDao().findByName(player.getName());
        if (foundPlayer == null) {
            database.playerDao().insert(player);
            settings.getSelectedPlayers().put(teamPlayer, player);
        } else {
            settings.getSelectedPlayers().put(teamPlayer, foundPlayer);
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
            settings.getSelectedPlayers().remove(player);
        }
    }


}
