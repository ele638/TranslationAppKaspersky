package ru.ele638.testtranslate.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import ru.ele638.testtranslate.Adapters.MainWordsRVAdapter;
import ru.ele638.testtranslate.Helpers.BaseSchedulerProvider;
import ru.ele638.testtranslate.Helpers.OnWordInterractionListener;
import ru.ele638.testtranslate.Helpers.Room.AppDatabase;
import ru.ele638.testtranslate.Models.UserWord;

public class FavViewModel extends ViewModel {

    public MainWordsRVAdapter mainRVAdapter = new MainWordsRVAdapter();
    public MutableLiveData<ArrayList<UserWord>> userWords = new MutableLiveData<>();
    private AppDatabase database;
    private BaseSchedulerProvider schedulerProvider;

    public FavViewModel() {
        mainRVAdapter.setListener(new OnWordInterractionListener() {
            @Override
            public void updateWord(final UserWord word) {
                schedulerProvider.io().scheduleDirect(new Runnable() {
                    @Override
                    public void run() {
                        database.userWordDao().update(word);
                    }
                });
            }

            @Override
            public void deleteWord(final UserWord word) {
                schedulerProvider.io().scheduleDirect(new Runnable() {
                    @Override
                    public void run() {
                        database.userWordDao().delete(word);
                    }
                });
            }
        });
        mainRVAdapter.setDeletionMode(true);
    }

    public void getAllUserWords() {
        Disposable disposable = database.userWordDao().getFaoriteUserWord()
                .observeOn(schedulerProvider.ui())
                .subscribe(new Consumer<List<UserWord>>() {
                    @Override
                    public void accept(List<UserWord> userWordList) throws Exception {
                        mainRVAdapter.setData((ArrayList<UserWord>) userWordList);
                        userWords.postValue((ArrayList<UserWord>) userWordList);
                    }
                });
    }

    public void setDatabase(AppDatabase database) {
        this.database = database;
    }

    public void setSchedulerProvider(BaseSchedulerProvider schedulerProvider) {
        this.schedulerProvider = schedulerProvider;
    }
}
