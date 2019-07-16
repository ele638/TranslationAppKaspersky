package ru.ele638.testtranslate.Views;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ru.ele638.testtranslate.R;

public class MainActivity extends AppCompatActivity {

    private final String MAINFRAGMENTTAG = "MainTag";
    private final String FAVFRAGMENTTAG = "FavFragmentTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_frame_layout, MainFragment.newInstance(), MAINFRAGMENTTAG)
                    .commit();
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (menuItem.getItemId()){
                    case R.id.navigation_translator : {
                        Fragment mainFragment = fragmentManager.findFragmentByTag(MAINFRAGMENTTAG);
                        transaction.replace(R.id.main_frame_layout, mainFragment == null ? MainFragment.newInstance() : mainFragment, MAINFRAGMENTTAG);
                        transaction.commit();
                        return true;
                    }

                    case R.id.navigation_favorites : {
                        Fragment favFragment = fragmentManager.findFragmentByTag(FAVFRAGMENTTAG);
                        transaction.replace(R.id.main_frame_layout, favFragment == null ? FavFragment.newInstance() : favFragment, FAVFRAGMENTTAG);
                        transaction.commit();
                        return true;
                    }

                    default: return false;
                }
            }
        });
    }
}
