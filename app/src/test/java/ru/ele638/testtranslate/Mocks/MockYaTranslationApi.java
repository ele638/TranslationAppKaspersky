package ru.ele638.testtranslate.Mocks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.ele638.testtranslate.Models.Language;
import ru.ele638.testtranslate.Network.LanguageList;
import ru.ele638.testtranslate.Network.TranslateResponse;
import ru.ele638.testtranslate.Network.YaTranslateApi;

public class MockYaTranslationApi implements YaTranslateApi {
    @Override
    public Call<LanguageList> getLanguages(String key) {
        return new Call<LanguageList>() {
            @Override
            public Response<LanguageList> execute() throws IOException {
                LanguageList list = new LanguageList();
                List<Language> bodyList = new ArrayList<>();
                bodyList.add(new Language("Russian", "ru"));
                bodyList.add(new Language("English", "en"));
                bodyList.add(new Language("French", "fr"));
                list.setList(bodyList);
                return Response.success(list);
            }

            @Override
            public void enqueue(Callback<LanguageList> callback) {
                try {
                    callback.onResponse(this, execute());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public boolean isExecuted() {
                return false;
            }

            @Override
            public void cancel() {

            }

            @Override
            public boolean isCanceled() {
                return false;
            }

            @Override
            public Call<LanguageList> clone() {
                return null;
            }

            @Override
            public Request request() {
                return null;
            }
        };
    }

    @Override
    public Call<TranslateResponse> getTranslate(String apikey, String text, String lang) {
        return new Call<TranslateResponse>() {
            @Override
            public Response<TranslateResponse> execute() throws IOException {
                TranslateResponse response = new TranslateResponse();
                List<String> translations = new ArrayList<>();
                translations.add("TranslationForWord");
                response.setText(translations);
                return Response.success(response);
            }

            @Override
            public void enqueue(Callback<TranslateResponse> callback) {
                try {
                    callback.onResponse(this, execute());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public boolean isExecuted() {
                return false;
            }

            @Override
            public void cancel() {

            }

            @Override
            public boolean isCanceled() {
                return false;
            }

            @Override
            public Call<TranslateResponse> clone() {
                return null;
            }

            @Override
            public Request request() {
                return null;
            }
        };
    }
}
