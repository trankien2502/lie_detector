package com.vtdglobal.liedetector.activity;

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
import com.vtdglobal.liedetector.util.SystemUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class LanguageActivity extends BaseActivity {
    ActivityLanguageBinding mActivityLanguageBinding;
    List<Language> listLanguage;
    LanguageAdapter languageAdapter;
    boolean isChoose = false;
    private String languageSelected;
    String codeLang;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityLanguageBinding = ActivityLanguageBinding.inflate(getLayoutInflater());
        setContentView(mActivityLanguageBinding.getRoot());

        checkLanguage();
        codeLang = Locale.getDefault().getLanguage();
        getListLanguage();
        initUI();
        initListener();
    }

    private void checkLanguage() {
        if (Objects.equals(SystemUtil.getLanguageName(getBaseContext()), ""))
            return;
        Intent intent = new Intent(LanguageActivity.this, IntroActivity.class);
        startActivity(intent);
        finishAffinity();


    }

    private void initListener() {
        mActivityLanguageBinding.imgTick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isChoose) {
                    SystemUtil.saveLocale(getBaseContext(), codeLang);
//                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("languageData", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putBoolean("selectedLanguage", true);
//                    editor.putString("languageCurrent", languageSelected);
//                    editor.apply();
                    Intent intent = new Intent(LanguageActivity.this, IntroActivity.class);
                    startActivity(intent);
                    finishAffinity();
                } else {
                    Toast.makeText(LanguageActivity.this, getString(R.string.please_select_language), Toast.LENGTH_SHORT).show();
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
        codeLang = language.getCode();
        SystemUtil.saveLocale(getBaseContext(), codeLang);
        languageSelected = language.getName();
        SystemUtil.setLanguageName(getBaseContext(),languageSelected);
        languageAdapter.setSelectedPosition(language.getCode());


    }

    private void getListLanguage() {
        listLanguage = new ArrayList<>();
        listLanguage.add(new Language(Language.ENGLISH, Language.LANGUAGE_0, R.drawable.ic_lang_en));
        listLanguage.add(new Language(Language.FRENCH, Language.LANGUAGE_1, R.drawable.ic_lang_fr));
        listLanguage.add(new Language(Language.PORTUGUESE, Language.LANGUAGE_2, R.drawable.ic_lang_pt));
        listLanguage.add(new Language(Language.GERMAN, Language.LANGUAGE_3, R.drawable.ic_lang_ge));
        listLanguage.add(new Language(Language.SPANISH, Language.LANGUAGE_4, R.drawable.ic_lang_es));
        listLanguage.add(new Language(Language.HINDI, Language.LANGUAGE_5, R.drawable.ic_lang_hi));
        listLanguage.add(new Language(Language.INDONESIA, Language.LANGUAGE_6, R.drawable.ic_lang_in));
        listLanguage.add(new Language(Language.CHINA, Language.LANGUAGE_7, R.drawable.ic_lang_zh));
    }
}