package ru.ele638.testtranslate.Mocks;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import ru.ele638.testtranslate.Helpers.Room.AppDatabase;
import ru.ele638.testtranslate.Helpers.Room.LanguageDao;
import ru.ele638.testtranslate.Helpers.Room.UserWordDao;

public class MockDatabase extends AppDatabase {
    public LanguageDao languageDao = new MockLanguageDao();
    private UserWordDao userWordDao = new MockUserWordDao();

    @Override
    public UserWordDao userWordDao() {
        return userWordDao;
    }

    @Override
    public LanguageDao languageDao() {
        return languageDao;
    }

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {
        userWordDao.deleteAll();
    }
}
