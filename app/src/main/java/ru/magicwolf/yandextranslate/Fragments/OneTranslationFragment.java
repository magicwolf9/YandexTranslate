package ru.magicwolf.yandextranslate.Fragments;

import android.app.Fragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import ru.magicwolf.yandextranslate.DBHelper;
import ru.magicwolf.yandextranslate.R;

public class OneTranslationFragment extends Fragment implements View.OnClickListener {

    private ImageButton btnAddToFavorite;
    private ViewGroup container;
    private boolean isFavorite = false;
    private String tag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup _container,
                             Bundle savedInstanceState) {
        container = _container;
        View thisView = inflater.inflate(R.layout.fragment_one_translation, container, false);

        TextView tvRawText = (TextView) thisView.findViewById(R.id.tvRawText); // textView для не переведенного текса
        TextView tvTranslatedText = (TextView) thisView.findViewById(R.id.tvTranslatedText); // textView для результата перевода
        TextView tvLangsFromTo = (TextView) thisView.findViewById(R.id.tvLangsFromTo); // направление перевода (например en-ru с английского на русский)

        btnAddToFavorite = (ImageButton) thisView.findViewById(R.id.buttonAddToFavorite); // кномка добавления или удаления из избранного
        btnAddToFavorite.setOnClickListener(this);
        Log.i("INFO", "tag" + getTag());

        DBHelper dbHelper = new DBHelper(container.getContext());
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        tag = getTag();
        if(tag.contains("f")){ // если в теге есть f значит перевод в избранном
            isFavorite = true;
            btnAddToFavorite.setImageResource(R.mipmap.added_bookmark);
            tag = tag.substring(1); // в теге убирется f в началеи остается только id из БД
            Log.i("INFO", "tagsub " + tag);
        }
        Cursor cursor = database.rawQuery("SELECT rawText, translatedText, langsFromTo FROM history WHERE id =?", new String[] {tag});

        if(cursor.moveToFirst()){ // наполнение textView из БД
            tvRawText.setText(cursor.getString(cursor.getColumnIndex("rawText")));
            tvTranslatedText.setText(cursor.getString(cursor.getColumnIndex("translatedText")));
            tvLangsFromTo.setText(cursor.getString(cursor.getColumnIndex("langsFromTo")));
        }
        cursor.close();

        return thisView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonAddToFavorite:
                Log.i("INFO", "Clicked" + getTag());
                addOrDeleteFavorite(); // добавить или удалить из избранного
                break;
        }
    }

    private void addOrDeleteFavorite(){
        DBHelper dbHelper = new DBHelper(container.getContext());
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        if(!isFavorite) {
            ContentValues cv = new ContentValues();
            cv.put("isFavorite","1");
            database.update("history", cv, "id="+tag, null); // обновление строки в БД, установка isFavorite = 1
            isFavorite = true;
            btnAddToFavorite.setImageResource(R.mipmap.added_bookmark); // кнопка когда объект в избранном
            Log.i("INFO", "Now is favorite " + getTag());
        } else {
            ContentValues cv = new ContentValues();
            cv.put("isFavorite","0");
            database.update("history", cv, "id="+tag, null); // обновление строки в БД, установка isFavorite = 0
            isFavorite = false;
            btnAddToFavorite.setImageResource(R.mipmap.not_added_bookmark); // кнопка когда объект не в избранном
            Log.i("INFO", "Now is not favorite " + getTag());
        }
    }

}