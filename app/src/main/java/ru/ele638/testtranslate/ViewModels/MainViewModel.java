package ru.ele638.testtranslate.ViewModels;

import android.os.Build;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.ele638.testtranslate.Adapters.LanguageSpinnerAdapter;
import ru.ele638.testtranslate.Adapters.MainWordsRVAdapter;
import ru.ele638.testtranslate.BuildConfig;
import ru.ele638.testtranslate.Helpers.BaseSchedulerProvider;
import ru.ele638.testtranslate.Helpers.Room.AppDatabase;
import ru.ele638.testtranslate.Models.Language;
import ru.ele638.testtranslate.Models.UserWord;
import ru.ele638.testtranslate.Network.LanguageList;
import ru.ele638.testtranslate.Network.TranslateResponse;
import ru.ele638.testtranslate.Network.YaTranslateApi;

public class MainViewModel extends ViewModel {

    public MainWordsRVAdapter mainRVAdapter = new MainWordsRVAdapter();
    public LanguageSpinnerAdapter spinnerAdapter;
    public MutableLiveData<ArrayList<UserWord>> userWords = new MutableLiveData<>();
    public ArrayList<Language> languages = new ArrayList<>();
    public MutableLiveData<Boolean> isLanguagesLoaded = new MutableLiveData<>();

    private AppDatabase database;
    private YaTranslateApi translateApi;
    private BaseSchedulerProvider schedulerProvider;

    public void setSchedulerProvider(BaseSchedulerProvider provider) {
        this.schedulerProvider = provider;
    }

    public void setupDatabase(AppDatabase database) {
        this.database = database;
    }

    public void setupNetwork(YaTranslateApi translateApi) {
        this.translateApi = translateApi;
    }

    public void addWord(String word, Language srcLang, Language dstLang) {
        if (!word.isEmpty()) {
            final UserWord userWord = new UserWord(word, srcLang, dstLang);
            if (!userWords.getValue().contains(userWord)) {
                Completable.fromAction(new Action() {
                    @Override
                    public void run() throws Exception {
                        database.userWordDao().insert(userWord);
                    }
                }).observeOn(schedulerProvider.ui())
                        .subscribeOn(schedulerProvider.io())
                        .subscribe();
            }
        }
    }

    public void loadDataFromDatabase() {
        isLanguagesLoaded.postValue(false);
        getAllUserWords();
        getAllLanguages();
        getWordsWithoutTranslations();
    }

    public void getWordsWithoutTranslations() {
        Disposable disposable = database.userWordDao().getUserWordsWithoutTranslations()
                .observeOn(schedulerProvider.io())
                .subscribeOn(schedulerProvider.io())
                .subscribe(new Consumer<List<UserWord>>() {
                    @Override
                    public void accept(List<UserWord> userWords) throws Exception {
                        for (int i = 0; i < userWords.size(); i++) {
                            final UserWord word = userWords.get(i);
                            loadTranslationFromServer(word);
                        }
                    }
                });
    }

    private void loadTranslationFromServer(final UserWord word) {
        translateApi.getTranslate(BuildConfig.APIKEY,
                word.getText(),
                String.format("%s-%s", word.getSrcLang().getCode(), word.getDstLang().getCode()))
                .enqueue(new Callback<TranslateResponse>() {
                    @Override
                    public void onResponse(Call<TranslateResponse> call, Response<TranslateResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            word.setTranslateWord(response.body().getText().get(0));
                            schedulerProvider.io().scheduleDirect(new Runnable() {
                                @Override
                                public void run() {
                                    database.userWordDao().update(word);
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<TranslateResponse> call, Throwable t) {

                    }
                });
    }

    public void getAllLanguages() {
        Disposable disposable = database.languageDao().getAllLanguages()
                .observeOn(schedulerProvider.ui())
                .subscribe(new Consumer<List<Language>>() {
                    @Override
                    public void accept(List<Language> languageList) throws Exception {
                        if (languageList.size() == 0) {
                            loadLanguagesFromNetwork();
                        } else {
                            updateLanguages(languageList);
                        }
                    }
                });
    }

    public void getAllUserWords() {
        Disposable disposable = database.userWordDao().getAllUserWords()
                .observeOn(schedulerProvider.ui())
                .subscribe(new Consumer<List<UserWord>>() {
                    @Override
                    public void accept(List<UserWord> userWordList) throws Exception {
                        mainRVAdapter.setData((ArrayList<UserWord>) userWordList);
                        userWords.postValue((ArrayList<UserWord>) userWordList);
                    }
                });
    }

    public void updateLanguages(List<Language> languageList) {
        if (languageList.size() > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                languageList.sort(new Comparator<Language>() {
                    @Override
                    public int compare(Language language, Language t1) {
                        return language.getName().compareTo(t1.getName());
                    }
                });
            }
            languages.clear();
            languages.addAll(languageList);
            isLanguagesLoaded.postValue(true);
        }
    }


    public void loadLanguagesFromNetwork() {
        translateApi.getLanguages(BuildConfig.APIKEY).enqueue(new Callback<LanguageList>() {
            @Override
            public void onResponse(Call<LanguageList> call, final Response<LanguageList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    schedulerProvider.io().scheduleDirect(new Runnable() {
                        @Override
                        public void run() {
                            database.languageDao().insertAll(response.body().getList().toArray(new Language[response.body().getList().size()]));
                        }
                    });

                    updateLanguages(response.body().getList());
                }
            }

            @Override
            public void onFailure(Call<LanguageList> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
