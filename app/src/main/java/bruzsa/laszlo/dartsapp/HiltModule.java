package bruzsa.laszlo.dartsapp;

import android.content.Context;

import androidx.room.Room;

import javax.inject.Singleton;

import bruzsa.laszlo.dartsapp.dabatase.AppDatabase;
import bruzsa.laszlo.dartsapp.model.home.AppSettings;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class HiltModule {

    @Provides
    @Singleton
    public AppSettings getAppSettings() {
        return new AppSettings();
    }

    @Provides
    @Singleton
    public AppDatabase getAppDatabase(@ApplicationContext Context context) {
        return Room
                .databaseBuilder(context, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }


}
