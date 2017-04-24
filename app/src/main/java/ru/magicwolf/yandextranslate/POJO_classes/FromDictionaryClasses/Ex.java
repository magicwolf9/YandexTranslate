package ru.magicwolf.yandextranslate.POJO_classes.FromDictionaryClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Ex {

    @SerializedName("text")
    @Expose
    public String text;
    @SerializedName("tr")
    @Expose
    public List<Tr_> tr = null;

}
