package bruzsa.laszlo.dartsapp.model.home;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import bruzsa.laszlo.dartsapp.enties.Player;
import bruzsa.laszlo.dartsapp.model.PlayersEnum;
import bruzsa.laszlo.dartsapp.model.cricket.CricketSettings;
import bruzsa.laszlo.dartsapp.model.x01.X01Settings;
import lombok.Getter;

@Singleton
public class AppSettings {

    @Getter
    private final X01Settings x01Settings;
    @Getter
    private final CricketSettings cricketSettings;
    @Getter
    private final GeneralSettings generalSettings;
    private final Map<PlayersEnum, Player> selectedPlayers = new EnumMap<>(PlayersEnum.class);


    @Inject
    public AppSettings(X01Settings x01Settings, CricketSettings cricketSettings, GeneralSettings generalSettings) {
        this.x01Settings = x01Settings;
        this.cricketSettings = cricketSettings;
        this.generalSettings = generalSettings;
    }

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
