package ru.magicwolf.yandextranslate.AsynkTasks;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.magicwolf.yandextranslate.APIs.LangListAPI;
import ru.magicwolf.yandextranslate.POJO_classes.Langs;

public class GetAvailableLangsTask extends AsyncTask<String, Integer, Map<String,String>> {

    @Override
    protected Map<String, String> doInBackground(String... interfaceLang) {
        String APIkey = "trnsl.1.1.20170331T221741Z.1c5df786072321a5.3ed8a1a15f5a0554ef5bc4c5dce03970e71c6434";

        Map<String, String> availibleLangs = new HashMap<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://translate.yandex.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LangListAPI api = retrofit.create(LangListAPI.class);

        Call<Langs> call = api.getList(APIkey, interfaceLang[0]);

        try {
            Response<Langs> response = call.execute();

            availibleLangs = response.body().getLangs();
            if(response.isSuccessful()){
                Log.i("INFO", "responseSuccessful");
            } else {
                Log.i("INFO", response.errorBody().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return availibleLangs;
    }

    @Override
    protected void onPostExecute(Map<String, String> availableLangs) {
        super.onPostExecute(availableLangs);
    }
}
