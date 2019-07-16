package ru.ele638.testtranslate.Dagger.Modules;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.ele638.testtranslate.Dagger.Scopes.ApplicationContext;

@Module
public class ContextModule {

    private Context context;

    public ContextModule(Context context) {
        this.context = context;
    }

    @ApplicationContext
    @Singleton
    @Provides
    public Context context() {
        return context.getApplicationContext();
    }
}

