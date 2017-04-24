package ru.magicwolf.yandextranslate;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        // конструктор суперкласса
        super(context, "myDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("INFO", "--- onCreate database ---");
        // создает таблицу с полями
        db.execSQL("create table history ("
                + "id integer primary key autoincrement,"
                + "rawText text,"
                + "translatedText text,"
                + "langsFromTo text,"
                + "isFavorite integer" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
