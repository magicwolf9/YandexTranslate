package ru.magicwolf.yandextranslate.APIs;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import ru.magicwolf.yandextranslate.POJO_classes.Langs;

public interface LangListAPI {
    @FormUrlEncoded
    @POST("api/v1.5/tr.json/getLangs")
    Call<Langs> getList(@Field("key") String APIkey, @Field("ui") String ui); // ui = ru
}
