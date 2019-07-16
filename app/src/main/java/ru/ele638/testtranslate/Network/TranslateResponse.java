package ru.ele638.testtranslate.Network;

import java.util.List;

public class TranslateResponse {
    private List<String> text;

    public List<String> getText() {
        return text;
    }

    public void setText(List<String> text) {
        this.text = text;
    }
}
