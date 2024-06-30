package com.vtdglobal.liedetector.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vtdglobal.liedetector.R;
import com.vtdglobal.liedetector.adapter.LanguageAdapter;
import com.vtdglobal.liedetector.databinding.FragmentLanguageBinding;
import com.vtdglobal.liedetector.model.Language;
import com.vtdglobal.liedetector.util.SystemUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class LanguageFragment extends Fragment {

    FragmentLanguageBinding mFragmentLanguageBinding;
    List<Language> listLanguage;
    LanguageAdapter languageAdapter;
    public static String languageSelected;
    String codeLang;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentLanguageBinding = FragmentLanguageBinding.inflate(inflater,container,false);
        codeLang = Locale.getDefault().getLanguage();
        getListLanguage();
        //getLanguageCurrent();
        initUI();

        return mFragmentLanguageBinding.getRoot();
    }


    private void initUI() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mFragmentLanguageBinding.rcvLanguage.setLayoutManager(linearLayoutManager);
        languageAdapter = new LanguageAdapter(getContext(), listLanguage, this::onSelected);
        languageAdapter.setSettingMode(true);
        mFragmentLanguageBinding.rcvLanguage.setAdapter(languageAdapter);

        String languageCurrent = SystemUtil.getPreLanguage(requireContext());
        for (Language language : listLanguage){
            if (languageCurrent.equals(language.getCode())){
                languageAdapter.setSelectedPosition(language.getCode());
            }
        }
    }
    private void onSelected(Language language) {
        codeLang = language.getCode();
        SystemUtil.saveLocale(requireContext(), codeLang);
        languageSelected = language.getName();
        SystemUtil.setLanguageName(requireContext(),languageSelected);
        languageAdapter.setSelectedPosition(language.getCode());
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