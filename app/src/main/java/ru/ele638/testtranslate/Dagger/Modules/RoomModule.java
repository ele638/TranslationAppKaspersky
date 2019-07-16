package ru.ele638.testtranslate.Dagger.Modules;

import android.app.Application;

import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.ele638.testtranslate.Helpers.Room.AppDatabase;
import ru.ele638.testtranslate.Helpers.Room.LanguageDao;
import ru.ele638.testtranslate.Helpers.Room.UserWordDao;

@Module
public class RoomModule {

    private AppDatabase appDatabase;

    public RoomModule(Application mApplication) {
        appDatabase = Room.databaseBuilder(mApplication, AppDatabase.class, "translation_base.db")
                .build();
    }

    @Provides
    @Singleton
    AppDatabase providesRoomDatabase() {
        return appDatabase;
    }

    @Singleton
    @Provides
    UserWordDao providesUserWordDao(AppDatabase database) {
        return database.userWordDao();
    }

    @Singleton
    @Provides
    LanguageDao languageDao(AppDatabase database) {
        return database.languageDao();
    }
}
