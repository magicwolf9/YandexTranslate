package ru.magicwolf.yandextranslate;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import ru.magicwolf.yandextranslate.Fragments.HistoryFragment;
import ru.magicwolf.yandextranslate.Fragments.TranslationFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        ImageButton btnTranslator = (ImageButton) findViewById(R.id.buttonTranslator); // кнопка перехода к переводчику
        ImageButton btnHistory = (ImageButton) findViewById(R.id.buttonHistory); // кнопка перехода к истории переводов и избранному
        findViewById(R.id.buttonHistory).setAlpha(0.5f); // делает кнопку перехода в раздел истории полупрозрачной

        final FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        final TranslationFragment translationFragment = new TranslationFragment();
        final HistoryFragment historyFragment = new HistoryFragment();

        fragmentTransaction.add(R.id.container, translationFragment);
        fragmentTransaction.add(R.id.container, historyFragment);
        fragmentTransaction.hide(historyFragment);
        fragmentTransaction.commit(); // добавление в активность фрагментов истории и переводчика и скрытие истории


        btnTranslator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().hide(historyFragment).commit(); // фрагмент истории не активен
                fragmentManager.beginTransaction().show(translationFragment).commit();
                findViewById(R.id.buttonTranslator).setAlpha(1f); // изменение прозрачности кнопок перехода
                findViewById(R.id.buttonHistory).setAlpha(0.5f);
            }
        });

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().hide(translationFragment).commit(); //фрагмент переводчика не активен
                fragmentManager.beginTransaction().show(historyFragment).commit();
                historyFragment.showHistoryOrFavorite();
                findViewById(R.id.buttonHistory).setAlpha(1f); // изменение прозрачности кнопок перехода
                findViewById(R.id.buttonTranslator).setAlpha(0.5f);
            }
        });
    }


}
