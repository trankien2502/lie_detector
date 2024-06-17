package com.vtdglobal.liedetector.activity;

import static android.graphics.Color.TRANSPARENT;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.vtdglobal.liedetector.R;
import com.vtdglobal.liedetector.databinding.ActivitySoundScannerBinding;
import com.vtdglobal.liedetector.databinding.ActivitySoundsBinding;

import java.util.Random;

public class SoundScannerActivity extends AppCompatActivity {
    ActivitySoundScannerBinding mActivitySoundScannerBinding;
    private final Handler handler = new Handler();
    private Runnable runnablePressing, runnableAnalyzing;
    private int countdownPressing = 100, countdownAnalyzing = 0;
    private boolean isButtonPressed = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivitySoundScannerBinding = ActivitySoundScannerBinding.inflate(getLayoutInflater());
        setContentView(mActivitySoundScannerBinding.getRoot());
        runnablePressing = new Runnable() {
            @Override
            public void run() {
                if (isButtonPressed) {
                    initUIDefault();
                    mActivitySoundScannerBinding.tvSoundScreenScanner1.setVisibility(View.GONE);
                    mActivitySoundScannerBinding.layoutSoundScanningProgress.setVisibility(View.VISIBLE);
                    mActivitySoundScannerBinding.tvScannerPress.setVisibility(View.GONE);
                    mActivitySoundScannerBinding.imgScannerDirectUp.setVisibility(View.GONE);
                    countdownPressing--;
                    if (countdownPressing>0){
                        handler.postDelayed(this, 500);
                    }
                    else {
                        mActivitySoundScannerBinding.tvSoundScreenScanner1.setVisibility(View.GONE);
                        mActivitySoundScannerBinding.layoutSoundScanningProgress.setVisibility(View.GONE);
                        mActivitySoundScannerBinding.layoutSoundAnalyzing.setVisibility(View.VISIBLE);
                        countdownAnalyzing = 0;
                        handler.postDelayed(runnableAnalyzing, 500);
                    }
                }
            }
        };
        runnableAnalyzing = new Runnable() {
            @Override
            public void run() {
                countdownAnalyzing++;
                switch (countdownAnalyzing) {
                    case 1:
                        mActivitySoundScannerBinding.imgSoundAnalyzingTruth.setImageResource(R.drawable.img_scanner_analyzing_none);
                        mActivitySoundScannerBinding.imgSoundAnalyzingLiar.setImageResource(R.drawable.img_scanner_analyzing_liar);
                        mActivitySoundScannerBinding.tvSoundScreenScanner3.setText("Analyzing");
                        break;
                    case 2:
                        mActivitySoundScannerBinding.imgSoundAnalyzingTruth.setImageResource(R.drawable.img_scanner_analyzing_truth);
                        mActivitySoundScannerBinding.imgSoundAnalyzingLiar.setImageResource(R.drawable.img_scanner_analyzing_none);
                        mActivitySoundScannerBinding.tvSoundScreenScanner3.setText("Analyzing.");
                        break;
                    case 3:
                        mActivitySoundScannerBinding.imgSoundAnalyzingTruth.setImageResource(R.drawable.img_scanner_analyzing_none);
                        mActivitySoundScannerBinding.imgSoundAnalyzingLiar.setImageResource(R.drawable.img_scanner_analyzing_liar);
                        mActivitySoundScannerBinding.tvSoundScreenScanner3.setText("Analyzing..");
                        break;
                    case 4:
                        mActivitySoundScannerBinding.imgSoundAnalyzingTruth.setImageResource(R.drawable.img_scanner_analyzing_truth);
                        mActivitySoundScannerBinding.imgSoundAnalyzingLiar.setImageResource(R.drawable.img_scanner_analyzing_none);
                        mActivitySoundScannerBinding.tvSoundScreenScanner3.setText("Analyzing...");
                        break;
                    case 5:
                        mActivitySoundScannerBinding.imgSoundAnalyzingTruth.setImageResource(R.drawable.img_scanner_analyzing_none);
                        mActivitySoundScannerBinding.imgSoundAnalyzingLiar.setImageResource(R.drawable.img_scanner_analyzing_liar);
                        mActivitySoundScannerBinding.tvSoundScreenScanner3.setText("Analyzing...");
                        break;
                    case 6:
                        mActivitySoundScannerBinding.imgSoundAnalyzingTruth.setImageResource(R.drawable.img_scanner_analyzing_none);
                        mActivitySoundScannerBinding.imgSoundAnalyzingLiar.setImageResource(R.drawable.img_scanner_analyzing_liar);
                        mActivitySoundScannerBinding.tvSoundScreenScanner3.setText("The result is ready NOW");
                        showDialogResult();
                        break;
                }
                handler.postDelayed(this, 500);

            }
        };
        initUI();
        initListenerHeader();
        initListener();
    }
    @SuppressLint("ClickableViewAccessibility")
    private void initListener() {
        mActivitySoundScannerBinding.layoutSoundButtonLeft.setOnClickListener(view -> goToFingerPrintScannerActivity());
        mActivitySoundScannerBinding.layoutSoundButtonRight.setOnClickListener(view -> goToEyesScannerActivity());
        mActivitySoundScannerBinding.layoutSoundButtonCenter.setOnClickListener(view -> initUI());
        mActivitySoundScannerBinding.layoutSoundPressButton2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isButtonPressed = true;
                        countdownPressing = 100;
                        handler.postDelayed(runnablePressing, 500);
                        return true;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        isButtonPressed = false;
                        if (countdownPressing>96){
                            initUIDefault();
                            mActivitySoundScannerBinding.tvSoundScreenScanner1.setVisibility(View.VISIBLE);
                            mActivitySoundScannerBinding.tvScannerPress.setVisibility(View.VISIBLE);
                            mActivitySoundScannerBinding.imgScannerDirectUp.setVisibility(View.VISIBLE);
                            mActivitySoundScannerBinding.layoutSoundScanningProgress.setVisibility(View.GONE);
                        }
                        else {
                            handler.removeCallbacks(runnablePressing);
                            mActivitySoundScannerBinding.layoutSoundScanningProgress.setVisibility(View.GONE);
                            mActivitySoundScannerBinding.tvSoundScreenScanner1.setVisibility(View.GONE);
                            mActivitySoundScannerBinding.layoutSoundScanningProgress.setVisibility(View.GONE);
                            mActivitySoundScannerBinding.layoutSoundAnalyzing.setVisibility(View.VISIBLE);
                            countdownAnalyzing = 0;
                            handler.postDelayed(runnableAnalyzing, 500);
                        }


                        return true;
                }
                return false;
            }
        });
    }
    private void showDialogResult() {
        handler.removeCallbacks(runnableAnalyzing);
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
        mActivitySoundScannerBinding.imgScannerResultBackgroundLight.setVisibility(View.GONE);
        mActivitySoundScannerBinding.imgSoundScreenScanner.setImageResource(R.drawable.img_scanner_screen_scanner_default);
        mActivitySoundScannerBinding.layoutSoundAnalyzing.setVisibility(View.GONE);
        mActivitySoundScannerBinding.layoutSoundAnalyzingResult.setVisibility(View.GONE);
        mActivitySoundScannerBinding.imgSoundPressBorder.setImageResource(R.drawable.img_scanner_press_border_default);
        mActivitySoundScannerBinding.imgSoundPress.setImageResource(R.drawable.img_sound_press_default);
        mActivitySoundScannerBinding.tvSoundScreenScanner3.setText("Analyzing");
        mActivitySoundScannerBinding.tvSoundScreenScanner3.setTextColor(getResources().getColor(R.color.white));
        mActivitySoundScannerBinding.tvScannerPress.setText("PRESS TO TALK");
        mActivitySoundScannerBinding.imgSoundBottomButton.setImageResource(R.drawable.img_sound_bottom_button_default);
    }

    private void initUIGetTruth() {
        mActivitySoundScannerBinding.imgScannerResultBackgroundLight.setVisibility(View.VISIBLE);
        mActivitySoundScannerBinding.imgScannerResultBackgroundLight.setImageResource(R.drawable.img_scanner_result_background_light_truth);
        mActivitySoundScannerBinding.imgSoundScreenScanner.setImageResource(R.drawable.img_scanner_screen_scanner_truth);
        mActivitySoundScannerBinding.tvSoundScreenScanner3.setText("YOU TELL THE TRUTH");
        mActivitySoundScannerBinding.tvSoundScreenScanner3.setTextColor(getResources().getColor(R.color.truth));
        mActivitySoundScannerBinding.imgSoundAnalyzingTruth.setImageResource(R.drawable.img_scanner_analyzing_truth);
        mActivitySoundScannerBinding.imgSoundAnalyzingLiar.setImageResource(R.drawable.img_scanner_analyzing_none);
        mActivitySoundScannerBinding.layoutSoundAnalyzingResult.setVisibility(View.VISIBLE);
        mActivitySoundScannerBinding.tvSoundAnalyzingTruth.setTextColor(getResources().getColor(R.color.truth));
        mActivitySoundScannerBinding.tvSoundAnalyzingLiar.setTextColor(getResources().getColor(R.color.grayDefault));
        mActivitySoundScannerBinding.imgSoundPressBorder.setImageResource(R.drawable.img_scanner_press_border_truth);
        mActivitySoundScannerBinding.imgSoundPress.setImageResource(R.drawable.img_sound_press_truth);
        mActivitySoundScannerBinding.tvScannerPress.setVisibility(View.VISIBLE);
        mActivitySoundScannerBinding.tvScannerPress.setText("TRY AGAIN");
        mActivitySoundScannerBinding.imgScannerDirectUp.setVisibility(View.VISIBLE);
        mActivitySoundScannerBinding.imgSoundBottomButton.setImageResource(R.drawable.img_sound_bottom_button_truth);
    }

    private void initUIGetLiar() {
        mActivitySoundScannerBinding.imgScannerResultBackgroundLight.setVisibility(View.VISIBLE);
        mActivitySoundScannerBinding.imgScannerResultBackgroundLight.setImageResource(R.drawable.img_scanner_result_background_light_liar);
        mActivitySoundScannerBinding.imgSoundScreenScanner.setImageResource(R.drawable.img_scanner_screen_scanner_liar);
        mActivitySoundScannerBinding.tvSoundScreenScanner3.setText("YOU LIE");
        mActivitySoundScannerBinding.tvSoundScreenScanner3.setTextColor(getResources().getColor(R.color.liar));
        mActivitySoundScannerBinding.imgSoundAnalyzingTruth.setImageResource(R.drawable.img_scanner_analyzing_none);
        mActivitySoundScannerBinding.imgSoundAnalyzingLiar.setImageResource(R.drawable.img_scanner_analyzing_liar);
        mActivitySoundScannerBinding.layoutSoundAnalyzingResult.setVisibility(View.VISIBLE);
        mActivitySoundScannerBinding.tvSoundAnalyzingTruth.setTextColor(getResources().getColor(R.color.grayDefault));
        mActivitySoundScannerBinding.tvSoundAnalyzingLiar.setTextColor(getResources().getColor(R.color.liar));
        mActivitySoundScannerBinding.imgSoundPressBorder.setImageResource(R.drawable.img_scanner_press_border_liar);
        mActivitySoundScannerBinding.imgSoundPress.setImageResource(R.drawable.img_sound_press_liar);
        mActivitySoundScannerBinding.tvScannerPress.setVisibility(View.VISIBLE);
        mActivitySoundScannerBinding.tvScannerPress.setText("TRY AGAIN");
        mActivitySoundScannerBinding.imgScannerDirectUp.setVisibility(View.VISIBLE);
        mActivitySoundScannerBinding.imgSoundBottomButton.setImageResource(R.drawable.img_sound_bottom_button_liar);
    }

    private void initListenerHeader() {
        mActivitySoundScannerBinding.header.imgLeft.setOnClickListener(view -> finish());
        mActivitySoundScannerBinding.header.imgSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SoundScannerActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
    }



    private void goToEyesScannerActivity() {
        Intent intent = new Intent(SoundScannerActivity.this, EyeScannerActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToFingerPrintScannerActivity() {
        Intent intent = new Intent(SoundScannerActivity.this, FingerPrintScannerActivity.class);
        startActivity(intent);
        finish();
    }

    private void initUI() {
        initUIDefault();
    }
}