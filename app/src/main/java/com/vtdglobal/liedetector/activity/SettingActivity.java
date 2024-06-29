package com.vtdglobal.liedetector.activity;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.vtdglobal.liedetector.R;
import com.vtdglobal.liedetector.databinding.ActivitySettingBinding;
import com.vtdglobal.liedetector.fragment.LanguageFragment;
import com.vtdglobal.liedetector.fragment.SettingFragment;
import com.vtdglobal.liedetector.fragment.SoundsFragment;

public class SettingActivity extends BaseActivity {
    static ActivitySettingBinding mActivitySettingBinding;
    LanguageFragment languageFragment = new LanguageFragment();
    public static final int TYPE_SETTING = 1;
    public static final int TYPE_LANGUAGE = 2;
    public static final int TYPE_RATE = 3;
    public static final int TYPE_SHARE = 4;
    public static final int TYPE_ABOUT = 5;
    public static int mType = TYPE_SETTING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivitySettingBinding = ActivitySettingBinding.inflate(getLayoutInflater());
        initUI();
        initHeaderListener();
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                onClickBackButton();
            }
        });
        setContentView(mActivitySettingBinding.getRoot());
    }

    private void initUI() {
        replaceFragment(new SettingFragment());
    }

    private void initHeaderListener() {
        mActivitySettingBinding.headerSetting.imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickBackButton();
            }
        });
        mActivitySettingBinding.headerSetting.imgTick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("languageData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("selectedLanguage", true);
                editor.putString("languageCurrent", LanguageFragment.languageSelected);
                editor.apply();
                onClickBackButton();

            }
        });
    }

    private void onClickBackButton() {
        if (mType == TYPE_SETTING) {
            finish();
        } else {
            replaceFragment(new SettingFragment());
            mType = TYPE_SETTING;
            initHeaderUI();
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.layout_setting_content, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();

    }

    public static void initHeaderUI() {
        switch (mType) {
            case TYPE_SETTING:
                mActivitySettingBinding.headerSetting.tvTitle.setText(R.string.setting);
                mActivitySettingBinding.headerSetting.imgTick.setVisibility(View.GONE);
                break;
            case TYPE_LANGUAGE:
                mActivitySettingBinding.headerSetting.tvTitle.setText(R.string.language);
                mActivitySettingBinding.headerSetting.imgTick.setVisibility(View.VISIBLE);
                break;
            case TYPE_ABOUT:
                mActivitySettingBinding.headerSetting.tvTitle.setText(R.string.up_about);
                mActivitySettingBinding.headerSetting.imgTick.setVisibility(View.GONE);
                break;

        }
    }
}