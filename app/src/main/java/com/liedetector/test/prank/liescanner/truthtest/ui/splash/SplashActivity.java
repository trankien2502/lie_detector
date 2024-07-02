package com.liedetector.test.prank.liescanner.truthtest.ui.splash;

import android.os.Handler;

import com.liedetector.test.prank.liescanner.truthtest.base.BaseActivity;
import com.liedetector.test.prank.liescanner.truthtest.databinding.ActivitySplashBinding;
import com.liedetector.test.prank.liescanner.truthtest.ui.language.LanguageStartActivity;
import com.liedetector.test.prank.liescanner.truthtest.util.SharePrefUtils;

public class SplashActivity extends BaseActivity<ActivitySplashBinding> {


    @Override
    public ActivitySplashBinding getBinding() {
        return ActivitySplashBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        SharePrefUtils.increaseCountOpenApp(this);

        new Handler().postDelayed(() -> {
            startNextActivity();
        }, 3000);

    }

    @Override
    public void bindView() {

    }

    private void startNextActivity() {
        startNextActivity(LanguageStartActivity.class, null);
        finishAffinity();
    }

    @Override
    public void onBackPressed() {
    }
}
