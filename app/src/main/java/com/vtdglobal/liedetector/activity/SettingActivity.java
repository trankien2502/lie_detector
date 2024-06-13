package com.vtdglobal.liedetector.activity;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.vtdglobal.liedetector.R;
import com.vtdglobal.liedetector.databinding.ActivitySettingBinding;
import com.vtdglobal.liedetector.fragment.SoundsFragment;

public class SettingActivity extends AppCompatActivity {
    static ActivitySettingBinding mActivitySettingBinding;

    public static final int TYPE_SETTING = 1;
    public static final int TYPE_LANGUAGE = 2;
    public static final int TYPE_RATE = 3;
    public static final int TYPE_SHARE = 4;
    public static final int TYPE_ABOUT = 5;

    public static final String TEXT_SETTING = "SETTING";
    public static final String TEXT_LANGUAGE = "LANGUAGE";
    public static final String TEXT_LANGUAGE_LOW = "Language";
    public static final String TEXT_RATE = "Rate";
    public static final String TEXT_SHARE_LOW = "Share";
    public static final String TEXT_ABOUT = "ABOUT";
    public static final String TEXT_ABOUT_LOW = "About";

    public static int mType = TYPE_SETTING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivitySettingBinding = ActivitySettingBinding.inflate(getLayoutInflater());

        initListener();
        setContentView(mActivitySettingBinding.getRoot());
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Thực hiện hành động tùy chỉnh khi nút back được nhấn
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                } else {
                    // Nếu không có fragment nào trong back stack, thực hiện hành động mặc định
                    finish();
                }
            }
        });
    }
    private void initUI() {
        replaceFragment(new SoundsFragment());
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

            }
        });
    }

    private void onClickBackButton() {
        if (mType==TYPE_SETTING){
            finish();
        } else {
            replaceFragment(new SoundsFragment());
            mType = TYPE_SETTING;
            initHeaderUI();
        }
    }

    private void initListener() {
        mActivitySettingBinding.headerSetting.imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickBackButton();
            }
        });
        mActivitySettingBinding.tvfsvf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new SoundsFragment());
            }
        });
    }
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.layout_setting_content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }
    public static void initHeaderUI(){
        switch (mType){
            case TYPE_SETTING:
                mActivitySettingBinding.headerSetting.tvTitle.setText(TEXT_SETTING);
                mActivitySettingBinding.headerSetting.imgTick.setVisibility(View.GONE);
                break;
            case TYPE_LANGUAGE:
                mActivitySettingBinding.headerSetting.tvTitle.setText(TEXT_LANGUAGE);
                mActivitySettingBinding.headerSetting.imgTick.setVisibility(View.VISIBLE);
                break;
            case TYPE_ABOUT:
                mActivitySettingBinding.headerSetting.tvTitle.setText(TEXT_ABOUT);
                mActivitySettingBinding.headerSetting.imgTick.setVisibility(View.GONE);
                break;

        }
    }
}