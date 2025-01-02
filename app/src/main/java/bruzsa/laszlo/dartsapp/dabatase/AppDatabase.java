package bruzsa.laszlo.dartsapp.dabatase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import bruzsa.laszlo.dartsapp.dao.PlayerDao;
import bruzsa.laszlo.dartsapp.data.LocalDateTimeConverter;
import bruzsa.laszlo.dartsapp.enties.Player;

@Database(entities = {Player.class}, version = 2)
@TypeConverters(LocalDateTimeConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    public abstract PlayerDao playerDao();

    private static final String DB_NAME = "UserDatabase.db";
    private static volatile AppDatabase appDatabase;

    public static synchronized AppDatabase getInstance(Context context) {
        if (appDatabase == null) appDatabase = create(context);
        return appDatabase;
    }

    private static AppDatabase create(final Context context) {
        return Room
                .databaseBuilder(context, AppDatabase.class, DB_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }
}