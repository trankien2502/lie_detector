package com.liedetector.test.prank.liescanner.truthtest.ui.permission;

import static android.graphics.Color.TRANSPARENT;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.ads.sapp.admob.Admob;
import com.ads.sapp.admob.AppOpenManager;
import com.ads.sapp.ads.CommonAd;
import com.ads.sapp.ads.CommonAdCallback;
import com.ads.sapp.ads.wrapper.ApAdError;
import com.ads.sapp.funtion.AdCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.liedetector.test.prank.liescanner.truthtest.R;
import com.liedetector.test.prank.liescanner.truthtest.ads.ConstantIdAds;
import com.liedetector.test.prank.liescanner.truthtest.ads.ConstantRemote;
import com.liedetector.test.prank.liescanner.truthtest.ads.IsNetWork;
import com.liedetector.test.prank.liescanner.truthtest.base.BaseActivity;
import com.liedetector.test.prank.liescanner.truthtest.databinding.ActivityPermissionBinding;
import com.liedetector.test.prank.liescanner.truthtest.ui.main.MainActivity;
import com.liedetector.test.prank.liescanner.truthtest.ui.scanner.ScannerActivity;
import com.liedetector.test.prank.liescanner.truthtest.ui.setting.SettingActivity;
import com.liedetector.test.prank.liescanner.truthtest.ui.splash.SplashActivity;
import com.liedetector.test.prank.liescanner.truthtest.util.EventTracking;
import com.liedetector.test.prank.liescanner.truthtest.util.SharePrefUtils;

public class PermissionActivity extends BaseActivity<ActivityPermissionBinding> {
    public static boolean isPermissionCamera;
    public static boolean isPermissionMicro;
    private  boolean isFromSetting = false;
    private static final String PREFS_NAME = "PermissionPrefs";
    private static final String PREF_KEY_DENY_COUNT_MIC = "denyCountMic";
    private static final String PREF_KEY_DENY_COUNT_CAM = "denyCountCam";

    public static final int MICRO_PERMISSION_REQUEST_CODE = 2;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 3;


    @Override
    public ActivityPermissionBinding getBinding() {
        return ActivityPermissionBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        if (isPermissionMicro) {
            binding.swichMicro.setChecked(true);
        }
        if (isPermissionCamera) {
            binding.swichCamera.setChecked(true);
        }
        EventTracking.logEvent(this,"permission_view");
        loadInterPermission();
        loadNativePermission();
    }
    private void showInterPermission(){
        if (IsNetWork.haveNetworkConnectionUMP(PermissionActivity.this) && ConstantIdAds.listIDAdsInterPermission.size() != 0 && ConstantRemote.inter_permission) {
            try {
                if (ConstantIdAds.mInterPermission != null) {
                    CommonAd.getInstance().forceShowInterstitial(this, ConstantIdAds.mInterPermission, new CommonAdCallback() {
                        @Override
                        public void onNextAction() {
                            startNextActivity();
                            ConstantIdAds.mInterPermission = null;
                            loadInterPermission();
                        }
                    }, true);
                } else {
                    startNextActivity();
                }
            } catch (Exception e) {
                startNextActivity();
            }
        } else {
            startNextActivity();
        }
    }
    public void loadInterPermission(){
        if (ConstantIdAds.mInterPermission == null && IsNetWork.haveNetworkConnectionUMP(PermissionActivity.this) && ConstantIdAds.listIDAdsInterPermission.size() != 0 && ConstantRemote.inter_permission) {
            ConstantIdAds.mInterPermission = CommonAd.getInstance().getInterstitialAds(this, ConstantIdAds.listIDAdsInterPermission);
        }
    }
    public void loadNativePermission() {
        try {
            if (IsNetWork.haveNetworkConnection(PermissionActivity.this) && ConstantIdAds.listIDAdsNativePermission.size() != 0 && ConstantRemote.native_permission) {
                Admob.getInstance().loadNativeAd(PermissionActivity.this, ConstantIdAds.listIDAdsNativePermission, new AdCallback() {
                    @Override
                    public void onUnifiedNativeAdLoaded(@NonNull NativeAd unifiedNativeAd) {
                        NativeAdView adView = (NativeAdView) LayoutInflater.from(PermissionActivity.this).inflate(R.layout.layout_native_show_large, null);
                        binding.nativePermission.removeAllViews();
                        binding.nativePermission.addView(adView);
                        Admob.getInstance().populateUnifiedNativeAdView(unifiedNativeAd, adView);
                    }

                    @Override
                    public void onAdFailedToLoad(@Nullable LoadAdError i) {
                        binding.nativePermission.setVisibility(View.INVISIBLE);
                    }
                });
            } else {
                binding.nativePermission.setVisibility(View.INVISIBLE);
            }

        } catch (Exception e) {
            binding.nativePermission.setVisibility(View.INVISIBLE);
        }
    }




    @Override
    public void bindView() {
        binding.btnContinue.setOnClickListener(view -> {
            if (checkAllPermission()) {
                showInterPermission();
            } else
                showDialogWarning();
            EventTracking.logEvent(PermissionActivity.this,"permission_continue_click");
        });
        binding.swichCamera.setOnCheckedChangeListener((compoundButton, b) -> {
            checkCameraPermission();
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            int denyCount = prefs.getInt(PREF_KEY_DENY_COUNT_CAM, 0);
            if (!isPermissionCamera && denyCount < 2) requestPermissionCamera();
            else if (!isPermissionCamera)
                showDialogPermissionGuide(CAMERA_PERMISSION_REQUEST_CODE);
            if (b) {
                binding.swichCamera.setChecked(isPermissionCamera);
            } else {
                if (ActivityCompat.checkSelfPermission(PermissionActivity.this,
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    binding.swichCamera.setChecked(true);
                    Toast.makeText(PermissionActivity.this, getString(R.string.camera_permission_is_already_granted), Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.swichMicro.setOnCheckedChangeListener((compoundButton, b) -> {
            checkMicroPermission();
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            int denyCount = prefs.getInt(PREF_KEY_DENY_COUNT_MIC, 0);
            if (!isPermissionMicro && denyCount < 2) requestPermissionMicro();
            else if (!isPermissionMicro)
                showDialogPermissionGuide(MICRO_PERMISSION_REQUEST_CODE);
            if (b) {
                binding.swichMicro.setChecked(isPermissionMicro);
            } else {
                if (ActivityCompat.checkSelfPermission(PermissionActivity.this,
                        Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                    binding.swichMicro.setChecked(true);
                    Toast.makeText(PermissionActivity.this, getString(R.string.microphone_permission_is_already_granted), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean checkAllPermission() {
        checkCameraPermission();
        checkMicroPermission();
        return isPermissionCamera && isPermissionMicro;
    }

    private void requestPermissionMicro() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECORD_AUDIO}, MICRO_PERMISSION_REQUEST_CODE);
    }

    private void checkMicroPermission() {
        isPermissionMicro = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissionCamera() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
    }

    private void checkCameraPermission() {
        isPermissionCamera = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MICRO_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isPermissionMicro = true;
                    binding.swichMicro.setChecked(true);
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                        int denyCount = prefs.getInt(PREF_KEY_DENY_COUNT_MIC, 0);
                        denyCount++;

                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putInt(PREF_KEY_DENY_COUNT_MIC, denyCount);
                        editor.apply();
                        //if (denyCount>2) showDialogPermissionGuide(MICRO_PERMISSION_REQUEST_CODE);
                    } else
                        Toast.makeText(this, getString(R.string.denied_mic), Toast.LENGTH_SHORT).show();
                }
                break;
            case CAMERA_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isPermissionCamera = true;
                    binding.swichCamera.setChecked(true);
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                        int denyCount = prefs.getInt(PREF_KEY_DENY_COUNT_CAM, 0);
                        denyCount++;

                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putInt(PREF_KEY_DENY_COUNT_CAM, denyCount);
                        editor.apply();
                        //if (denyCount>2) showDialogPermissionGuide(CAMERA_PERMISSION_REQUEST_CODE);

                    } else
                        Toast.makeText(this, getString(R.string.denied_camera), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void startNextActivity() {
        Intent intent = new Intent(PermissionActivity.this, MainActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    private void showDialogWarning() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_permission);

        Window window = dialog.getWindow();
        assert window != null;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(TRANSPARENT));

        dialog.setCancelable(false);
        // Set up the buttons
        TextView buttonOK = dialog.findViewById(R.id.btn_ok);
        TextView buttonCancel = dialog.findViewById(R.id.btn_cancel);

        buttonOK.setOnClickListener(v -> {
            dialog.dismiss();
            showInterPermission();
        });

        buttonCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void showDialogPermissionGuide(int perId) {
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

        if (perId == MICRO_PERMISSION_REQUEST_CODE) tvPermission.setText(R.string.microphone);
        if (perId == CAMERA_PERMISSION_REQUEST_CODE) tvPermission.setText(R.string.camera);
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFromSetting = true;
                //SharePrefUtils.setResumeBack(PermissionActivity.this,false);
                dialog.dismiss();
                //ConstantRemote.resume_back_from_setting = false;
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
                if (isFromSetting) Toast.makeText(PermissionActivity.this, "OK", Toast.LENGTH_SHORT).show();
            }
        });
        buttonCancel.setOnClickListener(v -> dialog.dismiss());
        // Show the dialog
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkMicroPermission();
        checkCameraPermission();

        if (isPermissionMicro) {
            binding.swichMicro.setChecked(true);
        }
        if (isPermissionCamera) {
            binding.swichCamera.setChecked(true);
        }
        if (isFromSetting){
            //Toast.makeText(this, "TRUE", Toast.LENGTH_SHORT).show();
            AppOpenManager.getInstance().disableAppResumeWithActivity(getClass());
            isFromSetting = false;
            return;
        }
        //Toast.makeText(this, "FALSE", Toast.LENGTH_SHORT).show();



    }
}