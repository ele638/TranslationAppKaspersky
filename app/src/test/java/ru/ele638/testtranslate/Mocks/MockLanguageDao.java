package ru.ele638.testtranslate.Mocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Flowable;
import ru.ele638.testtranslate.Helpers.Room.LanguageDao;
import ru.ele638.testtranslate.Models.Language;


public class MockLanguageDao implements LanguageDao {
    private List<Language> languages;

    MockLanguageDao() {
        languages = new ArrayList<>();
        languages.add(new Language("English", "en"));
        languages.add(new Language("Russian", "ru"));
    }


    @Override
    public Flowable<List<Language>> getAllLanguages() {
        return Flowable.just(languages);
    }

    @Override
    public void insertAll(Language... inLanguages) {
        languages.addAll(Arrays.asList(inLanguages));
    }

    @Override
    public void deleteAll() {
        languages.clear();
    }
}
