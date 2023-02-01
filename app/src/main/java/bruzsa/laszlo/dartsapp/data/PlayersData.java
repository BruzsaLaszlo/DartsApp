package bruzsa.laszlo.dartsapp.data;

import static java.time.LocalDate.now;

import java.util.List;

import bruzsa.laszlo.dartsapp.dao.Player;

public final class PlayersData {

    public static final List<Player> players = List.of(
            new Player(1L, "John Doe", "John", now().minusYears(50), null),
            new Player(2L, "Jane Doe", "Jane", now().minusYears(45), null),
            new Player(3L, "Janie Doe", "Janie", now().minusYears(15), null),
            new Player(4L, "Johnny Doe", "Johnny", now().minusYears(12), null),
            new Player(5L, "Peti", "John", now().minusYears(50), null),
            new Player(6L, "Laci", "John", now().minusYears(50), null),
            new Player(7L, "Barnu", "John", now().minusYears(50), null),
            new Player(8L, "Kriszti", "John", now().minusYears(50), null),
            new Player(9L, "Mama", "John", now().minusYears(50), null),
            new Player(10L, "Dorka", "John", now().minusYears(50), null));

    private PlayersData() {
    }
}
