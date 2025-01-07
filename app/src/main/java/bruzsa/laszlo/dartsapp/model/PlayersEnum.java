package bruzsa.laszlo.dartsapp.model;

import static bruzsa.laszlo.dartsapp.model.Team.TEAM1;
import static bruzsa.laszlo.dartsapp.model.Team.TEAM2;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PlayersEnum {

    PLAYER_1_1(TEAM1, 0),
    PLAYER_2_1(TEAM2, 1),
    PLAYER_1_2(TEAM1, 2),
    PLAYER_2_2(TEAM2, 3);

    public final Team team;
    private final int throwingOrder;

    public PlayersEnum nextPlayer(boolean isTeamPlay) {
        return PlayersEnum.values()[(this.throwingOrder + 1) % (isTeamPlay ? 4 : 2)];
    }

}
