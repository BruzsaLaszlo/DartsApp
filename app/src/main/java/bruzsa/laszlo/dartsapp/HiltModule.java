package bruzsa.laszlo.dartsapp;

import android.content.Context;

import androidx.room.Room;

import javax.inject.Singleton;

import bruzsa.laszlo.dartsapp.dabatase.AppDatabase;
import bruzsa.laszlo.dartsapp.model.cricket.CricketSettings;
import bruzsa.laszlo.dartsapp.model.x01.X01Settings;
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
    public X01Settings getX01Settings() {
        return new X01Settings();
    }

    @Provides
    @Singleton
    public CricketSettings getCricketSettings() {
        return new CricketSettings();
    }

    @Provides
    @Singleton
    public GeneralSettings getSettings() {
        return new GeneralSettings();
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
