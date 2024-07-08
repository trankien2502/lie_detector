package com.liedetector.test.prank.liescanner.truthtest;

import static com.facebook.FacebookSdk.sdkInitialize;

import com.ads.sapp.admob.Admob;
import com.ads.sapp.admob.AppOpenManager;
import com.ads.sapp.ads.CommonAd;
import com.ads.sapp.ads.CommonAdConfig;
import com.ads.sapp.application.AdsMultiDexApplication;
import com.liedetector.test.prank.liescanner.truthtest.ads.ConstantRemote;
import com.liedetector.test.prank.liescanner.truthtest.ui.splash.SplashActivity;
import com.liedetector.test.prank.liescanner.truthtest.util.SharePrefUtils;

public class MyApplication extends AdsMultiDexApplication {


    @Override
    public void onCreate() {
        super.onCreate();
        SharePrefUtils.init(this);
        ConstantRemote.initRemoteConfig(task -> {
            if (task.isSuccessful()) {
                ConstantRemote.open_splash = ConstantRemote.getRemoteConfigBoolean("open_splash");
            }
        });
        sdkInitialize(getApplicationContext());
        AppOpenManager.getInstance().disableAppResumeWithActivity(SplashActivity.class);
        Admob.getInstance().setNumToShowAds(0);

    }
    public void initAds() {
        commonAdConfig.setMediationProvider(CommonAdConfig.PROVIDER_ADMOB);
        commonAdConfig.setVariant(true);
        commonAdConfig.setIdAdResume(getString(R.string.resume));
        commonAdConfig.setListDeviceTest(listTestDevice);
        commonAdConfig.setMediationFloor(CommonAdConfig.WARTER_FALL);

        CommonAd.getInstance().init(this, commonAdConfig, false);
        Admob.getInstance().setOpenActivityAfterShowInterAds(true);
        Admob.getInstance().setDisableAdResumeWhenClickAds(true);
    }


}

