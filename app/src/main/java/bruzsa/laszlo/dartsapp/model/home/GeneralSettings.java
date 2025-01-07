package bruzsa.laszlo.dartsapp.model.home;

import static bruzsa.laszlo.dartsapp.model.home.GameMode.PLAYER;
import static bruzsa.laszlo.dartsapp.model.home.GameType.X01;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GeneralSettings {

    private GameMode gameMode = PLAYER;
    private GameType gameType = X01;


}
