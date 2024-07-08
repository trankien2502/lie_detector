package com.liedetector.test.prank.liescanner.truthtest.ui.language;

import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ads.sapp.admob.Admob;
import com.ads.sapp.funtion.AdCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.liedetector.test.prank.liescanner.truthtest.R;
import com.liedetector.test.prank.liescanner.truthtest.ads.ConstantIdAds;
import com.liedetector.test.prank.liescanner.truthtest.ads.ConstantRemote;
import com.liedetector.test.prank.liescanner.truthtest.ads.IsNetWork;
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
        loadNativeLanguage();
        EventTracking.logEvent(this, "language_fo_open");
    }

    @Override
    public void bindView() {
        binding.imgTick.setOnClickListener(view -> {
            SystemUtil.saveLocale(getBaseContext(), codeLang);
            SystemUtil.setLanguageName(getBaseContext(), nameLang);
            EventTracking.logEvent(this, "language_fo_save_click");
            startNextActivity();
        });
    }

    public void loadNativeLanguage() {
        try {
            if (IsNetWork.haveNetworkConnection(LanguageStartActivity.this) && ConstantIdAds.listIDAdsNativeLanguage.size() != 0 && ConstantRemote.native_language) {

                Admob.getInstance().loadNativeAd(LanguageStartActivity.this, ConstantIdAds.listIDAdsNativeLanguage, new AdCallback() {
                    @Override
                    public void onUnifiedNativeAdLoaded(@NonNull NativeAd unifiedNativeAd) {
                        NativeAdView adView = (NativeAdView) LayoutInflater.from(LanguageStartActivity.this).inflate(R.layout.layout_native_show_large, null);
                        binding.nativeLanguage.removeAllViews();
                        binding.nativeLanguage.addView(adView);
                        Admob.getInstance().populateUnifiedNativeAdView(unifiedNativeAd, adView);
                    }

                    @Override
                    public void onAdFailedToLoad(@Nullable LoadAdError i) {
                        binding.nativeLanguage.setVisibility(View.GONE);
                    }
                });
            } else {
                binding.nativeLanguage.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            binding.nativeLanguage.setVisibility(View.GONE);
        }
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
