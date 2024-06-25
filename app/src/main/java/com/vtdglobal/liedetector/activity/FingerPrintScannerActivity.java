package com.vtdglobal.liedetector.activity;

import static android.graphics.Color.TRANSPARENT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.vtdglobal.liedetector.R;
import com.vtdglobal.liedetector.databinding.ActivityFingerPrintScannerBinding;

import java.util.Random;

public class FingerPrintScannerActivity extends BaseActivity {
    ActivityFingerPrintScannerBinding mActivityFingerPrintScannerBinding;
    private final Handler handler = new Handler();
    private Runnable runnablePressing, runnableAnalyzing;
    private int countdownPressing = 5, countdownAnalyzing = 6;
    private boolean isButtonPressed = false,isAnalyzing = false;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityFingerPrintScannerBinding = ActivityFingerPrintScannerBinding.inflate(getLayoutInflater());
        setContentView(mActivityFingerPrintScannerBinding.getRoot());
        runnablePressing = new Runnable() {
            @Override
            public void run() {
                if (isButtonPressed) {
                    initUIDefault();
                    countdownPressing--;
                    mActivityFingerPrintScannerBinding.imgFingerPrintPressing.setColorFilter(Color.TRANSPARENT);
                    mActivityFingerPrintScannerBinding.tvFingerPrintScreenScanner1.setVisibility(View.GONE);
                    mActivityFingerPrintScannerBinding.layoutFingerPrintScanningProgress.setVisibility(View.VISIBLE);
                    mActivityFingerPrintScannerBinding.tvScannerPress.setVisibility(View.GONE);
                    mActivityFingerPrintScannerBinding.imgScannerDirectUp.setVisibility(View.GONE);
                    mActivityFingerPrintScannerBinding.tvFingerPrintScreenScanner2.setText("WAIT " + countdownPressing + " SECONDS TO SCAN");
                    if (countdownPressing > 0) {
                        handler.postDelayed(this, 1000);
                    } else {
                        mActivityFingerPrintScannerBinding.tvFingerPrintScreenScanner1.setVisibility(View.GONE);
                        mActivityFingerPrintScannerBinding.layoutFingerPrintScanningProgress.setVisibility(View.GONE);
                        mActivityFingerPrintScannerBinding.imgFingerPrintPressing.clearColorFilter();
                        mActivityFingerPrintScannerBinding.layoutFingerPrintAnalyzing.setVisibility(View.VISIBLE);
                        Random random = new Random();
                        countdownAnalyzing = 6 + random.nextInt(10);

                        if (mediaPlayer!=null){
                            mediaPlayer.release();
                            mediaPlayer=null;
                        }
                        mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.analyzing_sound);
                        mediaPlayer.start();
                        isAnalyzing = true;
                        handler.postDelayed(runnableAnalyzing, 500);
                    }
                }
            }
        };
        runnableAnalyzing = new Runnable() {
            @Override
            public void run() {
                countdownAnalyzing--;
                switch (countdownAnalyzing%4) {
                    case 0:
                        mActivityFingerPrintScannerBinding.imgFingerPrintAnalyzingTruth.setImageResource(R.drawable.img_scanner_analyzing_none);
                        mActivityFingerPrintScannerBinding.imgFingerPrintAnalyzingLiar.setImageResource(R.drawable.img_scanner_analyzing_liar);
                        mActivityFingerPrintScannerBinding.tvFingerPrintScreenScanner3.setText("Analyzing...");
                        break;
                    case 1:
                        mActivityFingerPrintScannerBinding.imgFingerPrintAnalyzingTruth.setImageResource(R.drawable.img_scanner_analyzing_truth);
                        mActivityFingerPrintScannerBinding.imgFingerPrintAnalyzingLiar.setImageResource(R.drawable.img_scanner_analyzing_none);
                        mActivityFingerPrintScannerBinding.tvFingerPrintScreenScanner3.setText("Analyzing..");
                        break;
                    case 2:
                        mActivityFingerPrintScannerBinding.imgFingerPrintAnalyzingTruth.setImageResource(R.drawable.img_scanner_analyzing_none);
                        mActivityFingerPrintScannerBinding.imgFingerPrintAnalyzingLiar.setImageResource(R.drawable.img_scanner_analyzing_liar);
                        mActivityFingerPrintScannerBinding.tvFingerPrintScreenScanner3.setText("Analyzing.");
                        break;
                    case 3:
                        mActivityFingerPrintScannerBinding.imgFingerPrintAnalyzingTruth.setImageResource(R.drawable.img_scanner_analyzing_truth);
                        mActivityFingerPrintScannerBinding.imgFingerPrintAnalyzingLiar.setImageResource(R.drawable.img_scanner_analyzing_none);
                        mActivityFingerPrintScannerBinding.tvFingerPrintScreenScanner3.setText("Analyzing");
                        break;
                }
                if (countdownAnalyzing==0){
                    mActivityFingerPrintScannerBinding.tvFingerPrintScreenScanner3.setText("The Result is ready NOW");
                    handler.removeCallbacks(runnableAnalyzing);
                    showDialogResult();
                }else
                    handler.postDelayed(this, 500);

            }
        };
        initUI();
        initListenerHeader();
        initListener();
    }
    @SuppressLint("ClickableViewAccessibility")
    private void initListener() {
        mActivityFingerPrintScannerBinding.layoutFingerPrintButtonLeft.setOnClickListener(view -> goToSoundScannerActivity());
        mActivityFingerPrintScannerBinding.layoutFingerPrintButtonRight.setOnClickListener(view -> goToEyesScannerActivity());
        mActivityFingerPrintScannerBinding.layoutFingerPrintButtonCenter.setOnClickListener(view -> initUI());
        mActivityFingerPrintScannerBinding.layoutFingerPrintPressButton2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(isAnalyzing) return false;
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isButtonPressed = true;
                        countdownPressing = 5 ;
                        mActivityFingerPrintScannerBinding.imgFingerPrintPressing.setColorFilter(Color.TRANSPARENT);
                        mActivityFingerPrintScannerBinding.tvFingerPrintScreenScanner1.setVisibility(View.GONE);
                        mActivityFingerPrintScannerBinding.layoutFingerPrintScanningProgress.setVisibility(View.VISIBLE);
                        mActivityFingerPrintScannerBinding.tvScannerPress.setVisibility(View.GONE);
                        mActivityFingerPrintScannerBinding.imgScannerDirectUp.setVisibility(View.GONE);
                        mActivityFingerPrintScannerBinding.tvFingerPrintScreenScanner2.setText("WAIT " + countdownPressing + " SECONDS TO SCAN");
                        handler.postDelayed(runnablePressing, 1000);
                        runAnimation();
                        if (mediaPlayer!=null){
                            mediaPlayer.release();
                            mediaPlayer = null;
                        }
                        mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.scansound_effect2);
                        mediaPlayer.start();
                        mActivityFingerPrintScannerBinding.tvFingerPrintScreenScanner2.setText("WAIT " + countdownPressing + " SECONDS TO SCAN");
                        return true;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        isButtonPressed = false;
                        if (countdownPressing > 0) {
                            initUIDefault();
                            if (mediaPlayer!=null){
                                mediaPlayer.release();
                                mediaPlayer = null;
                            }
                            mActivityFingerPrintScannerBinding.tvScannerPress.setVisibility(View.VISIBLE);
                            mActivityFingerPrintScannerBinding.imgScannerDirectUp.setVisibility(View.VISIBLE);

                        }
                        handler.removeCallbacks(runnablePressing);
                        mActivityFingerPrintScannerBinding.layoutFingerPrintScanningProgress.setVisibility(View.GONE);
                        mActivityFingerPrintScannerBinding.imgFingerPrintPressing.clearColorFilter();
                        return true;
                }
                return false;
            }
        });
    }

    private void showDialogResult() {
        isAnalyzing = false;
        handler.removeCallbacks(runnableAnalyzing);
        if (mediaPlayer!=null){
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.get_result_sound);
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

    private void initUIDefault() {
        mActivityFingerPrintScannerBinding.tvFingerPrintScreenScanner1.setVisibility(View.VISIBLE);
        mActivityFingerPrintScannerBinding.imgScannerResultBackgroundLight.setVisibility(View.GONE);
        mActivityFingerPrintScannerBinding.imgFingerPrintScreenScanner.setImageResource(R.drawable.img_scanner_screen_scanner_default);
        mActivityFingerPrintScannerBinding.layoutFingerPrintAnalyzing.setVisibility(View.GONE);
        mActivityFingerPrintScannerBinding.layoutFingerPrintAnalyzingResult.setVisibility(View.GONE);
        mActivityFingerPrintScannerBinding.imgFingerPrintPressBorder.setImageResource(R.drawable.img_scanner_press_border_default);
        mActivityFingerPrintScannerBinding.imgFingerPrintPress.setImageResource(R.drawable.img_finger_print_press_default);
        mActivityFingerPrintScannerBinding.tvFingerPrintScreenScanner3.setText("Analyzing");
        mActivityFingerPrintScannerBinding.tvFingerPrintScreenScanner3.setTextColor(getResources().getColor(R.color.white));
        mActivityFingerPrintScannerBinding.tvScannerPress.setText("PRESS HERE");
        mActivityFingerPrintScannerBinding.imgScannerFingerPrintBottomButton.setImageResource(R.drawable.img_finger_print_bottom_button_default);
    }

    private void initUIGetTruth() {
        if (mediaPlayer!=null){
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.result_true);
        mediaPlayer.start();
        mActivityFingerPrintScannerBinding.imgScannerResultBackgroundLight.setVisibility(View.VISIBLE);
        mActivityFingerPrintScannerBinding.imgScannerResultBackgroundLight.setImageResource(R.drawable.img_scanner_result_background_light_truth);
        mActivityFingerPrintScannerBinding.imgFingerPrintScreenScanner.setImageResource(R.drawable.img_scanner_screen_scanner_truth);
        mActivityFingerPrintScannerBinding.tvFingerPrintScreenScanner3.setText("YOU TELL THE TRUTH");
        mActivityFingerPrintScannerBinding.tvFingerPrintScreenScanner3.setTextColor(getResources().getColor(R.color.truth));
        mActivityFingerPrintScannerBinding.imgFingerPrintAnalyzingTruth.setImageResource(R.drawable.img_scanner_analyzing_truth);
        mActivityFingerPrintScannerBinding.imgFingerPrintAnalyzingLiar.setImageResource(R.drawable.img_scanner_analyzing_none);
        mActivityFingerPrintScannerBinding.layoutFingerPrintAnalyzingResult.setVisibility(View.VISIBLE);
        mActivityFingerPrintScannerBinding.tvFingerPrintAnalyzingTruth.setTextColor(getResources().getColor(R.color.truth));
        mActivityFingerPrintScannerBinding.tvFingerPrintAnalyzingLiar.setTextColor(getResources().getColor(R.color.grayDefault));
        mActivityFingerPrintScannerBinding.imgFingerPrintPressBorder.setImageResource(R.drawable.img_scanner_press_border_truth);
        mActivityFingerPrintScannerBinding.imgFingerPrintPress.setImageResource(R.drawable.img_finger_print_press_truth);
        mActivityFingerPrintScannerBinding.tvScannerPress.setVisibility(View.VISIBLE);
        mActivityFingerPrintScannerBinding.tvScannerPress.setText("TRY AGAIN");
        mActivityFingerPrintScannerBinding.imgScannerDirectUp.setVisibility(View.VISIBLE);
        mActivityFingerPrintScannerBinding.imgScannerFingerPrintBottomButton.setImageResource(R.drawable.img_finger_print_bottom_button_truth);
    }

    private void initUIGetLiar() {
        if (mediaPlayer!=null){
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.result_lie);
        mediaPlayer.start();
        mActivityFingerPrintScannerBinding.imgScannerResultBackgroundLight.setVisibility(View.VISIBLE);
        mActivityFingerPrintScannerBinding.imgScannerResultBackgroundLight.setImageResource(R.drawable.img_scanner_result_background_light_liar);
        mActivityFingerPrintScannerBinding.imgFingerPrintScreenScanner.setImageResource(R.drawable.img_scanner_screen_scanner_liar);
        mActivityFingerPrintScannerBinding.tvFingerPrintScreenScanner3.setText("YOU LIE");
        mActivityFingerPrintScannerBinding.tvFingerPrintScreenScanner3.setTextColor(getResources().getColor(R.color.liar));
        mActivityFingerPrintScannerBinding.imgFingerPrintAnalyzingTruth.setImageResource(R.drawable.img_scanner_analyzing_none);
        mActivityFingerPrintScannerBinding.imgFingerPrintAnalyzingLiar.setImageResource(R.drawable.img_scanner_analyzing_liar);
        mActivityFingerPrintScannerBinding.layoutFingerPrintAnalyzingResult.setVisibility(View.VISIBLE);
        mActivityFingerPrintScannerBinding.tvFingerPrintAnalyzingTruth.setTextColor(getResources().getColor(R.color.grayDefault));
        mActivityFingerPrintScannerBinding.tvFingerPrintAnalyzingLiar.setTextColor(getResources().getColor(R.color.liar));
        mActivityFingerPrintScannerBinding.imgFingerPrintPressBorder.setImageResource(R.drawable.img_scanner_press_border_liar);
        mActivityFingerPrintScannerBinding.imgFingerPrintPress.setImageResource(R.drawable.img_finger_print_press_liar);
        mActivityFingerPrintScannerBinding.tvScannerPress.setVisibility(View.VISIBLE);
        mActivityFingerPrintScannerBinding.tvScannerPress.setText("TRY AGAIN");
        mActivityFingerPrintScannerBinding.imgScannerDirectUp.setVisibility(View.VISIBLE);
        mActivityFingerPrintScannerBinding.imgScannerFingerPrintBottomButton.setImageResource(R.drawable.img_finger_print_bottom_button_liar);
    }

    private void initListenerHeader() {
        mActivityFingerPrintScannerBinding.header.imgLeft.setOnClickListener(view -> finish());
        mActivityFingerPrintScannerBinding.header.imgSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FingerPrintScannerActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
    }



    private void goToEyesScannerActivity() {
        Intent intent = new Intent(FingerPrintScannerActivity.this, EyeScannerActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToSoundScannerActivity() {
        Intent intent = new Intent(FingerPrintScannerActivity.this, SoundScannerActivity.class);
        startActivity(intent);
        finish();
    }

    private void initUI() {
        initUIDefault();
    }
    private void runAnimation(){
        ViewTreeObserver viewTreeObserver = mActivityFingerPrintScannerBinding.layoutFingerPrintPressButton2.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int parentHeight = mActivityFingerPrintScannerBinding.layoutFingerPrintPressButton2.getHeight();
                int pressingLightHeight = mActivityFingerPrintScannerBinding.imgFingerPrintPressing.getHeight();
                ValueAnimator animator = ValueAnimator.ofFloat(0f,parentHeight-pressingLightHeight);
                animator.setDuration(800);
                animator.setInterpolator(new LinearInterpolator());
                animator.setRepeatCount(ValueAnimator.INFINITE);
                animator.setRepeatMode(ValueAnimator.REVERSE);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                        float value = (float) valueAnimator.getAnimatedValue();
                        mActivityFingerPrintScannerBinding.imgFingerPrintPressing.setTranslationY(value);
                    }
                });
                animator.start();
                mActivityFingerPrintScannerBinding.layoutFingerPrintPressButton2.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer!=null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
        handler.removeCallbacks(runnableAnalyzing);
        handler.removeCallbacks(runnablePressing);

    }
}