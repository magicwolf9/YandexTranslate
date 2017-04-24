package ru.magicwolf.yandextranslate.AsynkTasks;

import android.os.AsyncTask;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.magicwolf.yandextranslate.APIs.TranslateTextAPI;
import ru.magicwolf.yandextranslate.POJO_classes.TranslateText;

public class TranslateTextTask extends AsyncTask<String, Integer, String> {
    @Override
    protected String doInBackground(String... params) {
        String APIkey = "trnsl.1.1.20170331T221741Z.1c5df786072321a5.3ed8a1a15f5a0554ef5bc4c5dce03970e71c6434",
                textFormat = "plain", options = "1";

        String text; // результат перевода

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://translate.yandex.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build(); // формирование базы запроса с парсером gson

        TranslateTextAPI api = retrofit.create(TranslateTextAPI.class);

        Call<TranslateText> call = api.translateText(APIkey, params[0], params[1], textFormat, options); // формирование запроса к API
            // param[0] - текст который необходимо перевести, param[1] - направление перевода (например en-ru с английского на русский)
        try {
            text = call.execute().body().text.get(0); // получение переведенного текста
        } catch (Exception e) {
            e.printStackTrace();
            text = params[0]; // если не удалось перевести вернуть полученный текст
        }

        return text;
    }

    @Override
    protected void onPostExecute(String translatedText) {
        super.onPostExecute(translatedText);
    }
}