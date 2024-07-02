package com.liedetector.test.prank.liescanner.truthtest.ui.scanner.tabs;

import static android.graphics.Color.TRANSPARENT;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.liedetector.test.prank.liescanner.truthtest.R;
import com.liedetector.test.prank.liescanner.truthtest.databinding.FragmentFingerPrintBinding;
import com.liedetector.test.prank.liescanner.truthtest.ui.scanner.ScannerActivity;

import java.io.IOException;
import java.util.Random;

import pl.droidsonroids.gif.GifDrawable;

public class FingerPrintFragment extends Fragment {

    FragmentFingerPrintBinding mFragmentFingerPrintBinding;
    private final Handler handler = new Handler();
    private Runnable runnablePressing, runnableAnalyzing;
    private int countdownPressing = 5, countdownAnalyzing = 6;
    private static boolean isButtonPressed = false;
    private static boolean isAnalyzing = false;
    MediaPlayer mediaPlayer;
    GifDrawable gifDrawable;

    public static boolean isButtonPressed() {
        return isButtonPressed;
    }

    public static boolean isAnalyzing() {
        return isAnalyzing;
    }

    public static void setButtonPressed(boolean buttonPressed) {
        isButtonPressed = buttonPressed;
    }

    public static void setAnalyzing(boolean analyzing) {
        isAnalyzing = analyzing;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentFingerPrintBinding = FragmentFingerPrintBinding.inflate(inflater, container, false);
        runnablePressing = new Runnable() {
            @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
            @Override
            public void run() {
                if (isButtonPressed) {
                    countdownPressing--;
                    mFragmentFingerPrintBinding.imgFingerPrintPressing.setColorFilter(Color.TRANSPARENT);
                    mFragmentFingerPrintBinding.tvFingerPrintScreenScanner1.setVisibility(View.GONE);
                    mFragmentFingerPrintBinding.layoutFingerPrintScanningProgress.setVisibility(View.VISIBLE);
                    mFragmentFingerPrintBinding.tvScannerPress.setTextColor(requireContext().getResources().getColor(R.color.none));
                    mFragmentFingerPrintBinding.imgScannerDirectUp.clearColorFilter();
                    mFragmentFingerPrintBinding.tvFingerPrintScreenScanner2.setText(getString(R.string.wait) + " " + countdownPressing + " " + getString(R.string.seconds_to_scan));
                    if (countdownPressing > 0) {
                        handler.postDelayed(this, 1000);
                    } else {
                        mFragmentFingerPrintBinding.tvFingerPrintScreenScanner1.setVisibility(View.GONE);
                        mFragmentFingerPrintBinding.layoutFingerPrintScanningProgress.setVisibility(View.GONE);
                        mFragmentFingerPrintBinding.imgFingerPrintPressing.clearColorFilter();
                        mFragmentFingerPrintBinding.layoutFingerPrintAnalyzing.setVisibility(View.VISIBLE);
                        mFragmentFingerPrintBinding.layoutParentScanningPressing.setVisibility(View.GONE);
                        Random random = new Random();
                        countdownAnalyzing = 6 + random.nextInt(10);
                        if (mediaPlayer != null) {
                            mediaPlayer.release();
                            mediaPlayer = null;
                        }
                        mediaPlayer = MediaPlayer.create(getContext(), R.raw.analyzing_sound);
                        mediaPlayer.start();
                        isAnalyzing = true;
                        handler.postDelayed(runnableAnalyzing, 500);
                        isButtonPressed = false;
                    }
                } else {
                    isAnalyzing = false;
                    handler.removeCallbacks(runnablePressing);
                    mFragmentFingerPrintBinding.layoutFingerPrintScanningProgress.setVisibility(View.GONE);
                    mFragmentFingerPrintBinding.imgFingerPrintPressing.clearColorFilter();
                    if (mediaPlayer != null) {
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                }
            }
        };
        runnableAnalyzing = new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                if (!isAnalyzing) {
                    handler.removeCallbacks(runnableAnalyzing);
                    initUIDefault();
                    if (mediaPlayer != null) {
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                } else {
                    countdownAnalyzing--;
                    switch (countdownAnalyzing % 4) {
                        case 0:
                            mFragmentFingerPrintBinding.imgFingerPrintAnalyzingTruth.setImageResource(R.drawable.img_scanner_analyzing_none);
                            mFragmentFingerPrintBinding.imgFingerPrintAnalyzingLiar.setImageResource(R.drawable.img_scanner_analyzing_liar);
                            mFragmentFingerPrintBinding.tvFingerPrintScreenScanner3.setText(R.string.analyzing___);
                            break;
                        case 1:
                            mFragmentFingerPrintBinding.imgFingerPrintAnalyzingTruth.setImageResource(R.drawable.img_scanner_analyzing_truth);
                            mFragmentFingerPrintBinding.imgFingerPrintAnalyzingLiar.setImageResource(R.drawable.img_scanner_analyzing_none);
                            mFragmentFingerPrintBinding.tvFingerPrintScreenScanner3.setText(R.string.analyzing__);
                            break;
                        case 2:
                            mFragmentFingerPrintBinding.imgFingerPrintAnalyzingTruth.setImageResource(R.drawable.img_scanner_analyzing_none);
                            mFragmentFingerPrintBinding.imgFingerPrintAnalyzingLiar.setImageResource(R.drawable.img_scanner_analyzing_liar);
                            mFragmentFingerPrintBinding.tvFingerPrintScreenScanner3.setText(R.string.analyzing_);
                            break;
                        case 3:
                            mFragmentFingerPrintBinding.imgFingerPrintAnalyzingTruth.setImageResource(R.drawable.img_scanner_analyzing_truth);
                            mFragmentFingerPrintBinding.imgFingerPrintAnalyzingLiar.setImageResource(R.drawable.img_scanner_analyzing_none);
                            mFragmentFingerPrintBinding.tvFingerPrintScreenScanner3.setText(R.string.analyzing);
                            break;
                    }
                    if (countdownAnalyzing == 0) {
                        mFragmentFingerPrintBinding.tvFingerPrintScreenScanner3.setText(R.string.the_result_is_ready_now);
                        handler.removeCallbacks(runnableAnalyzing);
                        if (mediaPlayer != null) {
                            mediaPlayer.release();
                            mediaPlayer = null;
                        }
                        if (ScannerActivity.isOpenDialog) {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (ScannerActivity.isOpenDialog)
                                        handler.postDelayed(this, 500);
                                    else {
                                        handler.removeCallbacks(this);
                                        if (isAnalyzing) stopAnalyzing();
                                    }
                                }
                            }, 500);
                        } else {
                            stopAnalyzing();
                        }

                    } else handler.postDelayed(this, 500);

                }

            }
        };
        initUI();
        initListener();
        return mFragmentFingerPrintBinding.getRoot();
    }

    private void stopAnalyzing() {
        isAnalyzing = false;
        handler.removeCallbacks(runnableAnalyzing);
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        try {
            showDialogResult();
        } catch (Exception e) {
            initUIDefault();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initListener() {

        mFragmentFingerPrintBinding.layoutFingerPrintPressButton2.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (isAnalyzing) return false;
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initUIDefault();
                        isButtonPressed = true;
                        countdownPressing = 5;
                        mFragmentFingerPrintBinding.imgFingerPrintPressing.setColorFilter(Color.TRANSPARENT);
                        mFragmentFingerPrintBinding.tvFingerPrintScreenScanner1.setVisibility(View.GONE);
                        mFragmentFingerPrintBinding.layoutFingerPrintScanningProgress.setVisibility(View.VISIBLE);
                        mFragmentFingerPrintBinding.tvScannerPress.setTextColor(requireContext().getResources().getColor(R.color.none));
                        mFragmentFingerPrintBinding.imgScannerDirectUp.clearColorFilter();
                        loadGif();
                        mFragmentFingerPrintBinding.tvFingerPrintScreenScanner2.setText(getString(R.string.wait) + " " + countdownPressing + " " + getString(R.string.seconds_to_scan));
                        handler.postDelayed(runnablePressing, 1000);
                        runAnimation();
                        if (mediaPlayer != null) {
                            mediaPlayer.release();
                            mediaPlayer = null;
                        }
                        mediaPlayer = MediaPlayer.create(getContext(), R.raw.scansound_effect2);
                        mediaPlayer.start();
                        return true;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        isButtonPressed = false;
                        if (countdownPressing > 0) {
                            initUIDefault();
                            if (mediaPlayer != null) {
                                mediaPlayer.release();
                                mediaPlayer = null;
                            }
                            mFragmentFingerPrintBinding.tvScannerPress.setTextColor(requireContext().getResources().getColor(R.color.white));
                            mFragmentFingerPrintBinding.imgScannerDirectUp.setColorFilter(Color.TRANSPARENT);

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
        mediaPlayer = MediaPlayer.create(getContext(), R.raw.get_result_sound);
        mediaPlayer.start();
        final Dialog dialog = new Dialog(requireContext());
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

                if (mediaPlayer != null) {
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
                getResultScanner();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

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
        mFragmentFingerPrintBinding.layoutParentScanningPressing.setVisibility(View.VISIBLE);
        mFragmentFingerPrintBinding.tvFingerPrintScreenScanner1.setVisibility(View.VISIBLE);
        mFragmentFingerPrintBinding.imgScannerResultBackgroundLight.setVisibility(View.GONE);
        mFragmentFingerPrintBinding.imgFingerPrintScreenScanner.setImageResource(R.drawable.img_scanner_screen_scanner_default);
        mFragmentFingerPrintBinding.layoutFingerPrintAnalyzing.setVisibility(View.GONE);
        mFragmentFingerPrintBinding.layoutFingerPrintAnalyzingResult.setVisibility(View.GONE);
        mFragmentFingerPrintBinding.imgFingerPrintPressBorder.setImageResource(R.drawable.img_scanner_press_border_default);
        mFragmentFingerPrintBinding.imgFingerPrintPress.setImageResource(R.drawable.img_finger_print_press_default);
        mFragmentFingerPrintBinding.tvFingerPrintScreenScanner3.setText(R.string.analyzing);
        mFragmentFingerPrintBinding.tvFingerPrintScreenScanner3.setTextColor(getResources().getColor(R.color.white));
        mFragmentFingerPrintBinding.tvScannerPress.setTextColor(requireContext().getResources().getColor(R.color.white));
        mFragmentFingerPrintBinding.tvScannerPress.setText(R.string.press_here);
        mFragmentFingerPrintBinding.imgScannerDirectUp.setColorFilter(TRANSPARENT);
        mFragmentFingerPrintBinding.layoutFingerPrintScanningProgress.setVisibility(View.GONE);
    }

    private void initUIGetTruth() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(getContext(), R.raw.result_true);
        mediaPlayer.start();
        ScannerActivity.mType = ScannerActivity.TYPE_TRUE;
        ScannerActivity.initUIFooter();
        mFragmentFingerPrintBinding.layoutParentScanningPressing.setVisibility(View.VISIBLE);
        mFragmentFingerPrintBinding.imgScannerResultBackgroundLight.setVisibility(View.VISIBLE);
        mFragmentFingerPrintBinding.imgScannerResultBackgroundLight.setImageResource(R.drawable.img_scanner_result_background_light_truth);
        mFragmentFingerPrintBinding.imgFingerPrintScreenScanner.setImageResource(R.drawable.img_scanner_screen_scanner_truth);
        mFragmentFingerPrintBinding.tvFingerPrintScreenScanner3.setText(R.string.you_tell_the_truth);
        mFragmentFingerPrintBinding.tvFingerPrintScreenScanner3.setTextColor(getResources().getColor(R.color.truth));
        mFragmentFingerPrintBinding.imgFingerPrintAnalyzingTruth.setImageResource(R.drawable.img_scanner_analyzing_truth);
        mFragmentFingerPrintBinding.imgFingerPrintAnalyzingLiar.setImageResource(R.drawable.img_scanner_analyzing_none);
        mFragmentFingerPrintBinding.layoutFingerPrintAnalyzingResult.setVisibility(View.VISIBLE);
        mFragmentFingerPrintBinding.tvFingerPrintAnalyzingTruth.setTextColor(getResources().getColor(R.color.truth));
        mFragmentFingerPrintBinding.tvFingerPrintAnalyzingLiar.setTextColor(getResources().getColor(R.color.grayDefault));
        mFragmentFingerPrintBinding.imgFingerPrintPressBorder.setImageResource(R.drawable.img_scanner_press_border_truth);
        mFragmentFingerPrintBinding.imgFingerPrintPress.setImageResource(R.drawable.img_finger_print_press_truth);
        mFragmentFingerPrintBinding.tvScannerPress.setTextColor(requireContext().getResources().getColor(R.color.white));
        mFragmentFingerPrintBinding.tvScannerPress.setText(R.string.try_again_text);
        mFragmentFingerPrintBinding.imgScannerDirectUp.setColorFilter(TRANSPARENT);
    }

    private void initUIGetLiar() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(getContext(), R.raw.result_lie);
        mediaPlayer.start();
        ScannerActivity.mType = ScannerActivity.TYPE_FALSE;
        ScannerActivity.initUIFooter();
        mFragmentFingerPrintBinding.layoutParentScanningPressing.setVisibility(View.VISIBLE);
        mFragmentFingerPrintBinding.imgScannerResultBackgroundLight.setVisibility(View.VISIBLE);
        mFragmentFingerPrintBinding.imgScannerResultBackgroundLight.setImageResource(R.drawable.img_scanner_result_background_light_liar);
        mFragmentFingerPrintBinding.imgFingerPrintScreenScanner.setImageResource(R.drawable.img_scanner_screen_scanner_liar);
        mFragmentFingerPrintBinding.tvFingerPrintScreenScanner3.setText(R.string.you_lie);
        mFragmentFingerPrintBinding.tvFingerPrintScreenScanner3.setTextColor(getResources().getColor(R.color.liar));
        mFragmentFingerPrintBinding.imgFingerPrintAnalyzingTruth.setImageResource(R.drawable.img_scanner_analyzing_none);
        mFragmentFingerPrintBinding.imgFingerPrintAnalyzingLiar.setImageResource(R.drawable.img_scanner_analyzing_liar);
        mFragmentFingerPrintBinding.layoutFingerPrintAnalyzingResult.setVisibility(View.VISIBLE);
        mFragmentFingerPrintBinding.tvFingerPrintAnalyzingTruth.setTextColor(getResources().getColor(R.color.grayDefault));
        mFragmentFingerPrintBinding.tvFingerPrintAnalyzingLiar.setTextColor(getResources().getColor(R.color.liar));
        mFragmentFingerPrintBinding.imgFingerPrintPressBorder.setImageResource(R.drawable.img_scanner_press_border_liar);
        mFragmentFingerPrintBinding.imgFingerPrintPress.setImageResource(R.drawable.img_finger_print_press_liar);
        mFragmentFingerPrintBinding.tvScannerPress.setTextColor(requireContext().getResources().getColor(R.color.white));
        mFragmentFingerPrintBinding.tvScannerPress.setText(R.string.try_again_text);
        mFragmentFingerPrintBinding.imgScannerDirectUp.setColorFilter(TRANSPARENT);
    }


    private void initUI() {
        initUIDefault();
    }

    private void runAnimation() {
        ViewTreeObserver viewTreeObserver = mFragmentFingerPrintBinding.layoutParentScanningPressing.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int parentHeight = mFragmentFingerPrintBinding.layoutParentScanningPressing.getHeight();
                int pressingLightHeight = mFragmentFingerPrintBinding.imgFingerPrintPressing.getHeight();
                ValueAnimator animator = ValueAnimator.ofFloat(0f, parentHeight - pressingLightHeight);
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
                mFragmentFingerPrintBinding.layoutParentScanningPressing.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (isAnalyzing) initUIDefault();
        isAnalyzing = false;
        isButtonPressed = false;
        handler.removeCallbacks(runnableAnalyzing);
        handler.removeCallbacks(runnablePressing);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        isAnalyzing = false;
        isButtonPressed = false;
        handler.removeCallbacks(runnableAnalyzing);
        handler.removeCallbacks(runnablePressing);

    }
}