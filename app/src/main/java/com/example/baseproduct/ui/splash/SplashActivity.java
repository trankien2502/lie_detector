package com.example.baseproduct.ui.splash;

import android.os.Handler;

import com.example.baseproduct.base.BaseActivity;
import com.example.baseproduct.databinding.ActivitySplashBinding;
import com.example.baseproduct.ui.language.LanguageStartActivity;
import com.example.baseproduct.util.SharePrefUtils;

public class SplashActivity extends BaseActivity<ActivitySplashBinding> {


    @Override
    public ActivitySplashBinding getBinding() {
        return ActivitySplashBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        SharePrefUtils.increaseCountOpenApp(this);

        new Handler().postDelayed(() -> {
            startNextActivity(LanguageStartActivity.class, null);
            finishAffinity();
        }, 3000);

    }

    @Override
    public void bindView() {

    }

    @Override
    public void onBackPressed() {
    }
}
