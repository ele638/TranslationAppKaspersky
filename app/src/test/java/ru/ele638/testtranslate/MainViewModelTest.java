package ru.ele638.testtranslate;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ru.ele638.testtranslate.Adapters.LanguageSpinnerAdapter;
import ru.ele638.testtranslate.Helpers.TrampolineSchedulerProvider;
import ru.ele638.testtranslate.Mocks.MockDatabase;
import ru.ele638.testtranslate.Mocks.MockYaTranslationApi;
import ru.ele638.testtranslate.Models.Language;
import ru.ele638.testtranslate.Network.YaTranslateApi;
import ru.ele638.testtranslate.ViewModels.MainViewModel;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MainViewModelTest {
    @Rule
    public RxImmediateSchedulerRule timeoutRule = new RxImmediateSchedulerRule();
    @Rule
    public InstantTaskExecutorRule taskExecutorRule = new InstantTaskExecutorRule();
    @Mock
    Context context;
    private MockDatabase database;
    private YaTranslateApi translateApi;
    private MainViewModel viewModel = new MainViewModel();
    private LanguageSpinnerAdapter spinnerAdapter = new LanguageSpinnerAdapter(context,
            android.R.layout.simple_spinner_dropdown_item,
            viewModel.languages);


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        database = new MockDatabase();
        translateApi = new MockYaTranslationApi();

        viewModel.setupDatabase(database);
        viewModel.setupNetwork(translateApi);
        viewModel.spinnerAdapter = spinnerAdapter;
        viewModel.setSchedulerProvider(new TrampolineSchedulerProvider());
    }

    @Test

    public void testAInitState() {
        assert viewModel.languages.size() == 0;
        assert viewModel.userWords.getValue() == null;
    }

    @Test
    public void testBLoadFromDB() {
        viewModel.getAllLanguages();
        assert viewModel.languages.size() == 2;
        viewModel.getAllUserWords();
        assert viewModel.userWords.getValue().size() == 2;
    }

    @Test
    public void testCAddWord() {
        testBLoadFromDB();
        viewModel.addWord("testWord3", new Language("Spanish", "sp"), new Language("English", "en"));
        assert viewModel.userWords.getValue().size() == 3;
        assert viewModel.languages.size() == 2;
    }

    @Test
    public void testDReloadLangs() {
        database.languageDao.deleteAll();
        viewModel.getAllLanguages();
        assert viewModel.languages.size() == 3;
    }

    @Test
    public void testELoadData() {
        testBLoadFromDB();
        int count = 0;
        for (int i = 0; i < viewModel.userWords.getValue().size(); i++) {
            if (viewModel.userWords.getValue().get(i).getTranslateWord() != null)
                count++;
        }
        assert count == 1;
        viewModel.loadDataFromDatabase();
        for (int i = 0; i < viewModel.userWords.getValue().size(); i++) {
            assert viewModel.userWords.getValue().get(i).getTranslation() != null;
        }
    }


}
