package bruzsa.laszlo.dartsapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import bruzsa.laszlo.dartsapp.enties.Player;

@Dao
public interface PlayerDao {

    @Query("SELECT * FROM player")
    List<Player> getAll();

    @Query("SELECT * FROM player WHERE id IN (:playerIds)")
    List<Player> loadAllByIds(int[] playerIds);

    @Query("SELECT * FROM player WHERE name LIKE :name LIMIT 1")
    Player findByName(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Player... players);

    @Insert
    void insert(Player player);

    @Update
    void update(Player player);

    @Delete
    void delete(Player player);

}
