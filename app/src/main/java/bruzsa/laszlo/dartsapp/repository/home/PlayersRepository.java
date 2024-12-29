package bruzsa.laszlo.dartsapp.repository.home;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import bruzsa.laszlo.dartsapp.dao.Player;
import bruzsa.laszlo.dartsapp.data.PlayersData;

public interface PlayersRepository {

    default List<Player> getAllPlayers() {
        return PlayersData.players.stream()
                .sorted(Comparator.comparing(Player::getName))
                .collect(Collectors.toList());
    }

}
