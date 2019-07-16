package ru.ele638.testtranslate.Dagger.Components;

import javax.inject.Singleton;

import dagger.Component;
import ru.ele638.testtranslate.Dagger.Modules.ContextModule;
import ru.ele638.testtranslate.Dagger.Modules.RoomModule;
import ru.ele638.testtranslate.Dagger.Modules.TranslationAppModule;
import ru.ele638.testtranslate.Helpers.Room.AppDatabase;
import ru.ele638.testtranslate.Network.YaTranslateApi;

@Singleton
@Component(modules = {TranslationAppModule.class, ContextModule.class, RoomModule.class})
public interface TranslateAppComponent {
    YaTranslateApi getTranslationService();

    AppDatabase database();
}
