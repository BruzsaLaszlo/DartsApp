package bruzsa.laszlo.dartsapp;

import android.util.Log;

import java.util.EnumMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import bruzsa.laszlo.dartsapp.enties.Player;
import bruzsa.laszlo.dartsapp.model.TeamPlayer;
import bruzsa.laszlo.dartsapp.model.cricket.CricketSettings;
import bruzsa.laszlo.dartsapp.model.x01.X01Settings;
import lombok.Getter;

@Getter
@Singleton
public class AppSettings {

    private final X01Settings x01Settings;
    private final CricketSettings cricketSettings;
    private final GeneralSettings generalSettings;
    private final Map<TeamPlayer, Player> selectedPlayers = new EnumMap<>(TeamPlayer.class);

    private static int n;

    @Inject
    public AppSettings(X01Settings x01Settings, CricketSettings cricketSettings, GeneralSettings generalSettings) {
        this.x01Settings = x01Settings;
        this.cricketSettings = cricketSettings;
        this.generalSettings = generalSettings;
        Log.e("error", "AppSettings: " + ++n);
    }

}
