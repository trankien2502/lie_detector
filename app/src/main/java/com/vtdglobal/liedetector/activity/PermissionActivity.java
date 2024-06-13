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
import android.os.Build;
import android.os.Bundle;
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

public class PermissionActivity extends AppCompatActivity {
    ActivityPermissionBinding mActivityPermissionBinding;
    public static boolean isPermissionCamera ;
    public static boolean isPermissionMicro ;
    public static boolean isPermissionNotification ;

    private static final int MICRO_PERMISSION_REQUEST_CODE = 2;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 3;
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 4;

//    SharedPreferences sharedPreferences =getApplicationContext().getSharedPreferences("permissionData", Context.MODE_PRIVATE);
//    SharedPreferences.Editor editor = sharedPreferences.edit();

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
        SharedPreferences sharedPreferences =getApplicationContext().getSharedPreferences("permissionData", Context.MODE_PRIVATE);
        isPermissionMicro = sharedPreferences.getBoolean("permissionMicro", Boolean.valueOf(String.valueOf(Context.MODE_PRIVATE)));
        isPermissionCamera = sharedPreferences.getBoolean("permissionCamera", Boolean.valueOf(String.valueOf(Context.MODE_PRIVATE)));
        isPermissionNotification = sharedPreferences.getBoolean("permissionNotification", Boolean.valueOf(String.valueOf(Context.MODE_PRIVATE)));
//        checkNotificationPermission();
//        checkCameraPermission();
//        checkMicroPermission();
        return isPermissionNotification && isPermissionCamera && isPermissionMicro;
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

    private void requestPermissionNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_PERMISSION_REQUEST_CODE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void checkNotificationPermission() {

        isPermissionNotification = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
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
                    Toast.makeText(this, "Microphone permission denied. Please enable it in settings.", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(this, "Camera permission denied. Please enable it in settings.", Toast.LENGTH_SHORT).show();
                }
                break;
            case NOTIFICATION_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isPermissionNotification = true;
                    mActivityPermissionBinding.swichNotification.setChecked(true);
                    SharedPreferences sharedPreferences =getApplicationContext().getSharedPreferences("permissionData", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("permissionNotification", true);
                    editor.apply();
                } else {
                    Toast.makeText(this, "Post notification permission denied. Please enable it in settings.", Toast.LENGTH_SHORT).show();
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
                if (!isPermissionCamera) requestPermissionCamera();
                if (b) {
                    mActivityPermissionBinding.swichCamera.setChecked(isPermissionCamera);
                } else {
                    if (ActivityCompat.checkSelfPermission(PermissionActivity.this,
                            Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        mActivityPermissionBinding.swichCamera.setChecked(true);
                        Toast.makeText(PermissionActivity.this, "Camera permission is already granted", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        mActivityPermissionBinding.swichMicro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                checkMicroPermission();
                if (!isPermissionMicro) requestPermissionMicro();
                if (b) {
                    mActivityPermissionBinding.swichMicro.setChecked(isPermissionMicro);
                } else {
                    if (ActivityCompat.checkSelfPermission(PermissionActivity.this,
                            Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                        mActivityPermissionBinding.swichMicro.setChecked(true);
                        Toast.makeText(PermissionActivity.this, "Microphone permission is already granted", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        mActivityPermissionBinding.swichNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    checkNotificationPermission();
                }
                if (!isPermissionNotification) requestPermissionNotification();
                if (b) {
                    mActivityPermissionBinding.swichNotification.setChecked(isPermissionNotification);
                } else {
                    if (ActivityCompat.checkSelfPermission(PermissionActivity.this,
                            Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                        mActivityPermissionBinding.swichNotification.setChecked(true);
                        Toast.makeText(PermissionActivity.this, "Post notification permission is already granted", Toast.LENGTH_SHORT).show();
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

    private void initUI() {
        if (isPermissionMicro) {
            mActivityPermissionBinding.swichMicro.setChecked(true);
        }
        if (isPermissionCamera) {
            mActivityPermissionBinding.swichCamera.setChecked(true);
        }
        if (isPermissionNotification) {
            mActivityPermissionBinding.swichNotification.setChecked(true);
        }
    }

}