package bruzsa.laszlo.dartsapp.model.cricket;

import androidx.annotation.NonNull;

import bruzsa.laszlo.dartsapp.enties.Player;

public class CricketTeam {

    private final Player player1;
    private Player player2;

    private final boolean teamPlay;

    public CricketTeam(Player... players) {
        player1 = players[0];
        teamPlay = players.length == 2 && players[1] != null;
        if (teamPlay)
            player2 = players[1];
    }

    @NonNull
    @Override
    public String toString() {
        return player1.getName() + (teamPlay ? '\n' + player2.getName() : "");
    }

}
