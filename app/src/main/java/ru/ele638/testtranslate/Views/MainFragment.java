package ru.ele638.testtranslate.Views;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import javax.inject.Inject;

import ru.ele638.testtranslate.Adapters.LanguageSpinnerAdapter;
import ru.ele638.testtranslate.Application.TranslationApplication;
import ru.ele638.testtranslate.Dagger.Components.DaggerMainFragmentComponent;
import ru.ele638.testtranslate.Dagger.Components.MainFragmentComponent;
import ru.ele638.testtranslate.Helpers.Room.AppDatabase;
import ru.ele638.testtranslate.Helpers.SchedulerProvider;
import ru.ele638.testtranslate.Models.Language;
import ru.ele638.testtranslate.Models.UserWord;
import ru.ele638.testtranslate.Network.YaTranslateApi;
import ru.ele638.testtranslate.R;
import ru.ele638.testtranslate.ViewModels.MainViewModel;

public class MainFragment extends Fragment {

    private final String SRC_LANG_TAG = "SRC_LANG_TAG";
    private final String DST_LANG_TAG = "DST_LANG_TAG";
    private final String SHARED_PREFERENCES = "MYSHAREDPREFERENCES";
    @Inject
    AppDatabase database;
    @Inject
    YaTranslateApi translateApi;
    private MainViewModel mViewModel;
    private RecyclerView mainRV;
    private TextView emptyStateTV;
    private EditText searchET;
    private Button addWordBtn;
    private Spinner srcLangSpinner, destLangSpinner;
    private ImageButton langSwitchBtn;

    static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        mainRV = view.findViewById(R.id.main_recycler_view);
        emptyStateTV = view.findViewById(R.id.main_empty_state_text_view);
        searchET = view.findViewById(R.id.main_search_edit_text);
        addWordBtn = view.findViewById(R.id.main_add_btn);
        srcLangSpinner = view.findViewById(R.id.main_source_lang_spinner);
        destLangSpinner = view.findViewById(R.id.main_dest_lang_spinner);
        langSwitchBtn = view.findViewById(R.id.main_lang_switch);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MainFragmentComponent component = DaggerMainFragmentComponent.builder()
                .translateAppComponent(TranslationApplication.get(this).getTranslateAppComponent())
                .build();
        component.injectMainFragment(this);

        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mViewModel.setSchedulerProvider(new SchedulerProvider());
        mViewModel.setupDatabase(database);
        mViewModel.setupNetwork(translateApi);
        mViewModel.loadDataFromDatabase();

        mainRV.setLayoutManager(new LinearLayoutManager(getContext()));
        mainRV.setAdapter(mViewModel.mainRVAdapter);

        final LanguageSpinnerAdapter spinnerAdapter = new LanguageSpinnerAdapter(getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                mViewModel.languages);

        mViewModel.spinnerAdapter = spinnerAdapter;
        srcLangSpinner.setAdapter(spinnerAdapter);
        destLangSpinner.setAdapter(spinnerAdapter);

        langSwitchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mViewModel.languages.size() > 0) {
                    int tmpPos = srcLangSpinner.getSelectedItemPosition();
                    srcLangSpinner.setSelection(destLangSpinner.getSelectedItemPosition(), true);
                    destLangSpinner.setSelection(tmpPos, true);
                }
            }
        });

//        searchET.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                mViewModel.mainRVAdapter.getFilter().filter(charSequence);
//                setVisibilityState(mViewModel.mainRVAdapter.isEmpty());
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {}
//        });

        searchET.setImeActionLabel("Filter", 1);
        searchET.setPrivateImeOptions("actionUnspecified");
        searchET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_NEXT || i == EditorInfo.IME_NULL) {
                    mViewModel.mainRVAdapter.getFilter().filter(textView.getText());
                    return true;
                }
                return false;
            }
        });

        addWordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.addWord(searchET.getText().toString(),
                        (Language) srcLangSpinner.getSelectedItem(),
                        (Language) destLangSpinner.getSelectedItem());
                searchET.setText("");
                setVisibilityState(mViewModel.mainRVAdapter.isEmpty());
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.userWords.observe(this, new Observer<ArrayList<UserWord>>() {
            @Override
            public void onChanged(ArrayList<UserWord> userWords) {
                setVisibilityState(userWords.size() == 0);
                mainRV.getAdapter().notifyDataSetChanged();
            }
        });
        mViewModel.isLanguagesLoaded.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    mViewModel.spinnerAdapter.notifyDataSetChanged();
                    readSettings();
                }
            }
        });
    }

    @Override
    public void onStop() {
        saveSettings();
        super.onStop();
    }

    private void setVisibilityState(boolean isEmpty) {
        if (isEmpty) {
            mainRV.setVisibility(View.GONE);
            emptyStateTV.setVisibility(View.VISIBLE);
        } else {
            mainRV.setVisibility(View.VISIBLE);
            emptyStateTV.setVisibility(View.GONE);
        }
    }

    private void saveSettings() {
        if (getActivity() != null && srcLangSpinner.getSelectedItem() != null && destLangSpinner.getSelectedItem() != null) {
            SharedPreferences preferences = getActivity().getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
            preferences.edit()
                    .putString(SRC_LANG_TAG, ((Language) srcLangSpinner.getSelectedItem()).getCode())
                    .putString(DST_LANG_TAG, ((Language) destLangSpinner.getSelectedItem()).getCode())
                    .apply();
        }
    }

    private void readSettings() {
        if (getActivity() != null) {
            SharedPreferences preferences = getActivity().getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
            String srcCode = preferences.getString(SRC_LANG_TAG, "");
            String dstCode = preferences.getString(DST_LANG_TAG, "");
            for (int i = 0; i < mViewModel.languages.size(); i++) {
                Language lang = mViewModel.languages.get(i);
                if (lang.getCode().equals(srcCode)) srcLangSpinner.setSelection(i);
                if (lang.getCode().equals(dstCode)) destLangSpinner.setSelection(i);
            }
        }
    }

}
