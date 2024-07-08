package com.liedetector.test.prank.liescanner.truthtest.ads;
import android.os.Handler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.liedetector.test.prank.liescanner.truthtest.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ConstantRemote {
    public static ArrayList<String> show_language = new ArrayList<>(Collections.singletonList("1,2,3"));
    public static ArrayList<String> rate_aoa_inter_splash = new ArrayList<>(Arrays.asList("30", "70"));
    public static ArrayList<String> inter_all = new ArrayList<>(Arrays.asList("30", "70"));
    public static boolean show_inter_all = true;

    public static boolean open_splash = true;

    public static boolean inter_splash = true;
    public static boolean native_language = true;
    public static boolean inter_intro = false;
    public static boolean native_intro = true;
    public static boolean inter_permission = true;
    public static boolean native_permission = true;
    public static boolean inter_scanner = true;
    public static boolean inter_sound = true;
    public static boolean native_home = true;
    public static boolean banner_collapsible = true;
    public static boolean banner = true;

    public static boolean resume = true;



    public static void initRemoteConfig(OnCompleteListener listener) {
        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mFirebaseRemoteConfig.reset();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(3600).build();
        new Handler().postDelayed(() -> {
            mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
            mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);
            mFirebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(listener);
        }, 2000);
    }


    public static boolean getRemoteConfigBoolean(String adUnitId) {
        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        return mFirebaseRemoteConfig.getBoolean(adUnitId);
    }

    public static ArrayList<String> getRemoteConfigString(String adUnitId) {
        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        String object = mFirebaseRemoteConfig.getString(adUnitId);
        String[] arStr = object.split(",");
        return new ArrayList<>(Arrays.asList(arStr));
    }

    public static ArrayList<String> getRemoteConfigOpenSplash(String adUnitId) {
        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        String object = mFirebaseRemoteConfig.getString(adUnitId);
        String[] arStr = object.split("_");
        return new ArrayList<>(Arrays.asList(arStr));
    }
    public static long getRemoteConfigLong(String adUnitId) {
        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        return mFirebaseRemoteConfig.getLong(adUnitId);
    }
}
