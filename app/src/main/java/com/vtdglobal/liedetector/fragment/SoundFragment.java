package com.vtdglobal.liedetector.fragment;

import static android.graphics.Color.TRANSPARENT;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.vtdglobal.liedetector.R;
import com.vtdglobal.liedetector.activity.ScannerActivity;
import com.vtdglobal.liedetector.databinding.FragmentSoundBinding;

import java.io.IOException;
import java.util.Random;

import pl.droidsonroids.gif.GifDrawable;

public class SoundFragment extends Fragment {
    FragmentSoundBinding mFragmentSoundBinding;
    private final Handler handler = new Handler();
    private Runnable runnablePressing, runnableAnalyzing;
    private int countdownPressing = 16, countdownAnalyzing = 0;
    private boolean isButtonPressed = false, isAnalyzing = false;
    MediaPlayer mediaPlayer;
    GifDrawable gifDrawable;
    Random random = new Random();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentSoundBinding = FragmentSoundBinding.inflate(inflater, container, false);
        runnablePressing = new Runnable() {
            @Override
            public void run() {
                if (isButtonPressed) {
                    initUIDefault();
                    countdownPressing--;
                    mFragmentSoundBinding.tvSoundScreenScanner1.setVisibility(View.GONE);
                    mFragmentSoundBinding.layoutSoundScanningProgress.setVisibility(View.VISIBLE);
                    mFragmentSoundBinding.tvScannerPress.setVisibility(View.GONE);
                    mFragmentSoundBinding.imgScannerDirectUp.setVisibility(View.GONE);
                    if (countdownPressing > 0) {
                        handler.postDelayed(this, 1000);
                    } else {
                        mFragmentSoundBinding.tvSoundScreenScanner1.setVisibility(View.GONE);
                        mFragmentSoundBinding.layoutSoundScanningProgress.setVisibility(View.GONE);
                        mFragmentSoundBinding.layoutSoundAnalyzing.setVisibility(View.VISIBLE);

                        if (mediaPlayer != null) {
                            mediaPlayer.release();
                        }
                        mediaPlayer = MediaPlayer.create(getContext(), R.raw.analyzing_sound);
                        mediaPlayer.start();
                        isAnalyzing = true;
                        countdownAnalyzing = (16 - countdownPressing) * 2 + random.nextInt(16);
                        handler.postDelayed(runnableAnalyzing, 500);
                    }
                }
            }
        };
        runnableAnalyzing = new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                countdownAnalyzing--;
                switch (countdownAnalyzing % 4) {
                    case 0:
                        mFragmentSoundBinding.imgSoundAnalyzingTruth.setImageResource(R.drawable.img_scanner_analyzing_none);
                        mFragmentSoundBinding.imgSoundAnalyzingLiar.setImageResource(R.drawable.img_scanner_analyzing_liar);
                        mFragmentSoundBinding.tvSoundScreenScanner3.setText(R.string.analyzing___);
                        break;
                    case 1:
                        mFragmentSoundBinding.imgSoundAnalyzingTruth.setImageResource(R.drawable.img_scanner_analyzing_truth);
                        mFragmentSoundBinding.imgSoundAnalyzingLiar.setImageResource(R.drawable.img_scanner_analyzing_none);
                        mFragmentSoundBinding.tvSoundScreenScanner3.setText(R.string.analyzing__);
                        break;
                    case 2:
                        mFragmentSoundBinding.imgSoundAnalyzingTruth.setImageResource(R.drawable.img_scanner_analyzing_none);
                        mFragmentSoundBinding.imgSoundAnalyzingLiar.setImageResource(R.drawable.img_scanner_analyzing_liar);
                        mFragmentSoundBinding.tvSoundScreenScanner3.setText(R.string.analyzing_);
                        break;
                    case 3:
                        mFragmentSoundBinding.imgSoundAnalyzingTruth.setImageResource(R.drawable.img_scanner_analyzing_truth);
                        mFragmentSoundBinding.imgSoundAnalyzingLiar.setImageResource(R.drawable.img_scanner_analyzing_none);
                        mFragmentSoundBinding.tvSoundScreenScanner3.setText(R.string.analyzing);
                        break;
                }
                if (countdownAnalyzing == 0) {
                    mFragmentSoundBinding.tvSoundScreenScanner3.setText(R.string.the_result_is_ready_now);
                    showDialogResult();
                } else
                    handler.postDelayed(this, 500);

            }
        };
        initUI();
        initListener();
        return mFragmentSoundBinding.getRoot();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initListener() {
        mFragmentSoundBinding.layoutSoundPressButton2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (isAnalyzing) return false;
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isButtonPressed = true;
                        countdownPressing = 16;
                        if (mediaPlayer != null) {
                            mediaPlayer.release();
                            mediaPlayer = null;
                        }
                        mFragmentSoundBinding.tvSoundScreenScanner1.setVisibility(View.GONE);
                        mFragmentSoundBinding.layoutSoundScanningProgress.setVisibility(View.VISIBLE);
                        loadGif();
                        mFragmentSoundBinding.tvScannerPress.setVisibility(View.GONE);
                        mFragmentSoundBinding.imgScannerDirectUp.setVisibility(View.GONE);
                        handler.postDelayed(runnablePressing, 1000);
                        return true;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        isButtonPressed = false;
                        if (countdownPressing > 14) {
                            initUIDefault();
                            mFragmentSoundBinding.tvScannerPress.setVisibility(View.VISIBLE);
                            mFragmentSoundBinding.imgScannerDirectUp.setVisibility(View.VISIBLE);
                            mFragmentSoundBinding.layoutSoundScanningProgress.setVisibility(View.GONE);
                        } else {
                            handler.removeCallbacks(runnablePressing);
                            mFragmentSoundBinding.layoutSoundScanningProgress.setVisibility(View.GONE);
                            mFragmentSoundBinding.tvSoundScreenScanner1.setVisibility(View.GONE);
                            mFragmentSoundBinding.layoutSoundScanningProgress.setVisibility(View.GONE);
                            mFragmentSoundBinding.layoutSoundAnalyzing.setVisibility(View.VISIBLE);
                            countdownAnalyzing = (16 - countdownPressing) * 2;
                            handler.postDelayed(runnableAnalyzing, 500);
                            if (mediaPlayer != null) {
                                mediaPlayer.release();
                                mediaPlayer = null;
                            }
                            mediaPlayer = MediaPlayer.create(getContext(), R.raw.analyzing_sound);
                            mediaPlayer.start();
                            isAnalyzing = true;

                        }


                        return true;
                }
                return false;
            }
        });
    }

    private void showDialogResult() {
        handler.removeCallbacks(runnableAnalyzing);
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
                isAnalyzing = false;
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
        ScannerActivity.mType = ScannerActivity.TYPE_DEFAULT;
        ScannerActivity.initUIFooter();
        mFragmentSoundBinding.imgScannerResultBackgroundLight.setVisibility(View.GONE);
        mFragmentSoundBinding.imgSoundScreenScanner.setImageResource(R.drawable.img_scanner_screen_scanner_default);
        mFragmentSoundBinding.layoutSoundAnalyzing.setVisibility(View.GONE);
        mFragmentSoundBinding.layoutSoundAnalyzingResult.setVisibility(View.GONE);
        mFragmentSoundBinding.imgSoundPressBorder.setImageResource(R.drawable.img_scanner_press_border_default);
        mFragmentSoundBinding.imgSoundPress.setImageResource(R.drawable.img_sound_press_default);
        mFragmentSoundBinding.tvSoundScreenScanner3.setText(R.string.analyzing);
        mFragmentSoundBinding.tvSoundScreenScanner3.setTextColor(getResources().getColor(R.color.white));
        mFragmentSoundBinding.tvScannerPress.setText(R.string.press_to_talk);
        mFragmentSoundBinding.tvSoundScreenScanner1.setVisibility(View.VISIBLE);
    }

    private void initUIGetTruth() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(getContext(), R.raw.result_true);
        mediaPlayer.start();
        ScannerActivity.mType = ScannerActivity.TYPE_TRUE;
        ScannerActivity.initUIFooter();
        mFragmentSoundBinding.imgScannerResultBackgroundLight.setVisibility(View.VISIBLE);
        mFragmentSoundBinding.imgScannerResultBackgroundLight.setImageResource(R.drawable.img_scanner_result_background_light_truth);
        mFragmentSoundBinding.imgSoundScreenScanner.setImageResource(R.drawable.img_scanner_screen_scanner_truth);
        mFragmentSoundBinding.tvSoundScreenScanner3.setText(R.string.you_tell_the_truth);
        mFragmentSoundBinding.tvSoundScreenScanner3.setTextColor(getResources().getColor(R.color.truth));
        mFragmentSoundBinding.imgSoundAnalyzingTruth.setImageResource(R.drawable.img_scanner_analyzing_truth);
        mFragmentSoundBinding.imgSoundAnalyzingLiar.setImageResource(R.drawable.img_scanner_analyzing_none);
        mFragmentSoundBinding.layoutSoundAnalyzingResult.setVisibility(View.VISIBLE);
        mFragmentSoundBinding.tvSoundAnalyzingTruth.setTextColor(getResources().getColor(R.color.truth));
        mFragmentSoundBinding.tvSoundAnalyzingLiar.setTextColor(getResources().getColor(R.color.grayDefault));
        mFragmentSoundBinding.imgSoundPressBorder.setImageResource(R.drawable.img_scanner_press_border_truth);
        mFragmentSoundBinding.imgSoundPress.setImageResource(R.drawable.img_sound_press_truth);
        mFragmentSoundBinding.tvScannerPress.setVisibility(View.VISIBLE);
        mFragmentSoundBinding.tvScannerPress.setText(R.string.try_again_text);
        mFragmentSoundBinding.imgScannerDirectUp.setVisibility(View.VISIBLE);
    }

    private void initUIGetLiar() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(getContext(), R.raw.result_lie);
        mediaPlayer.start();
        ScannerActivity.mType = ScannerActivity.TYPE_FALSE;
        ScannerActivity.initUIFooter();
        mFragmentSoundBinding.imgScannerResultBackgroundLight.setVisibility(View.VISIBLE);
        mFragmentSoundBinding.imgScannerResultBackgroundLight.setImageResource(R.drawable.img_scanner_result_background_light_liar);
        mFragmentSoundBinding.imgSoundScreenScanner.setImageResource(R.drawable.img_scanner_screen_scanner_liar);
        mFragmentSoundBinding.tvSoundScreenScanner3.setText(R.string.you_lie);
        mFragmentSoundBinding.tvSoundScreenScanner3.setTextColor(getResources().getColor(R.color.liar));
        mFragmentSoundBinding.imgSoundAnalyzingTruth.setImageResource(R.drawable.img_scanner_analyzing_none);
        mFragmentSoundBinding.imgSoundAnalyzingLiar.setImageResource(R.drawable.img_scanner_analyzing_liar);
        mFragmentSoundBinding.layoutSoundAnalyzingResult.setVisibility(View.VISIBLE);
        mFragmentSoundBinding.tvSoundAnalyzingTruth.setTextColor(getResources().getColor(R.color.grayDefault));
        mFragmentSoundBinding.tvSoundAnalyzingLiar.setTextColor(getResources().getColor(R.color.liar));
        mFragmentSoundBinding.imgSoundPressBorder.setImageResource(R.drawable.img_scanner_press_border_liar);
        mFragmentSoundBinding.imgSoundPress.setImageResource(R.drawable.img_sound_press_liar);
        mFragmentSoundBinding.tvScannerPress.setVisibility(View.VISIBLE);
        mFragmentSoundBinding.tvScannerPress.setText(R.string.try_again_text);
        mFragmentSoundBinding.imgScannerDirectUp.setVisibility(View.VISIBLE);
    }


    private void initUI() {
        initUIDefault();
    }
    private void loadGif() {
        try {
            if (gifDrawable != null) {
                gifDrawable.reset();
            }
            gifDrawable = new GifDrawable(getResources(), R.drawable.img_scanning);
            gifDrawable.setSpeed(2.0f);
            mFragmentSoundBinding.imgSoundScanningProcess.setImageDrawable(gifDrawable);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnableAnalyzing);
        handler.removeCallbacks(runnablePressing);
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

    }
}