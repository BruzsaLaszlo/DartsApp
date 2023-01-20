package bruzsa.laszlo.dartsapp.repository.home;

import java.util.List;

import bruzsa.laszlo.dartsapp.dao.Player;
import bruzsa.laszlo.dartsapp.data.PlayersData;

public interface PlayersRepository {

    default List<Player> getAllPlayers() {
        return PlayersData.players;
    }

}
