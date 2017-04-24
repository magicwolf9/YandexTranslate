package ru.magicwolf.yandextranslate.AsynkTasks;

import android.os.AsyncTask;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.magicwolf.yandextranslate.APIs.GetLangAPI;
import ru.magicwolf.yandextranslate.POJO_classes.GetLang;

public class GetTextsLangTask extends AsyncTask<String, Integer, String> {
    @Override
    protected String doInBackground(String... params) {
        String APIkey = "trnsl.1.1.20170331T221741Z.1c5df786072321a5.3ed8a1a15f5a0554ef5bc4c5dce03970e71c6434",
                lang = "ru";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://translate.yandex.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GetLangAPI api = retrofit.create(GetLangAPI.class);

        Call<GetLang> call = api.getTextLang(APIkey, params[0]);

        try {
            lang = call.execute().body().lang;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lang;
    }

    @Override
    protected void onPostExecute(String lang) {
        super.onPostExecute(lang);
    }
}
