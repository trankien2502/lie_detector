package com.vtdglobal.liedetector.activity;

import static android.graphics.Color.TRANSPARENT;


import static com.vtdglobal.liedetector.activity.PermissionActivity.CAMERA_PERMISSION_REQUEST_CODE;
import static com.vtdglobal.liedetector.activity.PermissionActivity.MICRO_PERMISSION_REQUEST_CODE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;
import com.vtdglobal.liedetector.R;
import com.vtdglobal.liedetector.databinding.ActivityScannerBinding;
import com.vtdglobal.liedetector.databinding.FragmentFingerPrintBinding;
import com.vtdglobal.liedetector.fragment.EyeFragment;
import com.vtdglobal.liedetector.fragment.FingerPrintFragment;
import com.vtdglobal.liedetector.fragment.SoundFragment;
import com.vtdglobal.liedetector.fragment.SoundsFragment;

public class ScannerActivity extends BaseActivity {

    @SuppressLint("StaticFieldLeak")
    static ActivityScannerBinding mActivityScannerBinding;
    public static ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    @SuppressLint("StaticFieldLeak")
    public static ProcessCameraProvider cameraProvider;
    private boolean isPermissionMicro,isPermissionCamera;
    private static final String PREFS_NAME = "PermissionPrefs";
    private static final String PREF_KEY_DENY_COUNT_MIC = "denyCountMic";
    private static final String PREF_KEY_DENY_COUNT_CAM = "denyCountCam";

    public static final int TYPE_FINGER_PRINT = 1;
    public static final int TYPE_SOUND = 2;
    public static final int TYPE_EYES = 3;

    public static final int TYPE_TRUE = 1;
    public static final int TYPE_FALSE = 0;
    public static final int TYPE_DEFAULT = -1;

    public static int mTypeScanner = TYPE_FINGER_PRINT;
    public static int mType = TYPE_DEFAULT;
    public static boolean isOpenDialog = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityScannerBinding = ActivityScannerBinding.inflate(getLayoutInflater());
        setContentView(mActivityScannerBinding.getRoot());
        initListenerHeader();
        initUI();
        initUIFooter();
        initListenerFooter();
    }
    private void initUI() {
        mTypeScanner = TYPE_FINGER_PRINT;
        replaceFragment(new FingerPrintFragment());
        initUIFooter();
        checkCameraPermission();
        checkMicroPermission();
    }
    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame_print_sound, fragment);
//        transaction.addToBackStack(null);
        transaction.commit();

    }
    private void initListenerHeader() {
        mActivityScannerBinding.header.imgLeft.setOnClickListener(view -> finish());
        mActivityScannerBinding.header.imgSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScannerActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
    }
    private void openSoundScanner(){
        checkMicroPermission();
        if (isPermissionMicro){
            replaceFragment(new SoundFragment());
            mTypeScanner = TYPE_SOUND;
            initUIFooter();
            initListenerFooter();
        } else {
            requestPermissionMicro();
        }
    }
    private void openEyeScanner(){
        checkCameraPermission();
        if (isPermissionCamera){
            startCamera();
            replaceFragment(new EyeFragment());
            mTypeScanner = TYPE_EYES;
            initUIFooter();
            initListenerFooter();
        } else {
            requestPermissionCamera();
        }
    }
    private void openFingerPrintScanner(){
        replaceFragment(new FingerPrintFragment());
        mTypeScanner = TYPE_FINGER_PRINT;
        initUIFooter();
        initListenerFooter();
    }
    private void initListenerFooter(){
        mType = TYPE_DEFAULT;
        switch (mTypeScanner){
            case TYPE_FINGER_PRINT:
                mActivityScannerBinding.layoutScannerButtonLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean isAnalyzing = FingerPrintFragment.isAnalyzing();
                        boolean isPressing = FingerPrintFragment.isButtonPressed();
                        if (isAnalyzing || isPressing){
                            showDialogStopScan(TYPE_SOUND);
                        } else {
                            openSoundScanner();
                        }

                    }
                });
                mActivityScannerBinding.layoutScannerButtonRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean isAnalyzing = FingerPrintFragment.isAnalyzing();
                        boolean isPressing = FingerPrintFragment.isButtonPressed();
                        if (isAnalyzing || isPressing){
                            showDialogStopScan(TYPE_EYES);
                        } else {
                            openEyeScanner();
                        }
                    }
                });
                break;
            case TYPE_SOUND:
                mActivityScannerBinding.layoutScannerButtonLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean isAnalyzing = SoundFragment.isAnalyzing();
                        boolean isPressing = SoundFragment.isButtonPressed();
                        if (isAnalyzing || isPressing){
                            showDialogStopScan(TYPE_FINGER_PRINT);
                        } else {
                            openFingerPrintScanner();
                        }
                    }
                });
                mActivityScannerBinding.layoutScannerButtonRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean isAnalyzing = SoundFragment.isAnalyzing();
                        boolean isPressing = SoundFragment.isButtonPressed();
                        if (isAnalyzing || isPressing){
                            showDialogStopScan(TYPE_EYES);
                        } else {
                            openEyeScanner();
                        }
                    }
                });
                break;
            case TYPE_EYES:
                mActivityScannerBinding.layoutScannerButtonLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean isAnalyzing = EyeFragment.isAnalyzing();
                        if (isAnalyzing){
                            showDialogStopScan(TYPE_SOUND);
                        } else {
                            openSoundScanner();
                        }
                    }
                });
                mActivityScannerBinding.layoutScannerButtonRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean isAnalyzing = EyeFragment.isAnalyzing();
                        if (isAnalyzing){
                            showDialogStopScan(TYPE_FINGER_PRINT);
                        } else {
                            openFingerPrintScanner();
                        }
                    }
                });
                break;
        }
    }
    public static void initUIFooter() {
        switch (mTypeScanner){
            case TYPE_FINGER_PRINT:
                mActivityScannerBinding.contentFrameEye.setVisibility(View.GONE);
                mActivityScannerBinding.imgBackgroundScanner.setVisibility(View.VISIBLE);
                switch (mType) {
                    case TYPE_DEFAULT:
                        mActivityScannerBinding.imgScannerFingerPrintBottomButton.setImageResource(R.drawable.img_finger_print_bottom_button_default);
                        mActivityScannerBinding.imgButtonLeft.setImageResource(R.drawable.img_sound_button_default);
                        mActivityScannerBinding.imgButtonRight.setImageResource(R.drawable.img_eye_button_default);
                        break;
                    case TYPE_TRUE:
                        mActivityScannerBinding.imgScannerFingerPrintBottomButton.setImageResource(R.drawable.img_finger_print_bottom_button_truth);
                        mActivityScannerBinding.imgButtonLeft.setImageResource(R.drawable.img_sound_button_truth);
                        mActivityScannerBinding.imgButtonRight.setImageResource(R.drawable.img_eye_button_truth);
                        break;
                    case TYPE_FALSE:
                        mActivityScannerBinding.imgScannerFingerPrintBottomButton.setImageResource(R.drawable.img_finger_print_bottom_button_liar);
                        mActivityScannerBinding.imgButtonLeft.setImageResource(R.drawable.img_sound_button_liar);
                        mActivityScannerBinding.imgButtonRight.setImageResource(R.drawable.img_eye_button_liar);
                        break;
                }
                mActivityScannerBinding.tvButtonLeft.setText(R.string.sound);
                mActivityScannerBinding.tvButtonRight.setText(R.string.eyes);
                break;
            case TYPE_SOUND:
                mActivityScannerBinding.contentFrameEye.setVisibility(View.GONE);
                mActivityScannerBinding.imgBackgroundScanner.setVisibility(View.VISIBLE);
                switch (mType) {
                    case TYPE_DEFAULT:
                        mActivityScannerBinding.imgScannerFingerPrintBottomButton.setImageResource(R.drawable.img_sound_bottom_button_default);
                        mActivityScannerBinding.imgButtonLeft.setImageResource(R.drawable.img_finger_print_button_default);
                        mActivityScannerBinding.imgButtonRight.setImageResource(R.drawable.img_eye_button_default);
                        break;
                    case TYPE_TRUE:
                        mActivityScannerBinding.imgScannerFingerPrintBottomButton.setImageResource(R.drawable.img_sound_bottom_button_truth);
                        mActivityScannerBinding.imgButtonLeft.setImageResource(R.drawable.img_finger_print_button_truth);
                        mActivityScannerBinding.imgButtonRight.setImageResource(R.drawable.img_eye_button_truth);
                        break;
                    case TYPE_FALSE:
                        mActivityScannerBinding.imgScannerFingerPrintBottomButton.setImageResource(R.drawable.img_sound_bottom_button_liar);
                        mActivityScannerBinding.imgButtonLeft.setImageResource(R.drawable.img_finger_print_button_liar);
                        mActivityScannerBinding.imgButtonRight.setImageResource(R.drawable.img_eye_button_liar);
                        break;
                }
                mActivityScannerBinding.tvButtonLeft.setText(R.string.finger);
                mActivityScannerBinding.tvButtonRight.setText(R.string.eyes);
                break;
            case TYPE_EYES:
                switch (mType) {
                    case TYPE_DEFAULT:
                        mActivityScannerBinding.imgScannerFingerPrintBottomButton.setImageResource(R.drawable.img_eye_bottom_button_default);
                        mActivityScannerBinding.imgButtonLeft.setImageResource(R.drawable.img_sound_button_default);
                        mActivityScannerBinding.imgButtonRight.setImageResource(R.drawable.img_finger_print_button_default);
                        mActivityScannerBinding.imgBackgroundScanner.setVisibility(View.GONE);
                        mActivityScannerBinding.contentFrameEye.setVisibility(View.VISIBLE);
                        break;
                    case TYPE_TRUE:
                        mActivityScannerBinding.imgScannerFingerPrintBottomButton.setImageResource(R.drawable.img_eye_bottom_button_truth);
                        mActivityScannerBinding.imgButtonLeft.setImageResource(R.drawable.img_sound_button_truth);
                        mActivityScannerBinding.imgButtonRight.setImageResource(R.drawable.img_finger_print_button_truth);
                        mActivityScannerBinding.imgBackgroundScanner.setVisibility(View.VISIBLE);
                        mActivityScannerBinding.contentFrameEye.setVisibility(View.GONE);
                        break;
                    case TYPE_FALSE:
                        mActivityScannerBinding.imgScannerFingerPrintBottomButton.setImageResource(R.drawable.img_eye_bottom_button_liar);
                        mActivityScannerBinding.imgButtonLeft.setImageResource(R.drawable.img_sound_button_liar);
                        mActivityScannerBinding.imgButtonRight.setImageResource(R.drawable.img_finger_print_button_liar);
                        mActivityScannerBinding.imgBackgroundScanner.setVisibility(View.VISIBLE);
                        mActivityScannerBinding.contentFrameEye.setVisibility(View.GONE);
                        break;
                }
                mActivityScannerBinding.tvButtonLeft.setText(R.string.sound);
                mActivityScannerBinding.tvButtonRight.setText(R.string.finger);
                break;
        }
    }
    private void bindPreview(ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build();
        preview.setSurfaceProvider(mActivityScannerBinding.previewView.getSurfaceProvider());

        cameraProvider.unbindAll();
        cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview);

    }
    private void startCamera() {

        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    cameraProvider = cameraProviderFuture.get();
                    bindPreview(cameraProvider);
                } catch (Exception e) {
                    Log.e("CameraXApp", "Error: ", e);
                }
            }
        }, ContextCompat.getMainExecutor(this));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MICRO_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isPermissionMicro = true;
                    replaceFragment(new SoundFragment());
                    mTypeScanner = TYPE_SOUND;
                    initUIFooter();
                    initListenerFooter();
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                        int denyCount = prefs.getInt(PREF_KEY_DENY_COUNT_MIC, 0);
                        denyCount++;

                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putInt(PREF_KEY_DENY_COUNT_MIC, denyCount);
                        editor.apply();
                        if (shouldShowRequestPermissionRationale(permissions[0])){
                            if (denyCount>2) showDialogPermissionGuide(MICRO_PERMISSION_REQUEST_CODE);
                        }else {
                            showDialogPermissionGuide(MICRO_PERMISSION_REQUEST_CODE);
                        }

                    }
                    else Toast.makeText(this, getString(R.string.denied_mic), Toast.LENGTH_SHORT).show();
                }
                break;
            case CAMERA_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isPermissionCamera = true;
                    startCamera();
                    replaceFragment(new EyeFragment());
                    mTypeScanner = TYPE_EYES;
                    initUIFooter();
                    initListenerFooter();
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                        int denyCount = prefs.getInt(PREF_KEY_DENY_COUNT_CAM, 0);
                        denyCount++;

                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putInt(PREF_KEY_DENY_COUNT_CAM, denyCount);
                        editor.apply();
                        if (shouldShowRequestPermissionRationale(permissions[0])){
                            if (denyCount>2) showDialogPermissionGuide(CAMERA_PERMISSION_REQUEST_CODE);
                        }else {
                            showDialogPermissionGuide(CAMERA_PERMISSION_REQUEST_CODE);
                        }

                    }
                    else
                        Toast.makeText(this, getString(R.string.denied_camera), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    private void requestPermissionMicro() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int denyCount = prefs.getInt(PREF_KEY_DENY_COUNT_MIC, 0);
        if (denyCount<2){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO}, MICRO_PERMISSION_REQUEST_CODE);
        }
        else {
            showDialogPermissionGuide(MICRO_PERMISSION_REQUEST_CODE);
        }

    }

    private void checkMicroPermission() {
        isPermissionMicro = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissionCamera() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int denyCount = prefs.getInt(PREF_KEY_DENY_COUNT_CAM, 0);
        if (denyCount<2){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            showDialogPermissionGuide(CAMERA_PERMISSION_REQUEST_CODE);
        }

    }

    private void checkCameraPermission() {
        isPermissionCamera = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
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
    private void showDialogStopScan(int perId) {
        isOpenDialog = true;
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_stop_scan);

        Window window = dialog.getWindow();
        assert window != null;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(TRANSPARENT));

        dialog.setCancelable(false);
        // Set up the buttons
        TextView buttonStop = dialog.findViewById(R.id.btn_stop_scan);
        TextView buttonContinue = dialog.findViewById(R.id.btn_continue_scan);

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                isOpenDialog = false;
                if (perId == TYPE_FINGER_PRINT){
                    EyeFragment.setAnalyzing(false);
                    SoundFragment.setAnalyzing(false);
                    SoundFragment.setButtonPressed(false);
                    openFingerPrintScanner();
                }
                if (perId == TYPE_SOUND){
                    FingerPrintFragment.setAnalyzing(false);
                    FingerPrintFragment.setButtonPressed(false);
                    EyeFragment.setAnalyzing(false);
                    openSoundScanner();
                }
                if (perId == TYPE_EYES){
                    FingerPrintFragment.setAnalyzing(false);
                    FingerPrintFragment.setButtonPressed(false);
                    SoundFragment.setAnalyzing(false);
                    SoundFragment.setButtonPressed(false);
                    openEyeScanner();
                }

            }
        });

        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                isOpenDialog = false;
            }
        });

        // Show the dialog
        dialog.show();
    }


    }