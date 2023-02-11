package bruzsa.laszlo.dartsapp.data;

import static java.time.LocalDate.now;

import java.util.List;

import bruzsa.laszlo.dartsapp.dao.Player;

public final class PlayersData {

    private static long id;

    public static final List<Player> players = List.of(
            new Player(id++, " John Doe", "John", now().minusYears(50), null),
            new Player(id++, " Jane Doe", "Jane", now().minusYears(45), null),
            new Player(id++, " Janie Doe", "Janie", now().minusYears(15), null),
            new Player(id++, " Johnny Doe", "Johnny", now().minusYears(12), null),
            new Player(id++, "Bandi", null, null, null),
            new Player(id++, "Beni", null, null, null),
            new Player(id++, "Nóri", null, null, null),
            new Player(id++, "Eszter", null, null, null),
            new Player(id++, "Peti", null, null, null),
            new Player(id++, "Laci", null, null, null),
            new Player(id++, "Lóri", null, null, null),
            new Player(id++, "Barnu", null, null, null),
            new Player(id++, "Kriszti", null, null, null),
            new Player(id++, "Dorka", null, null, null));

    private PlayersData() {
    }
}
