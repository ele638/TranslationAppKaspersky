package ru.ele638.testtranslate.Dagger.Components;

import dagger.Component;
import ru.ele638.testtranslate.Dagger.Scopes.MainFragmentScope;
import ru.ele638.testtranslate.Views.MainFragment;

@MainFragmentScope
@Component(dependencies = TranslateAppComponent.class)
public interface MainFragmentComponent {

    void injectMainFragment(MainFragment fragment);
}
