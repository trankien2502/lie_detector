package com.liedetector.test.prank.liescanner.truthtest.ui.language;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.liedetector.test.prank.liescanner.truthtest.base.BaseActivity;
import com.liedetector.test.prank.liescanner.truthtest.databinding.ActivityLanguageStartBinding;
import com.liedetector.test.prank.liescanner.truthtest.ui.intro.IntroActivity;
import com.liedetector.test.prank.liescanner.truthtest.ui.language.adapter.LanguageStartAdapter;
import com.liedetector.test.prank.liescanner.truthtest.ui.language.model.LanguageModel;
import com.liedetector.test.prank.liescanner.truthtest.util.EventTracking;
import com.liedetector.test.prank.liescanner.truthtest.util.SystemUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LanguageStartActivity extends BaseActivity<ActivityLanguageStartBinding> {

    List<LanguageModel> listLanguage;
    String codeLang;
    String nameLang;

    @Override
    public ActivityLanguageStartBinding getBinding() {
        return ActivityLanguageStartBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        initData();
        codeLang = Locale.getDefault().getLanguage();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        LanguageStartAdapter languageStartAdapter = new LanguageStartAdapter(listLanguage, languageModel -> {
            codeLang = languageModel.getCode();
            nameLang = languageModel.getName();
        }, this);


        languageStartAdapter.setCheck(SystemUtil.getPreLanguage(getBaseContext()));

        binding.rcvLanguage.setLayoutManager(linearLayoutManager);
        binding.rcvLanguage.setAdapter(languageStartAdapter);
        EventTracking.logEvent(this,"language_fo_open");
    }

    @Override
    public void bindView() {
        binding.imgTick.setOnClickListener(view -> {
            SystemUtil.saveLocale(getBaseContext(), codeLang);
            SystemUtil.setLanguageName(getBaseContext(),nameLang);
            EventTracking.logEvent(this,"language_fo_save_click");
            startNextActivity();
        });
    }

    private void startNextActivity() {
        startNextActivity(IntroActivity.class, null);
        finishAffinity();
    }

    private void initData() {
        listLanguage = new ArrayList<>();
        String lang = Locale.getDefault().getLanguage();
        listLanguage.add(new LanguageModel("English", "en", false));
        listLanguage.add(new LanguageModel("China", "zh", false));
        listLanguage.add(new LanguageModel("French", "fr", false));
        listLanguage.add(new LanguageModel("German", "de", false));
        listLanguage.add(new LanguageModel("Hindi", "hi", false));
        listLanguage.add(new LanguageModel("Indonesia", "in", false));
        listLanguage.add(new LanguageModel("Portuguese", "pt", false));
        listLanguage.add(new LanguageModel("Spanish", "es", false));

        for (int i = 0; i < listLanguage.size(); i++) {
            if (listLanguage.get(i).getCode().equals(lang)) {
                listLanguage.add(0, listLanguage.get(i));
                listLanguage.remove(i + 1);
            }
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
