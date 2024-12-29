package bruzsa.laszlo.dartsapp.model;

import static bruzsa.laszlo.dartsapp.model.TeamPlayer.PLAYER_1_1;
import static bruzsa.laszlo.dartsapp.model.TeamPlayer.PLAYER_1_2;
import static bruzsa.laszlo.dartsapp.model.TeamPlayer.PLAYER_2_1;
import static bruzsa.laszlo.dartsapp.model.TeamPlayer.PLAYER_2_2;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Team {

    TEAM1(1),

    TEAM2(2);

    private final Integer number;

    public TeamPlayer player1() {
        return this == TEAM1 ? PLAYER_1_1 : PLAYER_2_1;
    }

    public TeamPlayer player2() {
        return this == TEAM1 ? PLAYER_1_2 : PLAYER_2_2;
    }

    public Team opponent() {
        return this == TEAM1 ? TEAM2 : TEAM1;
    }

}
