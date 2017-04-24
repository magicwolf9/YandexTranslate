package ru.magicwolf.yandextranslate.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import ru.magicwolf.yandextranslate.DBHelper;
import ru.magicwolf.yandextranslate.R;

public class HistoryFragment extends Fragment implements View.OnClickListener{

    private Button btnHistoryList, btnFavoriteList;
    private ImageButton btnClearHistory;
    private ViewGroup container;
    private View thisView;
    private boolean fav;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup _container,
                             Bundle savedInstanceState) {
        container = _container;
        Log.i("INFO", "there");
        thisView = inflater.inflate(R.layout.fragment_history, container, false);

        btnFavoriteList = (Button) thisView.findViewById(R.id.buttonFavoriteList); // кнопка показа избранного
        btnHistoryList = (Button) thisView.findViewById(R.id.buttonHistoryList); // кнопка показа истории переводов
        btnClearHistory = (ImageButton) thisView.findViewById(R.id.buttonClear); // кнопка очистки блока

        fav = false;
        btnFavoriteList.setOnClickListener(this);
        btnHistoryList.setOnClickListener(this);
        btnClearHistory.setOnClickListener(this);

        showHistoryOrFavorite();

        return thisView;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.buttonFavoriteList:
                fav = true; // показан блок збранного
                showHistoryOrFavorite();
                break;
            case R.id.buttonHistoryList:
                fav = false; // показан блок истории
                showHistoryOrFavorite();
                break;
            case R.id.buttonClear:
                clear(); // очистка блока
                break;
        }
    }

    public void showHistoryOrFavorite(){
        btnHistoryList.setBackgroundResource(R.color.transparent);
        btnFavoriteList.setBackgroundResource(R.drawable.bottom_black_line);

        Log.i("INFO", "there");
        LinearLayout fContainer = (LinearLayout) thisView.findViewById(R.id.fragmentsContainer);
        if(fContainer.getChildCount() > 0) fContainer.removeAllViews(); // очистка контейнера если там есть фрагменты

        DBHelper dbHelper = new DBHelper(container.getContext());
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        String tag = "f"; // часть тега, присваевамая фрагменту, если есть f - кнопка фрагмента будет выделена как добавленное в закладки (added_bookmark)
        Cursor cursor = database.rawQuery("SELECT id FROM history WHERE isFavorite = 1", new String[] {}); // получение избранного

        if(cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                final OneTranslationFragment fragment = new OneTranslationFragment();
                fragmentTransaction.add(R.id.fragmentsContainer, fragment, tag + cursor.getString(cursor.getColumnIndex("id")));
                    // заполнение контейнера вхождениями в БД
                Log.i("INFO", fragment.getTag());
                cursor.moveToNext();
            }
            cursor.close();
        }

        if(!fav) { // если пользователь в блоке истории
            btnHistoryList.setBackgroundResource(R.drawable.bottom_black_line);
            btnFavoriteList.setBackgroundResource(R.color.transparent);

            tag = "";
            cursor = database.rawQuery("SELECT id FROM history WHERE isFavorite = 0", new String[]{}); // получение не избранного

            if (cursor.moveToFirst()) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    final OneTranslationFragment fragment = new OneTranslationFragment();
                    fragmentTransaction.add(R.id.fragmentsContainer, fragment, tag + cursor.getString(cursor.getColumnIndex("id")));
                        // заполнение контейнера вхождениями в БД
                    Log.i("INFO", fragment.getTag());
                    cursor.moveToNext();
                }

                cursor.close();
            }
        }
        fragmentTransaction.commit(); // применение изменений

    }

    private void clear(){
        DBHelper dbHelper = new DBHelper(container.getContext());
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        if(fav){ // если пользователь в разделе избранного, то очищается избранное
            database.delete("history", "isFavorite = 1", null);
        } else { // если нет, то очищается история (кроме избранного)
            database.delete("history", "isFavorite = 0", null);
        }
        showHistoryOrFavorite(); // обновление фрагментов
    }
}
