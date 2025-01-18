package bruzsa.laszlo.dartsapp.model.home;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import bruzsa.laszlo.dartsapp.enties.Player;
import bruzsa.laszlo.dartsapp.model.PlayersEnum;
import bruzsa.laszlo.dartsapp.model.cricket.CricketSettings;
import bruzsa.laszlo.dartsapp.model.x01.X01Settings;
import lombok.Getter;

public class AppSettings {

    @Getter
    private final X01Settings x01Settings = new X01Settings();
    @Getter
    private final CricketSettings cricketSettings = new CricketSettings();
    @Getter
    private final GeneralSettings generalSettings = new GeneralSettings();
    private final Map<PlayersEnum, Player> selectedPlayers = new EnumMap<>(PlayersEnum.class);

    public Map<PlayersEnum, Player> getSelectedPlayers() {
        return Collections.unmodifiableMap(selectedPlayers);
    }

    public Player getSelectedPlayer(PlayersEnum player) {
        return selectedPlayers.get(player);
    }

    public void setSelectedPlayer(PlayersEnum playersEnum, Player player) {
        selectedPlayers.put(playersEnum, player);
    }

    public void removePlayersForSelection(PlayersEnum... players) {
        for (PlayersEnum player : players) {
            selectedPlayers.remove(player);
        }
    }

}
