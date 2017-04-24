package ru.magicwolf.yandextranslate.POJO_classes;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class Langs{
    @SerializedName("langs")
    private Map<String, String> langs;

    public Map<String, String> getLangs() {
        return this.langs;
    }

    public void setLangs(String name, String value) {
        this.langs.put(name, value);
    }

}
