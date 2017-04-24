package ru.magicwolf.yandextranslate.POJO_classes.FromDictionaryClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;



public class FromDictionary {

    @SerializedName("head")
    @Expose
    public Head head;
    @SerializedName("def")
    @Expose
    public List<Def> def;

}
class Head {}


