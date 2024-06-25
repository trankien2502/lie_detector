package com.vtdglobal.liedetector.fragment;

import static android.graphics.Color.TRANSPARENT;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;
import com.vtdglobal.liedetector.R;
import com.vtdglobal.liedetector.activity.EyeScannerActivity;
import com.vtdglobal.liedetector.activity.FingerPrintScannerActivity;
import com.vtdglobal.liedetector.activity.MainActivity;
import com.vtdglobal.liedetector.activity.PermissionActivity;
import com.vtdglobal.liedetector.activity.ScannerActivity;
import com.vtdglobal.liedetector.activity.SettingActivity;
import com.vtdglobal.liedetector.activity.SoundScannerActivity;
import com.vtdglobal.liedetector.databinding.FragmentEyeBinding;

import java.io.IOException;
import java.util.Random;

import pl.droidsonroids.gif.GifDrawable;


public class EyeFragment extends Fragment {

   FragmentEyeBinding mFragmentEyeBinding;

//    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
//    private ProcessCameraProvider cameraProvider;
    private final Handler handler = new Handler();
    private Runnable runnableAnalyzing;
    private int  countdownAnalyzing = 5;
    private boolean  isResultScreen = false;
    MediaPlayer mediaPlayer;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentEyeBinding = FragmentEyeBinding.inflate(inflater,container,false);
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
                    mediaPlayer = MediaPlayer.create(getContext(), R.raw.get_result_sound);
                    mediaPlayer.start();
                    handler.removeCallbacks(runnableAnalyzing);
                    setVisibilityAnalyzingGone();
                    showDialogResult();
                }

            }
        };
        initUI();
        initListener();
        return mFragmentEyeBinding.getRoot();
    }

    private void initListener() {
        mFragmentEyeBinding.layoutEyePressButton2.setOnClickListener(new View.OnClickListener() {
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
        mFragmentEyeBinding.layoutEyeScanning.setVisibility(View.VISIBLE);
        mFragmentEyeBinding.imgEyeScanningLeft.setVisibility(View.VISIBLE);
        mFragmentEyeBinding.imgEyeScanningRight.setVisibility(View.VISIBLE);
        mFragmentEyeBinding.imgEyeScanning.setVisibility(View.VISIBLE);
        mFragmentEyeBinding.layoutEyePressButton.setVisibility(View.GONE);
        if (mediaPlayer!=null){
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(getContext(),R.raw.eyescansound_effect2);
        mediaPlayer.start();
        runAnimation();
    }
    private void setVisibilityAnalyzingGone(){
        mFragmentEyeBinding.layoutEyeScanning.setVisibility(View.GONE);
        mFragmentEyeBinding.imgEyeScanningLeft.setVisibility(View.GONE);
        mFragmentEyeBinding.imgEyeScanningRight.setVisibility(View.GONE);
        mFragmentEyeBinding.imgEyeScanning.setVisibility(View.GONE);
    }
    private void showDialogResult() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(getContext(), R.raw.get_result_sound);
        mediaPlayer.start();
        final Dialog dialog = new Dialog(getContext());
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
        mFragmentEyeBinding.layoutEyePressButton.setVisibility(View.VISIBLE);
        mFragmentEyeBinding.imgEyePressBorder.setImageResource(R.drawable.img_scanner_press_border_default);
        mFragmentEyeBinding.imgEyePress.setImageResource(R.drawable.img_eye_press_default);
        mFragmentEyeBinding.tvScannerPress.setVisibility(View.VISIBLE);
        mFragmentEyeBinding.tvScannerPress.setText("CLICK HERE");
        mFragmentEyeBinding.imgScannerDirectUp.setVisibility(View.VISIBLE);

        mFragmentEyeBinding.imgScannerResultBackgroundLight.setVisibility(View.GONE);
//        mFragmentEyeBinding.imgBackgroundHome.setVisibility(View.GONE);
        mFragmentEyeBinding.imgEyeScreenScanner.clearColorFilter();
        mFragmentEyeBinding.layoutEyeAnalyzing.setVisibility(View.GONE);
        mFragmentEyeBinding.layoutEyeScanning.setVisibility(View.VISIBLE);
//        mFragmentEyeBinding.previewView.setVisibility(View.VISIBLE);
        mFragmentEyeBinding.imgEyeScanningLeft.setVisibility(View.GONE);
        mFragmentEyeBinding.imgEyeScanningRight.setVisibility(View.GONE);
        mFragmentEyeBinding.imgEyeScanning.setVisibility(View.GONE);

    }

    private void setVisibilityResultVisible() {
        mFragmentEyeBinding.layoutEyePressButton.setVisibility(View.VISIBLE);
        mFragmentEyeBinding.layoutEyeAnalyzing.setVisibility(View.VISIBLE);
        mFragmentEyeBinding.imgEyeScreenScanner.setColorFilter(TRANSPARENT);
//        mFragmentEyeBinding.imgBackgroundHome.setVisibility(View.VISIBLE);
        mFragmentEyeBinding.layoutEyeScanning.setVisibility(View.GONE);
//        mFragmentEyeBinding.previewView.setVisibility(View.GONE);
    }

    private void initUIDefault() {
        setVisibilityResultGone();
        ScannerActivity.mType = ScannerActivity.TYPE_DEFAULT;
        ScannerActivity.initUIFooter();
            //startCamera();
    }

    private void initUIGetTruth() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(getContext(), R.raw.result_true);
        mediaPlayer.start();
        setVisibilityResultVisible();
        ScannerActivity.mType = ScannerActivity.TYPE_TRUE;
        ScannerActivity.initUIFooter();
        mFragmentEyeBinding.imgScannerResultBackgroundLight.setVisibility(View.VISIBLE);
        mFragmentEyeBinding.imgScannerResultBackgroundLight.setImageResource(R.drawable.img_scanner_result_background_light_truth);
        mFragmentEyeBinding.imgEyeScreenScanner.setImageResource(R.drawable.img_scanner_screen_scanner_truth);
        mFragmentEyeBinding.tvEyeScreenScanner3.setText("YOU TELL THE TRUTH");
        mFragmentEyeBinding.tvEyeScreenScanner3.setTextColor(getResources().getColor(R.color.truth));
        mFragmentEyeBinding.imgEyeAnalyzingTruth.setImageResource(R.drawable.img_scanner_analyzing_truth);
        mFragmentEyeBinding.imgEyeAnalyzingLiar.setImageResource(R.drawable.img_scanner_analyzing_none);
        mFragmentEyeBinding.layoutEyeAnalyzingResult.setVisibility(View.VISIBLE);
        mFragmentEyeBinding.tvEyeAnalyzingTruth.setTextColor(getResources().getColor(R.color.truth));
        mFragmentEyeBinding.tvEyeAnalyzingLiar.setTextColor(getResources().getColor(R.color.grayDefault));
        mFragmentEyeBinding.imgEyePressBorder.setImageResource(R.drawable.img_scanner_press_border_truth);
        mFragmentEyeBinding.imgEyePress.setImageResource(R.drawable.img_eye_press_truth);
        mFragmentEyeBinding.tvScannerPress.setVisibility(View.VISIBLE);
        mFragmentEyeBinding.tvScannerPress.setText("TRY AGAIN");
        mFragmentEyeBinding.imgScannerDirectUp.setVisibility(View.VISIBLE);
    }

    private void initUIGetLiar() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(getContext(), R.raw.result_lie);
        mediaPlayer.start();
        setVisibilityResultVisible();
        ScannerActivity.mType = ScannerActivity.TYPE_FALSE;
        ScannerActivity.initUIFooter();
        mFragmentEyeBinding.layoutEyePressButton.setVisibility(View.VISIBLE);
        mFragmentEyeBinding.imgScannerResultBackgroundLight.setVisibility(View.VISIBLE);
        mFragmentEyeBinding.imgScannerResultBackgroundLight.setImageResource(R.drawable.img_scanner_result_background_light_liar);
        mFragmentEyeBinding.imgEyeScreenScanner.setImageResource(R.drawable.img_scanner_screen_scanner_liar);
        mFragmentEyeBinding.tvEyeScreenScanner3.setText("YOU LIE");
        mFragmentEyeBinding.tvEyeScreenScanner3.setTextColor(getResources().getColor(R.color.liar));
        mFragmentEyeBinding.imgEyeAnalyzingTruth.setImageResource(R.drawable.img_scanner_analyzing_none);
        mFragmentEyeBinding.imgEyeAnalyzingLiar.setImageResource(R.drawable.img_scanner_analyzing_liar);
        mFragmentEyeBinding.layoutEyeAnalyzingResult.setVisibility(View.VISIBLE);
        mFragmentEyeBinding.tvEyeAnalyzingTruth.setTextColor(getResources().getColor(R.color.grayDefault));
        mFragmentEyeBinding.tvEyeAnalyzingLiar.setTextColor(getResources().getColor(R.color.liar));
        mFragmentEyeBinding.imgEyePressBorder.setImageResource(R.drawable.img_scanner_press_border_liar);
        mFragmentEyeBinding.imgEyePress.setImageResource(R.drawable.img_eye_press_liar);
        mFragmentEyeBinding.tvScannerPress.setVisibility(View.VISIBLE);
        mFragmentEyeBinding.tvScannerPress.setText("TRY AGAIN");
        mFragmentEyeBinding.imgScannerDirectUp.setVisibility(View.VISIBLE);
    }


    private void runAnimation() {
        ViewTreeObserver viewTreeObserver = mFragmentEyeBinding.layoutEyeScanning.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int parentHeight = mFragmentEyeBinding.layoutLinearEyeScanning.getHeight();
                int pressingLightHeight = mFragmentEyeBinding.imgEyeScanning.getHeight();
                ValueAnimator animator = ValueAnimator.ofFloat(0f, parentHeight - pressingLightHeight);
                animator.setDuration(2500);
                animator.setInterpolator(new LinearInterpolator());
                animator.setRepeatCount(5000);
                animator.setRepeatMode(ValueAnimator.REVERSE);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                        float value = (float) valueAnimator.getAnimatedValue();
                        mFragmentEyeBinding.imgEyeScanning.setTranslationY(value);
                    }
                });
                animator.start();
                mFragmentEyeBinding.layoutEyeScanning.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer!=null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (ScannerActivity.cameraProvider != null) {
            ScannerActivity.cameraProvider.unbindAll();
        }
        handler.removeCallbacks(runnableAnalyzing);
    }
}