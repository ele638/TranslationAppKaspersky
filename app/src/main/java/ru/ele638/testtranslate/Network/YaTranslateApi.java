package ru.ele638.testtranslate.Network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YaTranslateApi {
    @GET("api/v1.5/tr.json/getLangs?ui=ru")
    Call<LanguageList> getLanguages(@Query("key") String key);

    @GET("api/v1.5/tr.json/translate")
    Call<TranslateResponse> getTranslate(@Query("key") String apikey, @Query("text") String text, @Query("lang") String lang);
}
