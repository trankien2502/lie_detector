package com.vtdglobal.liedetector.activity;

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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;
import com.vtdglobal.liedetector.R;
import com.vtdglobal.liedetector.databinding.ActivityScannerBinding;
import com.vtdglobal.liedetector.fragment.EyeFragment;
import com.vtdglobal.liedetector.fragment.FingerPrintFragment;
import com.vtdglobal.liedetector.fragment.SoundFragment;
import com.vtdglobal.liedetector.fragment.SoundsFragment;

public class ScannerActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    static ActivityScannerBinding mActivityScannerBinding;
    public static ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    @SuppressLint("StaticFieldLeak")
    public static ProcessCameraProvider cameraProvider;
    private boolean isPermissionMicro,isPermissionCamera;

    public static final int TYPE_FINGER_PRINT = 1;
    public static final int TYPE_SOUND = 2;
    public static final int TYPE_EYES = 3;

    public static final int TYPE_TRUE = 1;
    public static final int TYPE_FALSE = 0;
    public static final int TYPE_DEFAULT = -1;

    public static int mTypeScanner = TYPE_FINGER_PRINT;
    public static int mType = TYPE_DEFAULT;
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
        replaceFragment(new FingerPrintFragment());
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
    private void initListenerFooter(){
        mType = TYPE_DEFAULT;
        switch (mTypeScanner){
            case TYPE_FINGER_PRINT:
                mActivityScannerBinding.layoutScannerButtonLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isPermissionMicro){
                            replaceFragment(new SoundFragment());
                            mTypeScanner = TYPE_SOUND;
                            initUIFooter();
                            initListenerFooter();
                        } else {
                            requestPermissionMicro();
                        }
                    }
                });
                mActivityScannerBinding.layoutScannerButtonRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
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
                });
                break;
            case TYPE_SOUND:
                mActivityScannerBinding.layoutScannerButtonLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        replaceFragment(new FingerPrintFragment());
                        mTypeScanner = TYPE_FINGER_PRINT;
                        initUIFooter();
                        initListenerFooter();
                    }
                });
                mActivityScannerBinding.layoutScannerButtonRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
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
                });
                break;
            case TYPE_EYES:
                mActivityScannerBinding.layoutScannerButtonLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isPermissionMicro){
                            replaceFragment(new SoundFragment());
                            mTypeScanner = TYPE_SOUND;
                            initUIFooter();
                            initListenerFooter();
                        } else {
                            requestPermissionMicro();
                        }
                    }
                });
                mActivityScannerBinding.layoutScannerButtonRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        replaceFragment(new FingerPrintFragment());
                        mTypeScanner = TYPE_FINGER_PRINT;
                        initUIFooter();
                        initListenerFooter();
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
            case PermissionActivity.MICRO_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isPermissionMicro = true;
                    replaceFragment(new SoundFragment());
                    mTypeScanner = TYPE_SOUND;
                    initUIFooter();
                    initListenerFooter();
                } else {
                    Toast.makeText(this, getString(R.string.denied_mic), Toast.LENGTH_SHORT).show();
                }
                break;
            case PermissionActivity.CAMERA_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isPermissionCamera = true;
                    startCamera();
                    replaceFragment(new EyeFragment());
                    mTypeScanner = TYPE_EYES;
                    initUIFooter();
                    initListenerFooter();
                } else {
                    Toast.makeText(this, getString(R.string.denied_camera), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    private void requestPermissionMicro() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECORD_AUDIO}, PermissionActivity.MICRO_PERMISSION_REQUEST_CODE);
    }

    private void checkMicroPermission() {
        isPermissionMicro = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissionCamera() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},PermissionActivity.CAMERA_PERMISSION_REQUEST_CODE);
    }

    private void checkCameraPermission() {
        isPermissionCamera = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }




    }