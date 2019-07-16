package ru.ele638.testtranslate.Views;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import ru.ele638.testtranslate.R;

public class MainActivity extends AppCompatActivity {

    private final String MAINFRAGMENTTAG = "MainTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment mainFragment = fragmentManager.findFragmentByTag(MAINFRAGMENTTAG);

        if (mainFragment == null) {
            transaction.add(R.id.main_frame_layout, MainFragment.newInstance(), MAINFRAGMENTTAG);
        } else {
            transaction.replace(R.id.main_frame_layout, mainFragment, MAINFRAGMENTTAG);
        }

        transaction.commit();
    }
}
