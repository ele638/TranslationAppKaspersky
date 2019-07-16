package ru.ele638.testtranslate.Dagger.Components;

import dagger.Component;
import ru.ele638.testtranslate.Dagger.Scopes.FavFragmentScope;
import ru.ele638.testtranslate.Views.FavFragment;

@FavFragmentScope
@Component(dependencies = TranslateAppComponent.class)
public interface FavFragmentComponent {

    void injectFavFragment(FavFragment fragment);
}
