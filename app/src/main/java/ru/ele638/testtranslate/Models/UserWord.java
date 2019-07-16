package ru.ele638.testtranslate.Models;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity
public class UserWord {
    @PrimaryKey(autoGenerate = true)
    private long wordId;
    private String text;

    @Embedded(prefix = "src_")
    private Language srcLang;
    @Embedded(prefix = "dst_")
    private Language dstLang;

    private String translateWord;

    public UserWord(String text, Language srcLang, Language dstLang) {
        this.text = text;
        this.srcLang = srcLang;
        this.dstLang = dstLang;
    }

    public boolean isEmptyTranslation() {
        return translateWord == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserWord word = (UserWord) o;
        if (!word.text.equals(this.text)) return false;
        if (!word.srcLang.getCode().equals(this.srcLang.getCode())) return false;
        if (!word.dstLang.getCode().equals(this.dstLang.getCode())) return false;
        return getTranslation().equals(word.getTranslation());
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(getTranslation());
    }

    public String getTranslation() {
        return String.format("%s: %s", dstLang.getName(), translateWord);
    }

    public String getText() {
        return text;
    }

    public String getTextForCard() {
        return String.format("%s: %s", srcLang.getName(), text);
    }

    public long getWordId() {
        return wordId;
    }

    public void setWordId(long wordId) {
        this.wordId = wordId;
    }

    public Language getSrcLang() {
        return srcLang;
    }

    public Language getDstLang() {
        return dstLang;
    }

    public String getTranslateWord() {
        return translateWord;
    }

    public void setTranslateWord(String translateWord) {
        this.translateWord = translateWord;
    }

}
