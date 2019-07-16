package ru.ele638.testtranslate.Views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import javax.inject.Inject;

import ru.ele638.testtranslate.Application.TranslationApplication;
import ru.ele638.testtranslate.Dagger.Components.DaggerFavFragmentComponent;
import ru.ele638.testtranslate.Helpers.Room.AppDatabase;
import ru.ele638.testtranslate.Helpers.SchedulerProvider;
import ru.ele638.testtranslate.Models.UserWord;
import ru.ele638.testtranslate.R;
import ru.ele638.testtranslate.ViewModels.FavViewModel;

public class FavFragment extends Fragment {

    @Inject
    AppDatabase database;
    private FavViewModel mViewModel;
    private RecyclerView mainRV;
    private TextView emptyStateTV;

    static FavFragment newInstance() {
        return new FavFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fav_fragment, container, false);
        mainRV = view.findViewById(R.id.fav_recycler_view);
        emptyStateTV = view.findViewById(R.id.fav_empty_state_text_view);

        Toolbar toolbar = ((AppCompatActivity) getActivity()).findViewById(R.id.toolbar);
        if (toolbar != null){
            toolbar.setTitle(R.string.favorites);
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DaggerFavFragmentComponent.builder()
                .translateAppComponent(TranslationApplication.get(this).getTranslateAppComponent())
                .build()
                .injectFavFragment(this);

        mViewModel = ViewModelProviders.of(this).get(FavViewModel.class);
        mViewModel.setSchedulerProvider(new SchedulerProvider());
        mViewModel.setDatabase(database);
        mViewModel.getAllUserWords();

        mainRV.setLayoutManager(new LinearLayoutManager(getContext()));
        mainRV.setAdapter(mViewModel.mainRVAdapter);

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
    }

    @Override
    public void onStop() {
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

}
