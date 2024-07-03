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
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.liedetector.test.prank.liescanner.truthtest.R;
import com.liedetector.test.prank.liescanner.truthtest.base.BaseActivity;
import com.liedetector.test.prank.liescanner.truthtest.databinding.ActivityPermissionBinding;
import com.liedetector.test.prank.liescanner.truthtest.ui.main.MainActivity;
import com.liedetector.test.prank.liescanner.truthtest.util.EventTracking;

public class PermissionActivity extends BaseActivity<ActivityPermissionBinding> {
    public static boolean isPermissionCamera;
    public static boolean isPermissionMicro;
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
    }

    @Override
    public void bindView() {
        binding.btnContinue.setOnClickListener(view -> {
            if (checkAllPermission()) {
                startNextActivity();
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

        buttonOK.setOnClickListener(v -> {
            dialog.dismiss();
            startNextActivity();
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
        buttonOK.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
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
    }
}