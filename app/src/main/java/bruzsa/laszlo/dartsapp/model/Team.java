package bruzsa.laszlo.dartsapp.model;

import static bruzsa.laszlo.dartsapp.model.TeamPlayer.PLAYER_1_1;
import static bruzsa.laszlo.dartsapp.model.TeamPlayer.PLAYER_1_2;
import static bruzsa.laszlo.dartsapp.model.TeamPlayer.PLAYER_2_1;
import static bruzsa.laszlo.dartsapp.model.TeamPlayer.PLAYER_2_2;

public enum Team {

    TEAM1,

    TEAM2;

    public TeamPlayer player1() {
        return this == TEAM1 ? PLAYER_1_1 : PLAYER_2_1;
    }

    public TeamPlayer player2() {
        return this == TEAM1 ? PLAYER_1_2 : PLAYER_2_2;
    }

    public Team opponent() {
        return this == TEAM1 ? TEAM2 : TEAM1;
    }

    public boolean isTeam1() {
        return this == TEAM1;
    }
}
