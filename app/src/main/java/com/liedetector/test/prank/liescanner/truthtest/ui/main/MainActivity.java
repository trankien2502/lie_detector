package com.liedetector.test.prank.liescanner.truthtest.ui.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ads.sapp.admob.Admob;
import com.ads.sapp.ads.CommonAd;
import com.ads.sapp.ads.CommonAdCallback;
import com.ads.sapp.ads.wrapper.ApAdError;
import com.ads.sapp.funtion.AdCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.liedetector.test.prank.liescanner.truthtest.R;
import com.liedetector.test.prank.liescanner.truthtest.ads.ConstantIdAds;
import com.liedetector.test.prank.liescanner.truthtest.ads.ConstantRemote;
import com.liedetector.test.prank.liescanner.truthtest.ads.IsNetWork;
import com.liedetector.test.prank.liescanner.truthtest.base.BaseActivity;
import com.liedetector.test.prank.liescanner.truthtest.databinding.ActivityMainBinding;
import com.liedetector.test.prank.liescanner.truthtest.dialog.exit.ExitAppDialog;
import com.liedetector.test.prank.liescanner.truthtest.dialog.rate.IClickDialogRate;
import com.liedetector.test.prank.liescanner.truthtest.dialog.rate.RatingDialog;
import com.liedetector.test.prank.liescanner.truthtest.ui.intro.IntroActivity;
import com.liedetector.test.prank.liescanner.truthtest.ui.language.LanguageActivity;
import com.liedetector.test.prank.liescanner.truthtest.ui.language.LanguageStartActivity;
import com.liedetector.test.prank.liescanner.truthtest.ui.permission.PermissionActivity;
import com.liedetector.test.prank.liescanner.truthtest.ui.scanner.ScannerActivity;
import com.liedetector.test.prank.liescanner.truthtest.ui.setting.SettingActivity;
import com.liedetector.test.prank.liescanner.truthtest.ui.sound.SoundsActivity;
import com.liedetector.test.prank.liescanner.truthtest.ui.splash.SplashActivity;
import com.liedetector.test.prank.liescanner.truthtest.util.EventTracking;
import com.liedetector.test.prank.liescanner.truthtest.util.SharePrefUtils;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    @Override
    public ActivityMainBinding getBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        EventTracking.logEvent(this, "home_view");
        //loadNativeHome();
        loadInterScanner();
        loadInterSound();
    }

    @Override
    public void bindView() {
        binding.layoutScanner.setOnClickListener(view -> {
            showInterScanner();
            EventTracking.logEvent(this, "home_scanner_click");
        });

        binding.layoutSounds.setOnClickListener(view -> {
            showInterSound();
            EventTracking.logEvent(this, "home_sound_click");
        });
    }
    public void loadNativeHome() {
        try {
            if (IsNetWork.haveNetworkConnection(MainActivity.this) && ConstantIdAds.listIDAdsNativeHome.size() != 0 && ConstantRemote.native_home) {
                NativeAdView adViewLoad = (NativeAdView) LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_native_load_large, null,false);
                binding.nativeHome.removeAllViews();
                binding.nativeHome.addView(adViewLoad);
                Admob.getInstance().loadNativeAd(MainActivity.this, ConstantIdAds.listIDAdsNativeHome, new AdCallback() {
                    @Override
                    public void onUnifiedNativeAdLoaded(@NonNull NativeAd unifiedNativeAd) {
                        NativeAdView adView = (NativeAdView) LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_native_show_large, null,false);
                        binding.nativeHome.removeAllViews();
                        binding.nativeHome.addView(adView);
                        Admob.getInstance().populateUnifiedNativeAdView(unifiedNativeAd, adView);
                    }

                    @Override
                    public void onAdFailedToLoad(@Nullable LoadAdError i) {
                        binding.nativeHome.setVisibility(View.INVISIBLE);
                    }
                });
            } else {
                binding.nativeHome.setVisibility(View.INVISIBLE);
            }

        } catch (Exception e) {
            binding.nativeHome.setVisibility(View.INVISIBLE);
        }
    }
    private void countExitApp(){
        final String PREFS_NAME = "ExitAppPrefs";
        final String PREF_KEY_COUNT_EXIT_APP = "exitAppCount";
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int count = prefs.getInt(PREF_KEY_COUNT_EXIT_APP, 0);
        count++;
        //SharePrefUtils.putInt(PREFS_NAME,count);
        if (count%2==0){
            onRate();
        }
    }
    private void onRate() {
        RatingDialog ratingDialog = new RatingDialog(MainActivity.this, true);
        ratingDialog.init(new IClickDialogRate() {
            @Override
            public void send() {
                ratingDialog.dismiss();
                String uriText = "mailto:" + SharePrefUtils.email + "?subject=" + "Review for " + SharePrefUtils.subject + "&body=" + SharePrefUtils.subject + "\nRate : " + ratingDialog.getRating() + "\nContent: ";
                Uri uri = Uri.parse(uriText);
                Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
                sendIntent.setData(uri);
                try {
                    startActivity(Intent.createChooser(sendIntent, getString(R.string.Send_Email)));
                    SharePrefUtils.forceRated(getApplicationContext());
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), getString(R.string.There_is_no), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void rate() {
                EventTracking.logEvent(getApplicationContext(),"rate_submit");
                ReviewManager manager = ReviewManagerFactory.create(getApplicationContext());
                Task<ReviewInfo> request = manager.requestReviewFlow();
                request.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ReviewInfo reviewInfo = task.getResult();
                        Task<Void> flow = manager.launchReviewFlow(MainActivity.this, reviewInfo);
                        flow.addOnSuccessListener(result -> {
                            SharePrefUtils.forceRated(MainActivity.this);
                            ratingDialog.dismiss();
                        });
                    } else {
                        ratingDialog.dismiss();
                    }
                });
            }

            @Override
            public void later() {
                EventTracking.logEvent(MainActivity.this,"rate_not_now");
                ratingDialog.dismiss();
            }

        });
        ratingDialog.show();
        EventTracking.logEvent(this,"rate_show");
    }
    private void loadInterScanner() {
        if (ConstantIdAds.mInterScanner == null && IsNetWork.haveNetworkConnectionUMP(MainActivity.this) && ConstantIdAds.listIDAdsInterScanner.size() != 0 && ConstantRemote.inter_scanner) {
            ConstantIdAds.mInterScanner = CommonAd.getInstance().getInterstitialAds(this, ConstantIdAds.listIDAdsInterScanner);
        }
    }

    private void loadInterSound() {
        if (ConstantIdAds.mInterSound == null && IsNetWork.haveNetworkConnectionUMP(MainActivity.this) && ConstantIdAds.listIDAdsInterSound.size() != 0 && ConstantRemote.inter_sound) {
            ConstantIdAds.mInterSound = CommonAd.getInstance().getInterstitialAds(this, ConstantIdAds.listIDAdsInterSound);
        }
    }

    private void showInterScanner() {
        if (IsNetWork.haveNetworkConnectionUMP(MainActivity.this) && ConstantIdAds.listIDAdsInterScanner.size() != 0 && ConstantRemote.inter_scanner) {
            try {
                if (ConstantIdAds.mInterScanner != null) {
                    CommonAd.getInstance().forceShowInterstitial(this, ConstantIdAds.mInterScanner, new CommonAdCallback() {
                        @Override
                        public void onNextAction() {
                            startNextActivity(ScannerActivity.class);
                            ConstantIdAds.mInterScanner = null;
                            loadInterScanner();
                        }
                    }, true);
                } else {
                    startNextActivity(ScannerActivity.class);
                }
            } catch (Exception e) {
                startNextActivity(ScannerActivity.class);
            }
        } else {
            startNextActivity(ScannerActivity.class);
        }

    }
    private void startNextActivity(Class activity){
        Intent intent = new Intent(this,activity);
        startActivity(intent);
        //loadNativeHome();
    }
    public ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            binding.nativeHome.removeAllViews();
            try {
                binding.nativeHome.addView((NativeAdView) LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_native_load_large, null));
                loadNativeHome();
            } catch (Exception e) {
                binding.nativeHome.setVisibility(View.INVISIBLE);
            }
        }
    });

    
    private void showInterSound() {
        if (IsNetWork.haveNetworkConnectionUMP( MainActivity.this) && ConstantIdAds.listIDAdsInterSound.size() != 0 && ConstantRemote.show_inter_all) {
            try {
                if (ConstantIdAds.mInterSound != null) {
                    CommonAd.getInstance().forceShowInterstitial(this, ConstantIdAds.mInterSound, new CommonAdCallback() {
                        @Override
                        public void onNextAction() {
                            startNextActivity(SoundsActivity.class,null);
                            ConstantIdAds.mInterSound = null;
                            loadInterSound();
                        }
                    }, true);
                } else {
                    startNextActivity(SoundsActivity.class);
                }
            } catch (Exception e) {
                startNextActivity(SoundsActivity.class);
            }
        } else {
            startNextActivity(SoundsActivity.class);
        }

}


    private void showDialogExit() {
        ExitAppDialog dialog = new ExitAppDialog(this, false);
        dialog.init(this::finishAffinity);
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNativeHome();
    }

    @Override
    public void onBackPressed() {
        showDialogExit();
    }
}