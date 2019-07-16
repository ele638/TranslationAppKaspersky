package ru.ele638.testtranslate.Helpers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ru.ele638.testtranslate.Models.Language;

public class LanguageDeserializer implements JsonDeserializer<List<Language>> {
    @Override
    public List<Language> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject object = json
                .getAsJsonObject();

        final List<Language> outList = new ArrayList<>();
        Iterator<Map.Entry<String, JsonElement>> iterator = object.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, JsonElement> entry = iterator.next();
            String code = entry.getKey();
            String name = entry.getValue().getAsString();
            outList.add(new Language(name, code));
        }
        return outList;
    }
}
