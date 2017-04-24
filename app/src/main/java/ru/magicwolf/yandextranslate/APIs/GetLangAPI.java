package ru.magicwolf.yandextranslate.APIs;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import ru.magicwolf.yandextranslate.POJO_classes.GetLang;

public interface GetLangAPI {
    @FormUrlEncoded
    @POST("api/v1.5/tr.json/detect")
    Call<GetLang> getTextLang(@Field("key") String APIkey, @Field("text") String text);
}
