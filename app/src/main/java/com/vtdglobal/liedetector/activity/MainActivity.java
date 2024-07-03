package com.vtdglobal.liedetector.activity;

import static android.graphics.Color.TRANSPARENT;
import static com.vtdglobal.liedetector.activity.PermissionActivity.CAMERA_PERMISSION_REQUEST_CODE;
import static com.vtdglobal.liedetector.activity.PermissionActivity.MICRO_PERMISSION_REQUEST_CODE;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.vtdglobal.liedetector.R;
import com.vtdglobal.liedetector.databinding.ActivityMainBinding;
import com.vtdglobal.liedetector.fragment.EyeFragment;
import com.vtdglobal.liedetector.util.CameraUtil;

public class MainActivity extends BaseActivity {
    ActivityMainBinding mActivityMainBinding;
    boolean isPermissionCamera;
    private static final String PREFS_NAME = "PermissionPrefs";
    private static final String PREF_KEY_DENY_COUNT_CAM = "denyCountCam";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mActivityMainBinding.getRoot());
        initListener();
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showDialogExit();
            }
        });

    }

    private void initListener() {
        mActivityMainBinding.layoutButtonScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ScannerActivity.class);
                startActivity(intent);
            }
        });
        mActivityMainBinding.layoutButtonSounds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SoundsActivity.class);
                startActivity(intent);
            }
        });
        mActivityMainBinding.layoutButtonGhost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCameraPermission();
                if (isPermissionCamera) {
                    startNextActivity(GhostActivity.class,null);
                } else {
                    requestPermissionCamera();
                }

            }
        });
    }

    private void showDialogExit() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_exit_app);

        Window window = dialog.getWindow();
        assert window != null;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(TRANSPARENT));

        dialog.setCancelable(false);
        // Set up the buttons
        View buttonOK = dialog.findViewById(R.id.btn_quit_app);
        View buttonCancel = dialog.findViewById(R.id.btn_cancel_quit_app);

        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finishAffinity();
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


}