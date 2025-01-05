package bruzsa.laszlo.dartsapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import bruzsa.laszlo.dartsapp.enties.x01.X01TeamScores;


@Dao
public interface X01ScoresDao {

    @Query("SELECT * FROM team_score")
    List<X01TeamScores> getAll();

    @Query("SELECT * FROM team_score WHERE id IN (:teamScoreIds)")
    List<X01TeamScores> loadAllByIds(int[] teamScoreIds);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(X01TeamScores... players);

    @Insert
    void insert(X01TeamScores player);

    @Update
    void update(X01TeamScores player);

    @Delete
    void delete(X01TeamScores team_score);

}
