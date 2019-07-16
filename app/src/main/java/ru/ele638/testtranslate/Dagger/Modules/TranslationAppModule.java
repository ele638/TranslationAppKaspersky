package ru.ele638.testtranslate.Dagger.Modules;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.ele638.testtranslate.Helpers.LanguageDeserializer;
import ru.ele638.testtranslate.Models.Language;
import ru.ele638.testtranslate.Network.YaTranslateApi;

@Module(includes = OkHttpClientModule.class)
public class TranslationAppModule {

    private final String BASEURL = "https://translate.yandex.net/";

    Application mApplication;

    public TranslationAppModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }

    @Provides
    public YaTranslateApi getTranslateAPI(Retrofit retrofit) {
        return retrofit.create(YaTranslateApi.class);
    }

    @Provides
    @Singleton
    Retrofit retrofit(OkHttpClient okHttpClient,
                      GsonConverterFactory gsonConverterFactory) {
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASEURL)
                .addConverterFactory(gsonConverterFactory)
                .build();
    }

    @Provides
    public Gson gson() {
        Type myOtherClassListType = new TypeToken<List<Language>>() {
        }.getType();
        return new GsonBuilder()
                .registerTypeAdapter(myOtherClassListType, new LanguageDeserializer())
                .create();
    }

    @Provides
    GsonConverterFactory gsonConverterFactory(Gson gson) {
        return GsonConverterFactory.create(gson);
    }
}
