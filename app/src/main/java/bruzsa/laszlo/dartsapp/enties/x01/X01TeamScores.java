package bruzsa.laszlo.dartsapp.enties.x01;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bruzsa.laszlo.dartsapp.enties.Player;
import bruzsa.laszlo.dartsapp.model.x01.X01Throw;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity(tableName = "x01-team_score")
public class X01TeamScores {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    private final List<X01Throw> throwsList = new ArrayList<>();
    private int legs;
    private int sets;
    @Embedded
    private Player player1;
    @Embedded
    private Player player2;

    public X01TeamScores(Player... players) {
        player1 = players[0];
        player2 = players.length == 2 ? players[1] : null;
    }

    public void addThrow(X01Throw dartsThrow) {
        throwsList.add(dartsThrow);
    }

    public List<X01Throw> getThrowsList() {
        return Collections.unmodifiableList(throwsList);
    }

    public int wonLeg() {
        return ++legs;
    }

    public void wonSet() {
        legs = 0;
        sets++;
    }

    public void loseSet() {
        legs = 0;
    }
}
