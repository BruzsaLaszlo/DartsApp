package bruzsa.laszlo.dartsapp.dabatase;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import bruzsa.laszlo.dartsapp.dabatase.dao.AppSettingsDao;
import bruzsa.laszlo.dartsapp.dabatase.dao.PlayerDao;
import bruzsa.laszlo.dartsapp.dabatase.dao.X01ThrowDao;
import bruzsa.laszlo.dartsapp.data.LocalDateTimeConverter;
import bruzsa.laszlo.dartsapp.data.MapConverter;
import bruzsa.laszlo.dartsapp.enties.Player;
import bruzsa.laszlo.dartsapp.model.x01.X01Throw;

@Database(entities = {Player.class, X01Throw.class}, version = 5, exportSchema = false)
@TypeConverters({LocalDateTimeConverter.class, MapConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public static final String DB_NAME = "UserDatabase.db";

    public abstract PlayerDao playerDao();

    public abstract AppSettingsDao appSettingsDao();

    //    public abstract X01ScoresDao x01ScoresDao();
    public abstract X01ThrowDao x01ThrowDao();
}