package bruzsa.laszlo.dartsapp.ui.home.player;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import bruzsa.laszlo.dartsapp.dabatase.AppDatabase;
import bruzsa.laszlo.dartsapp.enties.Player;
import bruzsa.laszlo.dartsapp.model.PlayersEnum;
import bruzsa.laszlo.dartsapp.model.home.AppSettings;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class PlayerViewModel extends ViewModel {

    private final AppSettings settings;
    private final AppDatabase database;

    @Inject
    public PlayerViewModel(AppSettings settings, AppDatabase database) {
        this.settings = settings;
        this.database = database;
    }

    public List<Player> getPlayers() {
        Map<PlayersEnum, Player> selectedPlayers = settings.getSelectedPlayers();
        return database.playerDao().getAll()
                .stream()
                .filter(not(selectedPlayers::containsValue))
                .collect(toList());
    }

    public void removePlayer(Player player) {
        Log.d("PlayerViewModel", "removePlayer: " + player);
        database.playerDao().delete(player);
    }

    public void selectPlayer(PlayersEnum playersEnum, Player player) {
        Player foundPlayer = database.playerDao().findByName(player.getName());
        if (foundPlayer == null) {
            long id = database.playerDao().insert(player);
            player.setId(id);
            settings.setSelectedPlayer(playersEnum, player);
        } else {
            settings.setSelectedPlayer(playersEnum, foundPlayer);
        }
    }

}
