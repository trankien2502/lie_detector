package com.vtdglobal.liedetector.activity;

import static android.graphics.Color.TRANSPARENT;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vtdglobal.liedetector.R;
import com.vtdglobal.liedetector.databinding.ActivityPermissionBinding;

public class PermissionActivity extends BaseActivity {
    ActivityPermissionBinding mActivityPermissionBinding;
    public static boolean isPermissionCamera ;
    public static boolean isPermissionMicro ;
    private static final String PREFS_NAME = "PermissionPrefs";
    private static final String PREF_KEY_DENY_COUNT_MIC = "denyCountMic";
    private static final String PREF_KEY_DENY_COUNT_CAM = "denyCountCam";

    public static final int MICRO_PERMISSION_REQUEST_CODE = 2;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityPermissionBinding = ActivityPermissionBinding.inflate(getLayoutInflater());
        setContentView(mActivityPermissionBinding.getRoot());


        if(checkAllPermission()) goToMainActivity();

        initUI();
        initListener();
    }

    private boolean checkAllPermission() {
        checkCameraPermission();
        checkMicroPermission();
        return  isPermissionCamera && isPermissionMicro;
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
                    mActivityPermissionBinding.swichMicro.setChecked(true);
                    SharedPreferences sharedPreferences =getApplicationContext().getSharedPreferences("permissionData", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("permissionMicro", true);
                    editor.apply();
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
                    mActivityPermissionBinding.swichCamera.setChecked(true);
                    SharedPreferences sharedPreferences =getApplicationContext().getSharedPreferences("permissionData", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("permissionCamera", true);
                    editor.apply();
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

    private void initListener() {
        mActivityPermissionBinding.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAllPermission()){
                    goToMainActivity();
                } else
                    showDialogWarning();

            }
        });
        mActivityPermissionBinding.swichCamera.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                checkCameraPermission();
                SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                int denyCount = prefs.getInt(PREF_KEY_DENY_COUNT_CAM, 0);
                if (!isPermissionCamera && denyCount<2) requestPermissionCamera();
                else if (!isPermissionCamera) showDialogPermissionGuide(CAMERA_PERMISSION_REQUEST_CODE);
                if (b) {
                    mActivityPermissionBinding.swichCamera.setChecked(isPermissionCamera);
                } else {
                    if (ActivityCompat.checkSelfPermission(PermissionActivity.this,
                            Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        mActivityPermissionBinding.swichCamera.setChecked(true);
                        Toast.makeText(PermissionActivity.this, getString(R.string.camera_permission_is_already_granted), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        mActivityPermissionBinding.swichMicro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                checkMicroPermission();
                SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                int denyCount = prefs.getInt(PREF_KEY_DENY_COUNT_MIC, 0);
                if (!isPermissionMicro && denyCount<2) requestPermissionMicro();
                else if (!isPermissionMicro) showDialogPermissionGuide(MICRO_PERMISSION_REQUEST_CODE);
                if (b) {
                    mActivityPermissionBinding.swichMicro.setChecked(isPermissionMicro);
                } else {
                    if (ActivityCompat.checkSelfPermission(PermissionActivity.this,
                            Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                        mActivityPermissionBinding.swichMicro.setChecked(true);
                        Toast.makeText(PermissionActivity.this, getString(R.string.microphone_permission_is_already_granted), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void goToMainActivity() {
        Intent intent = new Intent(PermissionActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
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

        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                goToMainActivity();
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

    private void initUI() {
        if (isPermissionMicro) {
            mActivityPermissionBinding.swichMicro.setChecked(true);
        }
        if (isPermissionCamera) {
            mActivityPermissionBinding.swichCamera.setChecked(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkMicroPermission();
        checkCameraPermission();
        initUI();
    }
}