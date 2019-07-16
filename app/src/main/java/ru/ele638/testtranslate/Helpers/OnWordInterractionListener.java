package ru.ele638.testtranslate.Helpers;

import ru.ele638.testtranslate.Models.UserWord;

public interface OnWordInterractionListener {
    void updateWord(UserWord word);
    void deleteWord(UserWord word);
}
