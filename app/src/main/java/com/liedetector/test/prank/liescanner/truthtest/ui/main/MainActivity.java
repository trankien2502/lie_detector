package com.liedetector.test.prank.liescanner.truthtest.ui.main;

import static android.graphics.Color.TRANSPARENT;

import static com.liedetector.test.prank.liescanner.truthtest.ui.permission.PermissionActivity.CAMERA_PERMISSION_REQUEST_CODE;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

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
import com.liedetector.test.prank.liescanner.truthtest.dialog.exit.IClickDialogExit;
import com.liedetector.test.prank.liescanner.truthtest.dialog.rate.IClickDialogRate;
import com.liedetector.test.prank.liescanner.truthtest.dialog.rate.RatingDialog;
import com.liedetector.test.prank.liescanner.truthtest.ui.ghost.GhostActivity;
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

    boolean isPermissionCamera;
    private static final String PREFS_NAME = "PermissionPrefs";
    private static final String PREF_KEY_DENY_COUNT_CAM = "denyCountCam";
    @Override
    public ActivityMainBinding getBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        EventTracking.logEvent(this, "home_view");
        loadNativeHome();
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
        binding.layoutGhost.setOnClickListener(view -> {
            checkCameraPermission();
            if (isPermissionCamera) {
                startNextActivity(GhostActivity.class);
            } else {
                requestPermissionCamera();
            }
//            startNextActivity(GhostActivity.class,null);
            EventTracking.logEvent(this, "home_ghost_click");
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
                        NativeAdView adView = (NativeAdView) LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_native_show_large1, null,false);
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
    public ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            //binding.nativeHome.removeAllViews();
            try {
                //binding.nativeHome.addView((NativeAdView) LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_native_load_large, null));
                loadNativeHome();
            } catch (Exception e) {
                binding.nativeHome.setVisibility(View.INVISIBLE);
            }
        }
    });

    private void showDialogRate() {
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
                    finishAffinity();
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), getString(R.string.There_is_no), Toast.LENGTH_SHORT).show();
                    SharePrefUtils.increaseCountExitApp(MainActivity.this);
                    finishAffinity();
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
                            finishAffinity();
                        });
                    } else {
                        SharePrefUtils.increaseCountExitApp(MainActivity.this);
                        finishAffinity();
                        ratingDialog.dismiss();
                    }
                });
            }

            @Override
            public void later() {
                EventTracking.logEvent(MainActivity.this,"rate_not_now");
                SharePrefUtils.increaseCountExitApp(MainActivity.this);
                finishAffinity();
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
        resultLauncher.launch(intent);
        //startActivity(intent);
    }

    
    private void showInterSound() {
        if (IsNetWork.haveNetworkConnectionUMP( MainActivity.this) && ConstantIdAds.listIDAdsInterSound.size() != 0 && ConstantRemote.inter_sound) {
            try {
                if (ConstantIdAds.mInterSound != null) {
                    CommonAd.getInstance().forceShowInterstitial(this, ConstantIdAds.mInterSound, new CommonAdCallback() {
                        @Override
                        public void onNextAction() {
                            startNextActivity(SoundsActivity.class);
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
        dialog.init(new IClickDialogExit() {
            @Override
            public void quit() {
                SharePrefUtils.increaseCountExitApp(MainActivity.this);
                finishAffinity();
            }
        });
        dialog.show();
    }

    private void showRateOrExitApp() {
        if (SharePrefUtils.isRated(this)) {
            showDialogExit();
        } else {
            int count = SharePrefUtils.getCountExitApp(this);
            if (count%2==0){
                showDialogRate();
            }else {
                showDialogExit();
            }
        }
    }
    private void requestPermissionCamera() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int denyCount = prefs.getInt(PREF_KEY_DENY_COUNT_CAM, 0);
        if (denyCount < 2) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            showDialogPermissionGuide();
        }

    }

    private void checkCameraPermission() {
        isPermissionCamera = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            isPermissionCamera = true;
            startNextActivity(GhostActivity.class,null);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                int denyCount = prefs.getInt(PREF_KEY_DENY_COUNT_CAM, 0);
                denyCount++;

                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt(PREF_KEY_DENY_COUNT_CAM, denyCount);
                editor.apply();
            } else
                Toast.makeText(this, getString(R.string.denied_camera), Toast.LENGTH_SHORT).show();
        }
    }
    private void showDialogPermissionGuide() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_permission_guide);

        Window window = dialog.getWindow();
        assert window != null;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(TRANSPARENT));

        dialog.setCancelable(false);
        // Set up the buttons
        TextView tvPermission = dialog.findViewById(R.id.tv_permission);
        TextView buttonOK = dialog.findViewById(R.id.btn_ok_guild);
        TextView buttonCancel = dialog.findViewById(R.id.btn_cancel_guild);

        tvPermission.setText(R.string.camera);

        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // Show the dialog
        dialog.show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //loadNativeHome();
    }

    @Override
    public void onBackPressed() {
        showRateOrExitApp();
    }
}