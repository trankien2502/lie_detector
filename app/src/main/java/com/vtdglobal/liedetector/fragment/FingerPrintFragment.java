package com.vtdglobal.liedetector.fragment;

import static android.graphics.Color.TRANSPARENT;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.vtdglobal.liedetector.R;
import com.vtdglobal.liedetector.activity.EyeScannerActivity;
import com.vtdglobal.liedetector.activity.FingerPrintScannerActivity;
import com.vtdglobal.liedetector.activity.ScannerActivity;
import com.vtdglobal.liedetector.activity.SettingActivity;
import com.vtdglobal.liedetector.activity.SoundScannerActivity;
import com.vtdglobal.liedetector.databinding.FragmentFingerPrintBinding;

import java.io.IOException;
import java.util.Random;

import pl.droidsonroids.gif.GifDrawable;

public class FingerPrintFragment extends Fragment {

    FragmentFingerPrintBinding mFragmentFingerPrintBinding;
    private final Handler handler = new Handler();
    private Runnable runnablePressing, runnableAnalyzing;
    private int countdownPressing = 5, countdownAnalyzing = 6;
    private boolean isButtonPressed = false,isAnalyzing = false;
    MediaPlayer mediaPlayer;
    GifDrawable gifDrawable;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentFingerPrintBinding = FragmentFingerPrintBinding.inflate(inflater,container,false);
        runnablePressing = new Runnable() {
            @Override
            public void run() {
                if (isButtonPressed) {
                    //initUIDefault();
                    countdownPressing--;
                    mFragmentFingerPrintBinding.imgFingerPrintPressing.setColorFilter(Color.TRANSPARENT);
                    mFragmentFingerPrintBinding.tvFingerPrintScreenScanner1.setVisibility(View.GONE);
                    mFragmentFingerPrintBinding.layoutFingerPrintScanningProgress.setVisibility(View.VISIBLE);
                    mFragmentFingerPrintBinding.tvScannerPress.setVisibility(View.GONE);
                    mFragmentFingerPrintBinding.imgScannerDirectUp.setVisibility(View.GONE);
                    mFragmentFingerPrintBinding.tvFingerPrintScreenScanner2.setText("WAIT " + countdownPressing + " SECONDS TO SCAN");
                    if (countdownPressing > 0) {
                        handler.postDelayed(this, 1000);
                    } else {
                        mFragmentFingerPrintBinding.tvFingerPrintScreenScanner1.setVisibility(View.GONE);
                        mFragmentFingerPrintBinding.layoutFingerPrintScanningProgress.setVisibility(View.GONE);
                        mFragmentFingerPrintBinding.imgFingerPrintPressing.clearColorFilter();
                        mFragmentFingerPrintBinding.layoutFingerPrintAnalyzing.setVisibility(View.VISIBLE);
                        Random random = new Random();
                        countdownAnalyzing = 6 + random.nextInt(10);

                        if (mediaPlayer!=null){
                            mediaPlayer.release();
                            mediaPlayer=null;
                        }
                        mediaPlayer = MediaPlayer.create(getContext(),R.raw.analyzing_sound);
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
                        mFragmentFingerPrintBinding.imgFingerPrintAnalyzingTruth.setImageResource(R.drawable.img_scanner_analyzing_none);
                        mFragmentFingerPrintBinding.imgFingerPrintAnalyzingLiar.setImageResource(R.drawable.img_scanner_analyzing_liar);
                        mFragmentFingerPrintBinding.tvFingerPrintScreenScanner3.setText("Analyzing...");
                        break;
                    case 1:
                        mFragmentFingerPrintBinding.imgFingerPrintAnalyzingTruth.setImageResource(R.drawable.img_scanner_analyzing_truth);
                        mFragmentFingerPrintBinding.imgFingerPrintAnalyzingLiar.setImageResource(R.drawable.img_scanner_analyzing_none);
                        mFragmentFingerPrintBinding.tvFingerPrintScreenScanner3.setText("Analyzing..");
                        break;
                    case 2:
                        mFragmentFingerPrintBinding.imgFingerPrintAnalyzingTruth.setImageResource(R.drawable.img_scanner_analyzing_none);
                        mFragmentFingerPrintBinding.imgFingerPrintAnalyzingLiar.setImageResource(R.drawable.img_scanner_analyzing_liar);
                        mFragmentFingerPrintBinding.tvFingerPrintScreenScanner3.setText("Analyzing.");
                        break;
                    case 3:
                        mFragmentFingerPrintBinding.imgFingerPrintAnalyzingTruth.setImageResource(R.drawable.img_scanner_analyzing_truth);
                        mFragmentFingerPrintBinding.imgFingerPrintAnalyzingLiar.setImageResource(R.drawable.img_scanner_analyzing_none);
                        mFragmentFingerPrintBinding.tvFingerPrintScreenScanner3.setText("Analyzing");
                        break;
                }
                if (countdownAnalyzing==0){
                    mFragmentFingerPrintBinding.tvFingerPrintScreenScanner3.setText("The Result is ready NOW");
                    handler.removeCallbacks(runnableAnalyzing);
                    showDialogResult();
                }else
                    handler.postDelayed(this, 500);

            }
        };
        initUI();
        initListener();
        return mFragmentFingerPrintBinding.getRoot();
    }
    @SuppressLint("ClickableViewAccessibility")
    private void initListener() {

        mFragmentFingerPrintBinding.layoutFingerPrintPressButton2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(isAnalyzing) return false;
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initUIDefault();
                        isButtonPressed = true;
                        countdownPressing = 5 ;
                        mFragmentFingerPrintBinding.imgFingerPrintPressing.setColorFilter(Color.TRANSPARENT);
                        mFragmentFingerPrintBinding.tvFingerPrintScreenScanner1.setVisibility(View.GONE);
                        mFragmentFingerPrintBinding.layoutFingerPrintScanningProgress.setVisibility(View.VISIBLE);
                        mFragmentFingerPrintBinding.tvScannerPress.setVisibility(View.GONE);
                        mFragmentFingerPrintBinding.imgScannerDirectUp.setVisibility(View.GONE);
                        loadGif();
                        mFragmentFingerPrintBinding.tvFingerPrintScreenScanner2.setText("WAIT " + countdownPressing + " SECONDS TO SCAN");
                        handler.postDelayed(runnablePressing, 1000);
                        runAnimation();
                        if (mediaPlayer!=null){
                            mediaPlayer.release();
                            mediaPlayer = null;
                        }
                        mediaPlayer = MediaPlayer.create(getContext(),R.raw.scansound_effect2);
                        mediaPlayer.start();
                        mFragmentFingerPrintBinding.tvFingerPrintScreenScanner2.setText("WAIT " + countdownPressing + " SECONDS TO SCAN");
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
                            mFragmentFingerPrintBinding.tvScannerPress.setVisibility(View.VISIBLE);
                            mFragmentFingerPrintBinding.imgScannerDirectUp.setVisibility(View.VISIBLE);

                        }
                        handler.removeCallbacks(runnablePressing);
                        mFragmentFingerPrintBinding.layoutFingerPrintScanningProgress.setVisibility(View.GONE);
                        mFragmentFingerPrintBinding.imgFingerPrintPressing.clearColorFilter();
                        return true;
                }
                return false;
            }
        });
    }

    private void showDialogResult() {

        handler.removeCallbacks(runnableAnalyzing);
        if (mediaPlayer!=null){
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(getContext(),R.raw.get_result_sound);
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
                isAnalyzing = false;
                mediaPlayer.release();
                mediaPlayer = null;
                getResultScanner();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
//    private void loadGif() {
//        Glide.with(this)
//                .asGif()
//                .load(R.drawable.img_scanning)
//                .error(R.drawable.img_scanner_scanning_process)
//                .into(mFragmentFingerPrintBinding.imgFingerPrintScanningProcess);
//
//    }

    private void loadGif() {
        try {
            if (gifDrawable != null) {
                gifDrawable.reset();
            }
            gifDrawable = new GifDrawable(getResources(), R.drawable.img_scanning);
            gifDrawable.setSpeed(2.0f);
            mFragmentFingerPrintBinding.imgFingerPrintScanningProcess.setImageDrawable(gifDrawable);

        } catch (IOException e) {
            e.printStackTrace();
        }
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
        ScannerActivity.mType = ScannerActivity.TYPE_DEFAULT;
        ScannerActivity.initUIFooter();
        mFragmentFingerPrintBinding.tvFingerPrintScreenScanner1.setVisibility(View.VISIBLE);
        mFragmentFingerPrintBinding.imgScannerResultBackgroundLight.setVisibility(View.GONE);
        mFragmentFingerPrintBinding.imgFingerPrintScreenScanner.setImageResource(R.drawable.img_scanner_screen_scanner_default);
        mFragmentFingerPrintBinding.layoutFingerPrintAnalyzing.setVisibility(View.GONE);
        mFragmentFingerPrintBinding.layoutFingerPrintAnalyzingResult.setVisibility(View.GONE);
        mFragmentFingerPrintBinding.imgFingerPrintPressBorder.setImageResource(R.drawable.img_scanner_press_border_default);
        mFragmentFingerPrintBinding.imgFingerPrintPress.setImageResource(R.drawable.img_finger_print_press_default);
        mFragmentFingerPrintBinding.tvFingerPrintScreenScanner3.setText("Analyzing");
        mFragmentFingerPrintBinding.tvFingerPrintScreenScanner3.setTextColor(getResources().getColor(R.color.white));
        mFragmentFingerPrintBinding.tvScannerPress.setText("PRESS HERE");
    }

    private void initUIGetTruth() {
        if (mediaPlayer!=null){
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(getContext(),R.raw.result_true);
        mediaPlayer.start();
        ScannerActivity.mType = ScannerActivity.TYPE_TRUE;
        ScannerActivity.initUIFooter();
        mFragmentFingerPrintBinding.imgScannerResultBackgroundLight.setVisibility(View.VISIBLE);
        mFragmentFingerPrintBinding.imgScannerResultBackgroundLight.setImageResource(R.drawable.img_scanner_result_background_light_truth);
        mFragmentFingerPrintBinding.imgFingerPrintScreenScanner.setImageResource(R.drawable.img_scanner_screen_scanner_truth);
        mFragmentFingerPrintBinding.tvFingerPrintScreenScanner3.setText("YOU TELL THE TRUTH");
        mFragmentFingerPrintBinding.tvFingerPrintScreenScanner3.setTextColor(getResources().getColor(R.color.truth));
        mFragmentFingerPrintBinding.imgFingerPrintAnalyzingTruth.setImageResource(R.drawable.img_scanner_analyzing_truth);
        mFragmentFingerPrintBinding.imgFingerPrintAnalyzingLiar.setImageResource(R.drawable.img_scanner_analyzing_none);
        mFragmentFingerPrintBinding.layoutFingerPrintAnalyzingResult.setVisibility(View.VISIBLE);
        mFragmentFingerPrintBinding.tvFingerPrintAnalyzingTruth.setTextColor(getResources().getColor(R.color.truth));
        mFragmentFingerPrintBinding.tvFingerPrintAnalyzingLiar.setTextColor(getResources().getColor(R.color.grayDefault));
        mFragmentFingerPrintBinding.imgFingerPrintPressBorder.setImageResource(R.drawable.img_scanner_press_border_truth);
        mFragmentFingerPrintBinding.imgFingerPrintPress.setImageResource(R.drawable.img_finger_print_press_truth);
        mFragmentFingerPrintBinding.tvScannerPress.setVisibility(View.VISIBLE);
        mFragmentFingerPrintBinding.tvScannerPress.setText("TRY AGAIN");
        mFragmentFingerPrintBinding.imgScannerDirectUp.setVisibility(View.VISIBLE);
    }

    private void initUIGetLiar() {
        if (mediaPlayer!=null){
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(getContext(),R.raw.result_lie);
        mediaPlayer.start();
        ScannerActivity.mType = ScannerActivity.TYPE_FALSE;
        ScannerActivity.initUIFooter();
        mFragmentFingerPrintBinding.imgScannerResultBackgroundLight.setVisibility(View.VISIBLE);
        mFragmentFingerPrintBinding.imgScannerResultBackgroundLight.setImageResource(R.drawable.img_scanner_result_background_light_liar);
        mFragmentFingerPrintBinding.imgFingerPrintScreenScanner.setImageResource(R.drawable.img_scanner_screen_scanner_liar);
        mFragmentFingerPrintBinding.tvFingerPrintScreenScanner3.setText("YOU LIE");
        mFragmentFingerPrintBinding.tvFingerPrintScreenScanner3.setTextColor(getResources().getColor(R.color.liar));
        mFragmentFingerPrintBinding.imgFingerPrintAnalyzingTruth.setImageResource(R.drawable.img_scanner_analyzing_none);
        mFragmentFingerPrintBinding.imgFingerPrintAnalyzingLiar.setImageResource(R.drawable.img_scanner_analyzing_liar);
        mFragmentFingerPrintBinding.layoutFingerPrintAnalyzingResult.setVisibility(View.VISIBLE);
        mFragmentFingerPrintBinding.tvFingerPrintAnalyzingTruth.setTextColor(getResources().getColor(R.color.grayDefault));
        mFragmentFingerPrintBinding.tvFingerPrintAnalyzingLiar.setTextColor(getResources().getColor(R.color.liar));
        mFragmentFingerPrintBinding.imgFingerPrintPressBorder.setImageResource(R.drawable.img_scanner_press_border_liar);
        mFragmentFingerPrintBinding.imgFingerPrintPress.setImageResource(R.drawable.img_finger_print_press_liar);
        mFragmentFingerPrintBinding.tvScannerPress.setVisibility(View.VISIBLE);
        mFragmentFingerPrintBinding.tvScannerPress.setText("TRY AGAIN");
        mFragmentFingerPrintBinding.imgScannerDirectUp.setVisibility(View.VISIBLE);
    }




//    private void goToEyesScannerActivity() {
//        Intent intent = new Intent(FingerPrintScannerActivity.this, EyeScannerActivity.class);
//        startActivity(intent);
//        finish();
//    }
//
//    private void goToSoundScannerActivity() {
//        Intent intent = new Intent(FingerPrintScannerActivity.this, SoundScannerActivity.class);
//        startActivity(intent);
//        finish();
//    }

    private void initUI() {
        initUIDefault();
    }
    private void runAnimation(){
        ViewTreeObserver viewTreeObserver = mFragmentFingerPrintBinding.layoutFingerPrintPressButton2.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int parentHeight = mFragmentFingerPrintBinding.layoutFingerPrintPressButton2.getHeight();
                int pressingLightHeight = mFragmentFingerPrintBinding.imgFingerPrintPressing.getHeight();
                ValueAnimator animator = ValueAnimator.ofFloat(0f,parentHeight-pressingLightHeight);
                animator.setDuration(800);
                animator.setInterpolator(new LinearInterpolator());
                animator.setRepeatCount(ValueAnimator.INFINITE);
                animator.setRepeatMode(ValueAnimator.REVERSE);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                        float value = (float) valueAnimator.getAnimatedValue();
                        mFragmentFingerPrintBinding.imgFingerPrintPressing.setTranslationY(value);
                    }
                });
                animator.start();
                mFragmentFingerPrintBinding.layoutFingerPrintPressButton2.getViewTreeObserver().removeOnGlobalLayoutListener(this);
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
        handler.removeCallbacks(runnableAnalyzing);
        handler.removeCallbacks(runnablePressing);

    }
}