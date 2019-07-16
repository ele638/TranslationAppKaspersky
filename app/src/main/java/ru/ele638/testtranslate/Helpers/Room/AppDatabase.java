package ru.ele638.testtranslate.Helpers.Room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ru.ele638.testtranslate.Models.Language;
import ru.ele638.testtranslate.Models.UserWord;

@Database(entities = {UserWord.class, Language.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserWordDao userWordDao();

    public abstract LanguageDao languageDao();
}
