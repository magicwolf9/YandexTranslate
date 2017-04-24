package ru.magicwolf.yandextranslate.POJO_classes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetLang {
    @SerializedName("code")
    @Expose
    public Integer code;
    @SerializedName("lang")
    @Expose
    public String lang;
}
