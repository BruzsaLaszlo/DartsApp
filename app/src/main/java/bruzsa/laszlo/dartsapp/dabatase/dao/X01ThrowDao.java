package bruzsa.laszlo.dartsapp.dabatase.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import bruzsa.laszlo.dartsapp.model.x01.X01Throw;


@Dao
public interface X01ThrowDao {

    @Query("SELECT * FROM x01throw")
    List<X01Throw> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(X01Throw... x01throws);

    @Insert
    long insert(X01Throw x01throw);

    @Update
    void update(X01Throw x01throw);

    @Delete
    void delete(X01Throw x01Throw);

}
