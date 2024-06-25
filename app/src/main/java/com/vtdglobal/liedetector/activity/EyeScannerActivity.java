package com.vtdglobal.liedetector.activity;

import static android.graphics.Color.TRANSPARENT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;
import com.vtdglobal.liedetector.R;
import com.vtdglobal.liedetector.databinding.ActivityEyeScannerBinding;

import java.util.Random;

public class EyeScannerActivity extends BaseActivity {
    ActivityEyeScannerBinding mActivityEyeScannerBinding;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ProcessCameraProvider cameraProvider;
    private final Handler handler = new Handler();
    private Runnable runnableAnalyzing;
    private int  countdownAnalyzing = 5;
    private boolean  isResultScreen = false;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityEyeScannerBinding = ActivityEyeScannerBinding.inflate(getLayoutInflater());
        setContentView(mActivityEyeScannerBinding.getRoot());
        runnableAnalyzing = new Runnable() {
            @Override
            public void run() {
                countdownAnalyzing--;
                if (countdownAnalyzing > 0) {
                    handler.postDelayed(this, 1000);
                } else {
                    if (mediaPlayer != null) {
                        mediaPlayer.release();
                    }
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.get_result_sound);
                    mediaPlayer.start();
                    handler.removeCallbacks(runnableAnalyzing);
                    setVisibilityAnalyzingGone();
                    showDialogResult();
                }

            }
        };
        initUI();
        initListenerHeader();
        initListener();
    }




    private void initListener() {
        mActivityEyeScannerBinding.layoutEyeButtonLeft.setOnClickListener(view -> goToSoundScannerActivity());
        mActivityEyeScannerBinding.layoutEyeButtonRight.setOnClickListener(view -> goToFingerPrintScannerActivity());
        mActivityEyeScannerBinding.layoutEyeButtonCenter.setOnClickListener(view -> setVisibilityResultGone());
        mActivityEyeScannerBinding.layoutEyePressButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isResultScreen){
                    initUIDefault();
                    isResultScreen = false;
                } else {
                    countdownAnalyzing = 5;
                    handler.postDelayed(runnableAnalyzing,1000);
                    setVisibilityAnalyzingVisible();
                }
            }
        });
    }

    private void initUI() {
        initUIDefault();
    }
    private void setVisibilityAnalyzingVisible(){
        mActivityEyeScannerBinding.layoutEyeScanning.setVisibility(View.VISIBLE);
        mActivityEyeScannerBinding.imgEyeScanningLeft.setVisibility(View.VISIBLE);
        mActivityEyeScannerBinding.imgEyeScanningRight.setVisibility(View.VISIBLE);
        mActivityEyeScannerBinding.imgEyeScanning.setVisibility(View.VISIBLE);
        mActivityEyeScannerBinding.layoutEyePressButton.setVisibility(View.GONE);
        if (mediaPlayer!=null){
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.eyescansound_effect2);
        mediaPlayer.start();
        runAnimation();
    }
    private void setVisibilityAnalyzingGone(){
        mActivityEyeScannerBinding.layoutEyeScanning.setVisibility(View.GONE);
        mActivityEyeScannerBinding.imgEyeScanningLeft.setVisibility(View.GONE);
        mActivityEyeScannerBinding.imgEyeScanningRight.setVisibility(View.GONE);
        mActivityEyeScannerBinding.imgEyeScanning.setVisibility(View.GONE);
    }
    private void showDialogResult() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.get_result_sound);
        mediaPlayer.start();
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_scanner_result);

        Window window = dialog.getWindow();
        assert window != null;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(TRANSPARENT));

        dialog.setCancelable(false);
        // Set up the buttons
        ImageView buttonView = dialog.findViewById(R.id.btn_scanner_view_result);


        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.release();
                mediaPlayer = null;
                isResultScreen = true;
                getResultScanner();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void getResultScanner() {
        Random random = new Random();
        boolean isTruth = random.nextBoolean();
        if (isTruth) {
            initUIGetTruth();
        } else {
            initUIGetLiar();
        }
    }

    private void setVisibilityResultGone() {
        mActivityEyeScannerBinding.layoutEyePressButton.setVisibility(View.VISIBLE);
        mActivityEyeScannerBinding.imgEyePressBorder.setImageResource(R.drawable.img_scanner_press_border_default);
        mActivityEyeScannerBinding.imgEyePress.setImageResource(R.drawable.img_eye_press_default);
        mActivityEyeScannerBinding.tvScannerPress.setVisibility(View.VISIBLE);
        mActivityEyeScannerBinding.tvScannerPress.setText("CLICK HERE");
        mActivityEyeScannerBinding.imgScannerDirectUp.setVisibility(View.VISIBLE);
        mActivityEyeScannerBinding.imgScannerEyeBottomButton.setImageResource(R.drawable.img_eye_bottom_button_default);

        mActivityEyeScannerBinding.imgScannerResultBackgroundLight.setVisibility(View.GONE);
        mActivityEyeScannerBinding.imgBackgroundHome.setVisibility(View.GONE);
        mActivityEyeScannerBinding.imgEyeScreenScanner.clearColorFilter();
        mActivityEyeScannerBinding.layoutEyeAnalyzing.setVisibility(View.GONE);
        mActivityEyeScannerBinding.layoutEyeScanning.setVisibility(View.VISIBLE);
        mActivityEyeScannerBinding.previewView.setVisibility(View.VISIBLE);
        mActivityEyeScannerBinding.imgEyeScanningLeft.setVisibility(View.GONE);
        mActivityEyeScannerBinding.imgEyeScanningRight.setVisibility(View.GONE);
        mActivityEyeScannerBinding.imgEyeScanning.setVisibility(View.GONE);

    }

    private void setVisibilityResultVisible() {
        mActivityEyeScannerBinding.layoutEyePressButton.setVisibility(View.VISIBLE);
        mActivityEyeScannerBinding.layoutEyeAnalyzing.setVisibility(View.VISIBLE);
        mActivityEyeScannerBinding.imgEyeScreenScanner.setColorFilter(TRANSPARENT);
        mActivityEyeScannerBinding.imgBackgroundHome.setVisibility(View.VISIBLE);
        mActivityEyeScannerBinding.layoutEyeScanning.setVisibility(View.GONE);
        mActivityEyeScannerBinding.previewView.setVisibility(View.GONE);
    }

    private void initUIDefault() {
        if (ContextCompat.checkSelfPermission(EyeScannerActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(EyeScannerActivity.this, new String[]{Manifest.permission.CAMERA}, PermissionActivity.CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            setVisibilityResultGone();
            startCamera();
        }

    }

    private void initUIGetTruth() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.result_true);
        mediaPlayer.start();
        setVisibilityResultVisible();

        mActivityEyeScannerBinding.imgScannerResultBackgroundLight.setVisibility(View.VISIBLE);
        mActivityEyeScannerBinding.imgScannerResultBackgroundLight.setImageResource(R.drawable.img_scanner_result_background_light_truth);
        mActivityEyeScannerBinding.imgEyeScreenScanner.setImageResource(R.drawable.img_scanner_screen_scanner_truth);
        mActivityEyeScannerBinding.tvEyeScreenScanner3.setText("YOU TELL THE TRUTH");
        mActivityEyeScannerBinding.tvEyeScreenScanner3.setTextColor(getResources().getColor(R.color.truth));
        mActivityEyeScannerBinding.imgEyeAnalyzingTruth.setImageResource(R.drawable.img_scanner_analyzing_truth);
        mActivityEyeScannerBinding.imgEyeAnalyzingLiar.setImageResource(R.drawable.img_scanner_analyzing_none);
        mActivityEyeScannerBinding.layoutEyeAnalyzingResult.setVisibility(View.VISIBLE);
        mActivityEyeScannerBinding.tvEyeAnalyzingTruth.setTextColor(getResources().getColor(R.color.truth));
        mActivityEyeScannerBinding.tvEyeAnalyzingLiar.setTextColor(getResources().getColor(R.color.grayDefault));
        mActivityEyeScannerBinding.imgEyePressBorder.setImageResource(R.drawable.img_scanner_press_border_truth);
        mActivityEyeScannerBinding.imgEyePress.setImageResource(R.drawable.img_eye_press_truth);
        mActivityEyeScannerBinding.tvScannerPress.setVisibility(View.VISIBLE);
        mActivityEyeScannerBinding.tvScannerPress.setText("TRY AGAIN");
        mActivityEyeScannerBinding.imgScannerDirectUp.setVisibility(View.VISIBLE);
        mActivityEyeScannerBinding.imgScannerEyeBottomButton.setImageResource(R.drawable.img_eye_bottom_button_truth);
    }

    private void initUIGetLiar() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.result_lie);
        mediaPlayer.start();
        setVisibilityResultVisible();

        mActivityEyeScannerBinding.layoutEyePressButton.setVisibility(View.VISIBLE);
        mActivityEyeScannerBinding.imgScannerResultBackgroundLight.setVisibility(View.VISIBLE);
        mActivityEyeScannerBinding.imgScannerResultBackgroundLight.setImageResource(R.drawable.img_scanner_result_background_light_liar);
        mActivityEyeScannerBinding.imgEyeScreenScanner.setImageResource(R.drawable.img_scanner_screen_scanner_liar);
        mActivityEyeScannerBinding.tvEyeScreenScanner3.setText("YOU LIE");
        mActivityEyeScannerBinding.tvEyeScreenScanner3.setTextColor(getResources().getColor(R.color.liar));
        mActivityEyeScannerBinding.imgEyeAnalyzingTruth.setImageResource(R.drawable.img_scanner_analyzing_none);
        mActivityEyeScannerBinding.imgEyeAnalyzingLiar.setImageResource(R.drawable.img_scanner_analyzing_liar);
        mActivityEyeScannerBinding.layoutEyeAnalyzingResult.setVisibility(View.VISIBLE);
        mActivityEyeScannerBinding.tvEyeAnalyzingTruth.setTextColor(getResources().getColor(R.color.grayDefault));
        mActivityEyeScannerBinding.tvEyeAnalyzingLiar.setTextColor(getResources().getColor(R.color.liar));
        mActivityEyeScannerBinding.imgEyePressBorder.setImageResource(R.drawable.img_scanner_press_border_liar);
        mActivityEyeScannerBinding.imgEyePress.setImageResource(R.drawable.img_eye_press_liar);
        mActivityEyeScannerBinding.tvScannerPress.setVisibility(View.VISIBLE);
        mActivityEyeScannerBinding.tvScannerPress.setText("TRY AGAIN");
        mActivityEyeScannerBinding.imgScannerDirectUp.setVisibility(View.VISIBLE);
        mActivityEyeScannerBinding.imgScannerEyeBottomButton.setImageResource(R.drawable.img_eye_bottom_button_liar);
    }

    private void initListenerHeader() {
        mActivityEyeScannerBinding.header.imgLeft.setOnClickListener(view -> finish());
        mActivityEyeScannerBinding.header.imgSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EyeScannerActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
    }

    private void runAnimation() {
        ViewTreeObserver viewTreeObserver = mActivityEyeScannerBinding.layoutEyeScanning.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int parentHeight = mActivityEyeScannerBinding.layoutLinearEyeScanning.getHeight();
                int pressingLightHeight = mActivityEyeScannerBinding.imgEyeScanning.getHeight();
                ValueAnimator animator = ValueAnimator.ofFloat(0f, parentHeight - pressingLightHeight);
                animator.setDuration(2500);
                animator.setInterpolator(new LinearInterpolator());
                animator.setRepeatCount(5000);
                animator.setRepeatMode(ValueAnimator.REVERSE);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                        float value = (float) valueAnimator.getAnimatedValue();
                        mActivityEyeScannerBinding.imgEyeScanning.setTranslationY(value);
                    }
                });
                animator.start();
                mActivityEyeScannerBinding.layoutEyeScanning.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    private void bindPreview(ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build();
        preview.setSurfaceProvider(mActivityEyeScannerBinding.previewView.getSurfaceProvider());

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
        if (requestCode == PermissionActivity.CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                Toast.makeText(this, "Camera permission denied. Please enable it in settings.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EyeScannerActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private void goToFingerPrintScannerActivity() {
        Intent intent = new Intent(EyeScannerActivity.this, FingerPrintScannerActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToSoundScannerActivity() {
        Intent intent = new Intent(EyeScannerActivity.this, SoundScannerActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer!=null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (cameraProvider != null) {
            cameraProvider.unbindAll();
        }
        handler.removeCallbacks(runnableAnalyzing);
    }
}