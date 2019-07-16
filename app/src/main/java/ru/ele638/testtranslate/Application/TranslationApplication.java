package ru.ele638.testtranslate.Application;

import android.app.Application;

import androidx.fragment.app.Fragment;

import ru.ele638.testtranslate.Dagger.Components.DaggerTranslateAppComponent;
import ru.ele638.testtranslate.Dagger.Components.TranslateAppComponent;
import ru.ele638.testtranslate.Dagger.Modules.ContextModule;
import ru.ele638.testtranslate.Dagger.Modules.RoomModule;
import ru.ele638.testtranslate.Dagger.Modules.TranslationAppModule;
import timber.log.Timber;

public class TranslationApplication extends Application {

    private TranslateAppComponent translateAppComponent;


    public static TranslationApplication get(Fragment fragment) {
        return (TranslationApplication) fragment.getActivity().getApplication();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());

        translateAppComponent = null;

        translateAppComponent = DaggerTranslateAppComponent.builder()
                .translationAppModule(new TranslationAppModule(this))
                .contextModule(new ContextModule(this))
                .roomModule(new RoomModule(this))
                .build();
    }

    public TranslateAppComponent getTranslateAppComponent() {
        return translateAppComponent;
    }
}
