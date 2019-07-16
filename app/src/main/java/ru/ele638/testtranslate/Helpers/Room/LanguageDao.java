package ru.ele638.testtranslate.Helpers.Room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import ru.ele638.testtranslate.Models.Language;

@Dao
public interface LanguageDao {

    @Query("SELECT * FROM language")
    Flowable<List<Language>> getAllLanguages();

    @Insert
    void insertAll(Language... languages);

    @Query("DELETE FROM language")
    void deleteAll();
}
