package ru.magicwolf.yandextranslate.APIs;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import ru.magicwolf.yandextranslate.POJO_classes.TranslateText;

public interface TranslateTextAPI {
    @FormUrlEncoded
    @POST("api/v1.5/tr.json/translate")
    Call<TranslateText> translateText(@Field("key") String APIkey, @Field("text") String text, @Field("lang") String lang, @Field("format") String format, @Field("option") String option);

}
