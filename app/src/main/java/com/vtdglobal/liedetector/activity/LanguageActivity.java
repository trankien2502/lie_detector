package com.vtdglobal.liedetector.activity;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.vtdglobal.liedetector.R;
import com.vtdglobal.liedetector.adapter.LanguageAdapter;
import com.vtdglobal.liedetector.databinding.ActivityLanguageBinding;
import com.vtdglobal.liedetector.model.Language;

import java.util.ArrayList;
import java.util.List;

public class LanguageActivity extends AppCompatActivity {
    ActivityLanguageBinding mActivityLanguageBinding;
    List<Language> listLanguage;
    LanguageAdapter languageAdapter;
    boolean isChoose = false;
    private String languageSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityLanguageBinding = ActivityLanguageBinding.inflate(getLayoutInflater());
        setContentView(mActivityLanguageBinding.getRoot());
        checkLanguage();
        getListLanguage();
        initUI();
        initListener();
    }
    private void checkLanguage (){
        SharedPreferences sharedPreferences =getApplicationContext().getSharedPreferences("languageData", Context.MODE_PRIVATE);
        boolean check = sharedPreferences.getBoolean("selectedLanguage", Boolean.valueOf(String.valueOf(Context.MODE_PRIVATE)));
        if (check) {
            Intent intent = new Intent(LanguageActivity.this,IntroActivity.class);
            startActivity(intent);
            finish();
        }
    }
    private void initListener() {
        mActivityLanguageBinding.imgTick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isChoose) {
                    SharedPreferences sharedPreferences =getApplicationContext().getSharedPreferences("languageData", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("selectedLanguage",true);
                    editor.putString("languageCurrent", languageSelected);
                    editor.apply();
                    Intent intent = new Intent(LanguageActivity.this, IntroActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LanguageActivity.this, "Please choose a language!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void initUI() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mActivityLanguageBinding.rcvLanguage.setLayoutManager(linearLayoutManager);
        languageAdapter = new LanguageAdapter(this, listLanguage, this::onSelected);
        mActivityLanguageBinding.rcvLanguage.setAdapter(languageAdapter);
    }

    private void onSelected(Language language) {
        isChoose = true;
        languageAdapter.setSelectedPosition(language.getId());
        languageSelected = language.getName();
    }

    private void getListLanguage() {
        listLanguage = new ArrayList<>();
        listLanguage.add(new Language(Language.ENGLISH, Language.LANGUAGE_0, R.drawable.img_eng));
        listLanguage.add(new Language(Language.FRENCH, Language.LANGUAGE_1, R.drawable.img_french));
        listLanguage.add(new Language(Language.PORTUGUESE, Language.LANGUAGE_2, R.drawable.img_portuguese));
        listLanguage.add(new Language(Language.GERMAN, Language.LANGUAGE_3, R.drawable.img_german));
        listLanguage.add(new Language(Language.SPANISH, Language.LANGUAGE_4, R.drawable.img_spanish));
        listLanguage.add(new Language(Language.HINDI, Language.LANGUAGE_5, R.drawable.img_hindi));
        listLanguage.add(new Language(Language.INDONESIA, Language.LANGUAGE_6, R.drawable.img_indo));
        listLanguage.add(new Language(Language.CHINA, Language.LANGUAGE_7, R.drawable.img_china));
    }
}