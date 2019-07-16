package ru.ele638.testtranslate.Mocks;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import ru.ele638.testtranslate.Helpers.Room.UserWordDao;
import ru.ele638.testtranslate.Models.Language;
import ru.ele638.testtranslate.Models.UserWord;


public class MockUserWordDao implements UserWordDao {
    private List<UserWord> userWords;

    MockUserWordDao() {
        userWords = new ArrayList<>();
        userWords.add(new UserWord(
                "testWord1",
                new Language("English", "en"),
                new Language("Russian", "ru")
        ));
        UserWord word = new UserWord(
                "testWord2",
                new Language("English", "en"),
                new Language("French", "fr")
        );
        word.setTranslateWord("testTranslationWord2");
        userWords.add(word);
    }

    @Override
    public Flowable<List<UserWord>> getAllUserWords() {
        return Flowable.just(userWords);
    }

    @Override
    public Flowable<List<UserWord>> getUserWordsWithoutTranslations() {
        List<UserWord> filteredWords = new ArrayList<>();
        for (int i = 0; i < userWords.size(); i++) {
            if (userWords.get(i).getTranslateWord() == null)
                filteredWords.add(userWords.get(i));
        }
        return Flowable.just(filteredWords);
    }

    @Override
    public void insert(UserWord userWord) {
        userWords.add(userWord);
    }

    @Override
    public void update(UserWord userWord) {
        UserWord oldWord = userWords.get(userWords.indexOf(userWord));
        oldWord = userWord;
    }

    @Override
    public void deleteAll() {
        userWords.clear();
    }
}
