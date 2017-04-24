package ru.magicwolf.yandextranslate.POJO_classes.FromDictionaryClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Def {
    @SerializedName("text")
    @Expose
    public String text;
    @SerializedName("pos")
    @Expose
    public String pos;
    @SerializedName("tr")
    @Expose
    public List<Tr> tr = null;

}
