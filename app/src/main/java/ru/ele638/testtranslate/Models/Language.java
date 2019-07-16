package ru.ele638.testtranslate.Models;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity
public class Language implements Parcelable {
    public static final Creator<Language> CREATOR = new Creator<Language>() {
        @Override
        public Language createFromParcel(Parcel in) {
            return new Language(in);
        }

        @Override
        public Language[] newArray(int size) {
            return new Language[size];
        }
    };
    @PrimaryKey(autoGenerate = true)
    private long langID;
    private String name;
    private String code;


    public Language(String name, String code) {
        this.name = name;
        this.code = code;
    }

    protected Language(Parcel in) {
        name = in.readString();
        code = in.readString();
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(code);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Language language = (Language) o;
        return getName().equals(language.getName()) &&
                getCode().equals(language.getCode());
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(getName(), getCode());
    }

    public long getLangID() {
        return langID;
    }

    public void setLangID(long id) {
        this.langID = id;
    }

}
