package ru.magicwolf.yandextranslate.POJO_classes.FromDictionaryClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Tr {

    @SerializedName("text")
    @Expose
    public String text;
    @SerializedName("pos")
    @Expose
    public String pos;
    @SerializedName("syn")
    @Expose
    public List<Syn> syn = null;
    @SerializedName("mean")
    @Expose
    public List<Mean> mean = null;
    @SerializedName("ex")
    @Expose
    public List<Ex> ex = null;

}
