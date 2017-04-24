package ru.magicwolf.yandextranslate.Fragments;

import android.app.Fragment;
import android.content.ContentValues;
//import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ru.magicwolf.yandextranslate.AsynkTasks.GetAvailableLangsTask;
import ru.magicwolf.yandextranslate.AsynkTasks.GetTextsLangTask;
import ru.magicwolf.yandextranslate.AsynkTasks.TranslateInDictionaryTask;
import ru.magicwolf.yandextranslate.AsynkTasks.TranslateTextTask;
import ru.magicwolf.yandextranslate.DBHelper;
import ru.magicwolf.yandextranslate.POJO_classes.FromDictionaryClasses.Def;
import ru.magicwolf.yandextranslate.POJO_classes.FromDictionaryClasses.Ex;
import ru.magicwolf.yandextranslate.POJO_classes.FromDictionaryClasses.FromDictionary;
import ru.magicwolf.yandextranslate.POJO_classes.FromDictionaryClasses.Tr;
import ru.magicwolf.yandextranslate.R;

//import static android.content.Context.MODE_PRIVATE;


public class TranslationFragment extends Fragment implements View.OnClickListener {
    private ViewGroup container;
    private EditText rawText;
    private TextView seeText;
    //private SharedPreferences lastLangs;
    private Spinner langsFrom;
    private Spinner langsTo;
    private Map<String, String> langs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup _container,
                             Bundle savedInstanceState) {
        container = _container;
        View thisView = inflater.inflate(R.layout.fragment_translator, container, false);

        rawText = (EditText) thisView.findViewById(R.id.typeText); // поле ввода текста
        seeText = (TextView) thisView.findViewById(R.id.seeText); // поле вывода текста
        langsFrom = (Spinner) thisView.findViewById(R.id.langFrom); // spinner с выбором языка С которого нужно перевести
        langsTo = (Spinner) thisView.findViewById(R.id.langTo); // spinner с выбором языка НА который нужно перевести
        ImageButton btnSwapeLangs = (ImageButton) thisView.findViewById(R.id.reverseLangs); // при клике меняет местами языки в spinner`ах
        ImageButton btnClearEditText = (ImageButton) thisView.findViewById(R.id.clearText); // очищает поле ввода при клике

        createLangsChoose(); // устанавливает выбор языков в spinner`ах

        rawText.addTextChangedListener(new TextWatcher() {
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            final Handler handler = new Handler(Looper.getMainLooper());
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    translateText(); // при срабатывании запускается метод перевода текста
                }
            };

            @Override
            public void afterTextChanged(final Editable s) {
                if(!s.toString().equals("")) { // если поле ввода текста не пустое запускает таймер
                    handler.removeCallbacks(runnable); // сбрасывает предыдущий таймер
                    handler.postDelayed(runnable, 1000); // устанавливает таймер на 1 секунду по истечении которого текст из поля ввода переводится
                }
            }
        });

        btnSwapeLangs.setOnClickListener(this);
        btnClearEditText.setOnClickListener(this);

        return thisView;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.reverseLangs:
                changeLangsButtonOnClick(); // поменять языки направления перевода местами
                break;
            case R.id.clearText:
                rawText.setText(""); // очистить поле ввода текста
                break;
        }
    }

    private void translateText(){

        GetTextsLangTask getTextsLangTask = new GetTextsLangTask();
        if(ifThereIsOnlyOneWord(rawText.getText().toString())){ // Проверяет сколько слов в текстовом поле
            TranslateInDictionaryTask task = new TranslateInDictionaryTask();

            try{
                setCurretLang(getTextsLangTask.execute(rawText.getText().toString()).get()); // устанавливает авоопределенный язык в spinner

                FromDictionary translatedArray = task.execute(getCurrentLangs(), rawText.getText().toString()).get(); // Получает ответ от API Словаря

                String text = formTranslationFromDictionary(translatedArray.def); // Формирует текст словарной статьи
                seeText.setText(text); // Показывает сформированный текст пользователю

                saveTranslation(rawText.getText().toString(), translatedArray.def.get(0).tr.get(0).text, getCurrentLangs());
            } catch (Exception e) {
                e.printStackTrace();
                translate(); // Если при переводе слова в словаре произошла ошибка, перевести слово в переводчике
            }
        } else{
            translate();
        }
    }

    private void translate(){
        GetTextsLangTask getTextsLangTask = new GetTextsLangTask();
        TranslateTextTask translateTextTask = new TranslateTextTask();
        try {
            String currentLangFrom = getTextsLangTask.execute(rawText.getText().toString()).get(); // автоопределение языка текста
            setCurretLang(currentLangFrom); // устанавливает авоопределенный язык в spinner

            String translatedText = translateTextTask.execute(rawText.getText().toString(), getCurrentLangs()).get(); // перевод текста с помощью API Переводчика
            String clearText = translatedText;
            translatedText += "\n\n«Переведено сервисом «Яндекс.Переводчик» http://translate.yandex.ru/";

            seeText.setText(translatedText); // показ результата перевода в textView
            saveTranslation(rawText.getText().toString(), clearText, getCurrentLangs()); // сохранение результата перевода
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean ifThereIsOnlyOneWord(String text){  // Проверяет сколько слов в текстовом поле по количеству пробелов
        text = text.replaceAll("\\s+", " "); // убирает лишние пробелы если стоят несколько подряд (превращает " d   f" в " d f")

        return text.replaceAll(" ", "").length() <= 2; // если количество пробелов в тексте меньше 3, то возвращает true (на случае если будет "to be ")
    }

    private void createLangsChoose(){
        GetAvailableLangsTask getAvailableLangsTask = new GetAvailableLangsTask();
        try {
            Map<String, String> availibleLangs = getAvailableLangsTask.execute("ru").get(); // Получает массив ключ-значение с набором доступных языков
            langs = availibleLangs;
            Log.i("INFO", availibleLangs.toString());
            setVariants(availibleLangs); // передает map в метод заполнения spinner`ов
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String getCurrentLangs(){ // получает выбранные в spinner`ах языки (для запросов к API)
        String from = "", to = "";

        for(Map.Entry entry: langs.entrySet()){ // проходится по Map и если значение совпадает с тем, что сейчас в spinner`е то выдает его ключ (для запроса к API)
            if(langsFrom.getSelectedItem().equals(entry.getValue())){
                from = (String) entry.getKey();
            }
            if(langsTo.getSelectedItem().equals(entry.getValue())){
                to = (String) entry.getKey();
            }
        }


        //lastLangs.edit().putString("from", from).apply();
        //lastLangs.edit().putString("to", to).apply();
        Log.i("INFO", from + "-" + to);
        return from + "-" + to; // возвращает строку в виде langFrom-langTo (например en-ru с английского на русский)
    }

    private void setCurretLang(String currentLang){ // Получает код языка (полученный автоопределением языка текста) и устанавливает его в spinner
        Log.i("INFO", "currentLang = " + currentLang);

        if(langsTo.getSelectedItem().toString().equals(langs.get(currentLang))){ //Если полученный язык совпадает с языком, на который должно переводиться, то их меняют местами
            int tmp = langsFrom.getSelectedItemPosition(); // позяция языка С которого должно было переводиться
            int selection = ((ArrayAdapter<String>) langsFrom.getAdapter()).getPosition(langs.get(currentLang)); // позиция языца НА который должно было переводиться, ищет позицию строки в адаптере spinner`a

            langsFrom.setSelection(selection); //меняет языки местами
            langsTo.setSelection(tmp);
        } else{ // если не совпадает изменить язык С которого будет переводиться
            Log.i("INFO", "not equal");
            langsFrom.setSelection(((ArrayAdapter<String>) langsFrom.getAdapter()).getPosition(langs.get(currentLang)));
        }

    }

    private void setVariants(Map<String, String> langs){ // устанавливает выбор языков в spinner`ах

        List<String> stringList = new ArrayList<>(langs.values()); // формирует лист значений из Map
        Log.i("INFO", stringList.toString());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(container.getContext(), android.R.layout.simple_spinner_item, stringList); // формирует адаптер для spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Log.i("INFO", adapter.getItem(0));

        langsFrom.setAdapter(adapter); // устанавливает адаптеры для spinner`ов
        langsTo.setAdapter(adapter);
    }

    private void changeLangsButtonOnClick(){ // поменять языки направления перевода местами
        int tmpId = langsTo.getSelectedItemPosition();
        langsTo.setSelection(langsFrom.getSelectedItemPosition());
        langsFrom.setSelection(tmpId);
    }

    private void saveTranslation(String rawText, String translatedText, String langsFromTo){
        DBHelper dbHelper = new DBHelper(container.getContext());
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("rawText", rawText);
        contentValues.put("translatedText", translatedText);
        contentValues.put("langsFromTo", langsFromTo);
        contentValues.put("isFavorite", 0);

        database.insert("history", null, contentValues); // сохранение результата перевода в базу данных
    }

    private String formTranslationFromDictionary(List<Def> defs){ // формирование текста словарной статьи
        String text = "";

        for(int i = 0; i < defs.size(); i++){
            Def defI = defs.get(i);
            text += "\n" + defI.text + "\n"; // не переведенное слово
            for(int k = 0; k < defI.tr.size(); k++){
                Tr trK = defI.tr.get(k); // получение первого из массивов переводов

                text += trK.text + " "; // переведенное слово
                text += "(" + trK.pos + ") \n"; // часть речи

                try {
                    if(trK.syn.size() != 0) text += "("; // если массив синонимов не пуст то открыть скобку

                    for (int s = 0; s < trK.syn.size(); s++) {
                        text += trK.syn.get(s).text + ", "; // перечисление синонимов
                    }
                    text = text.substring(0, text.length() - 2); // стирание лишних ", "
                    text += ")\n\n"; // переход на 2 строки вниз

                    if(trK.ex.size() > 0) {
                        text += "Примеры:\n"; // если массив примеров не пуст то написать "Примеры:" и на новую строку перевести
                    }

                    for (int s = 0; s < trK.ex.size(); s++) {
                        Ex exS = trK.ex.get(s);
                        text +=" " + (s + 1) + " " + exS.text + "\n"; // перевисление примеров с нумерацией (s+1)
                        if(exS.tr.size() != 0) text += "("; // если массив переводов примера не пуст, то открыть скобку

                        for (int d = 0; d < exS.tr.size(); d++) {
                            text += exS.tr.get(d).text + ", "; // перечислить варианты перевода примера
                        }

                        if(exS.tr.size() != 0){ // если массив переводов примера не пуст то
                            text = text.substring(0, text.length() - 2); //удалить лишние ", "
                            text += ")\n\n"; // закрыть скобку и перевести на 2 строки вниз
                        }
                    }
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
        }
        text += "\n\n«Переведено сервисом «Яндекс.Переводчик» http://translate.yandex.ru/ \n\n";

        return text;
    }
}
