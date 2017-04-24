package ru.magicwolf.yandextranslate.POJO_classes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TranslateText {
    @SerializedName("code")
    @Expose
    public Integer code;
    @SerializedName("lang")
    @Expose
    public String lang;
    @SerializedName("text")
    @Expose
    public List<String> text = null;

}

