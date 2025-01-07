package bruzsa.laszlo.dartsapp.model.cricket;

import androidx.annotation.NonNull;

import bruzsa.laszlo.dartsapp.enties.Player;
import lombok.Getter;

@Getter
public class CricketTeam {

    private final Player player1;
    private final Player player2;

    private final boolean teamPlay;

    public CricketTeam(Player player1) {
        this.player1 = player1;
        player2 = null;
        teamPlay = false;
    }

    public CricketTeam(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        teamPlay = true;
    }

    @NonNull
    @Override
    public String toString() {
        return player1.getName() + (player2 != null ? '\n' + player2.getName() : "");
    }

}
