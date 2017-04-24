package ru.magicwolf.yandextranslate.AsynkTasks;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.magicwolf.yandextranslate.APIs.TranslateInDictionaryAPI;
import ru.magicwolf.yandextranslate.POJO_classes.FromDictionaryClasses.FromDictionary;

public class TranslateInDictionaryTask extends AsyncTask<String, Integer, FromDictionary> {

    @Override
    protected FromDictionary doInBackground(String... params) {
        String APIkey = "dict.1.1.20170422T165821Z.b61aef358a56f4c8.946b91035ef23c34d7a594d66f1212e7b060245c";

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://dictionary.yandex.net/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        TranslateInDictionaryAPI api = retrofit.create(TranslateInDictionaryAPI.class);

        Call<FromDictionary> call = api.translateText(APIkey, params[0], params[1], "ru");
        FromDictionary fromDictionary = new FromDictionary();

        try {
            //Log.i("INFO", call.execute().errorBody().string());
            fromDictionary = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fromDictionary;
    }

    @Override
    protected void onPostExecute(FromDictionary translatedText) {
        super.onPostExecute(translatedText);
    }
}
