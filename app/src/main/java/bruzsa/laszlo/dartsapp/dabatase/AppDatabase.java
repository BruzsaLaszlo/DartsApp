package bruzsa.laszlo.dartsapp.dabatase;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import bruzsa.laszlo.dartsapp.dao.PlayerDao;
import bruzsa.laszlo.dartsapp.data.LocalDateTimeConverter;
import bruzsa.laszlo.dartsapp.enties.Player;

@Database(entities = {Player.class}, version = 2, exportSchema = false)
@TypeConverters(LocalDateTimeConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DB_NAME = "UserDatabase.db";

    public abstract PlayerDao playerDao();

}