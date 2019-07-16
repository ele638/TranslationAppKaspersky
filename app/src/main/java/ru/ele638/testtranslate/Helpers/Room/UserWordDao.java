package ru.ele638.testtranslate.Helpers.Room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import ru.ele638.testtranslate.Models.UserWord;

@Dao
public interface UserWordDao {

    @Query("SELECT * FROM userword")
    Flowable<List<UserWord>> getAllUserWords();

    @Query("SELECT * FROM userword WHERE translateWord is null")
    Flowable<List<UserWord>> getUserWordsWithoutTranslations();

    @Insert
    void insert(UserWord userWord);

    @Update
    void update(UserWord userWord);

    @Query("DELETE FROM userword")
    void deleteAll();
}
