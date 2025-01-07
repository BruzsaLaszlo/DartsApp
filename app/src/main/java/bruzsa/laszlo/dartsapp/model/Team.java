package bruzsa.laszlo.dartsapp.model;

import static bruzsa.laszlo.dartsapp.model.PlayersEnum.PLAYER_1_1;
import static bruzsa.laszlo.dartsapp.model.PlayersEnum.PLAYER_1_2;
import static bruzsa.laszlo.dartsapp.model.PlayersEnum.PLAYER_2_1;
import static bruzsa.laszlo.dartsapp.model.PlayersEnum.PLAYER_2_2;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Team {

    TEAM1(1),

    TEAM2(2);

    private final Integer number;

    public PlayersEnum player1() {
        return this == TEAM1 ? PLAYER_1_1 : PLAYER_2_1;
    }

    public PlayersEnum player2() {
        return this == TEAM1 ? PLAYER_1_2 : PLAYER_2_2;
    }

    public Team opponent() {
        return this == TEAM1 ? TEAM2 : TEAM1;
    }

}
