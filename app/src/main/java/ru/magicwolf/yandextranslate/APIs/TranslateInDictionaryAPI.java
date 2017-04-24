package ru.magicwolf.yandextranslate.APIs;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import ru.magicwolf.yandextranslate.POJO_classes.FromDictionaryClasses.FromDictionary;

public interface TranslateInDictionaryAPI {
    @FormUrlEncoded
    @POST("api/v1/dicservice.json/lookup")
    Call<FromDictionary> translateText(@Field("key") String APIkey, @Field("lang") String lang, @Field("text") String text, @Field("ui") String ui);

}
