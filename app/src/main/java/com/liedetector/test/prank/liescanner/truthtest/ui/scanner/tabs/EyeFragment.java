package com.liedetector.test.prank.liescanner.truthtest.ui.scanner.tabs;

import static android.graphics.Color.TRANSPARENT;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
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
import com.liedetector.test.prank.liescanner.truthtest.databinding.FragmentEyeBinding;
import com.liedetector.test.prank.liescanner.truthtest.ui.scanner.ScannerActivity;

import java.util.Random;


public class EyeFragment extends Fragment {

    FragmentEyeBinding mFragmentEyeBinding;

    private final Handler handler = new Handler();
    private Runnable runnableAnalyzing;
    private int countdownAnalyzing = 10;
    private boolean isResultScreen = false;
    public static boolean isAnalyzing = false;
    MediaPlayer mediaPlayer;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentEyeBinding = FragmentEyeBinding.inflate(inflater, container, false);
        runnableAnalyzing = new Runnable() {
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
                    if (countdownAnalyzing > 0) {
                        handler.postDelayed(this, 1000);
                    } else {
                        handler.removeCallbacks(runnableAnalyzing);
                        if (mediaPlayer != null) {
                            mediaPlayer.release();
                            mediaPlayer = null;
                        }
                        if (ScannerActivity.isOpenDialog) {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (ScannerActivity.isOpenDialog) {
                                        setVisibilityResultGone();
                                        handler.postDelayed(this, 1000);
                                    } else {
                                        if (isAnalyzing) {
                                            if (mediaPlayer != null) {
                                                mediaPlayer.release();
                                            }
                                            mediaPlayer = MediaPlayer.create(getContext(), R.raw.get_result_sound);
                                            mediaPlayer.start();
//                                            setVisibilityAnalyzingGone();
                                            stopAnalyzing();
                                        }
                                        handler.removeCallbacks(this);
                                    }
                                }
                            }, 1000);
                        } else {
                            if (mediaPlayer != null) {
                                mediaPlayer.release();
                            }
                            mediaPlayer = MediaPlayer.create(getContext(), R.raw.get_result_sound);
                            mediaPlayer.start();
                            stopAnalyzing();
                        }
                        //

                    }
                }


            }
        };
        initUI();
        initListener();
        return mFragmentEyeBinding.getRoot();
    }

    public static boolean isAnalyzing() {
        return isAnalyzing;
    }

    public static void setAnalyzing(boolean isAnalyzing) {
        EyeFragment.isAnalyzing = isAnalyzing;
    }

    private void initListener() {
        mFragmentEyeBinding.layoutEyePressButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isResultScreen) {
                    initUIDefault();
                    isResultScreen = false;
                } else {
                    countdownAnalyzing = 10;
                    isAnalyzing = true;
                    handler.postDelayed(runnableAnalyzing, 1000);
                    setVisibilityAnalyzingVisible();
                }
            }
        });
    }

    private void initUI() {
        initUIDefault();
    }

    private void setVisibilityAnalyzingVisible() {
        mFragmentEyeBinding.layoutEyeScanning.setVisibility(View.VISIBLE);
        mFragmentEyeBinding.imgEyeScanningLeft.setVisibility(View.VISIBLE);
        mFragmentEyeBinding.imgEyeScanningRight.setVisibility(View.VISIBLE);
        mFragmentEyeBinding.imgEyeScanning.setVisibility(View.VISIBLE);
        mFragmentEyeBinding.layoutEyePressButton.setVisibility(View.GONE);
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(getContext(), R.raw.eyescansound_effect2);
        mediaPlayer.start();
        runAnimation();
    }

    private void setVisibilityAnalyzingGone() {
        mFragmentEyeBinding.layoutEyeScanning.setVisibility(View.GONE);
        mFragmentEyeBinding.imgEyeScanningLeft.setVisibility(View.GONE);
        mFragmentEyeBinding.imgEyeScanningRight.setVisibility(View.GONE);
        mFragmentEyeBinding.imgEyeScanning.setVisibility(View.GONE);
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

    private void showDialogResult() {
        isAnalyzing = false;
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
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
                isResultScreen = true;
                setVisibilityAnalyzingGone();
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
        mFragmentEyeBinding.tvScannerPress.setText(R.string.click_here);
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
        ScannerActivity.startCamera(getContext());
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
        mFragmentEyeBinding.tvEyeScreenScanner3.setText(R.string.you_tell_the_truth);
        mFragmentEyeBinding.tvEyeScreenScanner3.setTextColor(getResources().getColor(R.color.truth));
        mFragmentEyeBinding.imgEyeAnalyzingTruth.setImageResource(R.drawable.img_scanner_analyzing_truth);
        mFragmentEyeBinding.imgEyeAnalyzingLiar.setImageResource(R.drawable.img_scanner_analyzing_none);
        mFragmentEyeBinding.layoutEyeAnalyzingResult.setVisibility(View.VISIBLE);
        mFragmentEyeBinding.tvEyeAnalyzingTruth.setTextColor(getResources().getColor(R.color.truth));
        mFragmentEyeBinding.tvEyeAnalyzingLiar.setTextColor(getResources().getColor(R.color.grayDefault));
        mFragmentEyeBinding.imgEyePressBorder.setImageResource(R.drawable.img_scanner_press_border_truth);
        mFragmentEyeBinding.imgEyePress.setImageResource(R.drawable.img_eye_press_truth);
        mFragmentEyeBinding.tvScannerPress.setVisibility(View.VISIBLE);
        mFragmentEyeBinding.tvScannerPress.setText(R.string.try_again_text);
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
        mFragmentEyeBinding.tvEyeScreenScanner3.setText(R.string.you_lie);
        mFragmentEyeBinding.tvEyeScreenScanner3.setTextColor(getResources().getColor(R.color.liar));
        mFragmentEyeBinding.imgEyeAnalyzingTruth.setImageResource(R.drawable.img_scanner_analyzing_none);
        mFragmentEyeBinding.imgEyeAnalyzingLiar.setImageResource(R.drawable.img_scanner_analyzing_liar);
        mFragmentEyeBinding.layoutEyeAnalyzingResult.setVisibility(View.VISIBLE);
        mFragmentEyeBinding.tvEyeAnalyzingTruth.setTextColor(getResources().getColor(R.color.grayDefault));
        mFragmentEyeBinding.tvEyeAnalyzingLiar.setTextColor(getResources().getColor(R.color.liar));
        mFragmentEyeBinding.imgEyePressBorder.setImageResource(R.drawable.img_scanner_press_border_liar);
        mFragmentEyeBinding.imgEyePress.setImageResource(R.drawable.img_eye_press_liar);
        mFragmentEyeBinding.tvScannerPress.setVisibility(View.VISIBLE);
        mFragmentEyeBinding.tvScannerPress.setText(R.string.try_again_text);
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
                animator.setRepeatCount(3);

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
    public void onStop() {
        super.onStop();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (isAnalyzing) initUIDefault();
        isAnalyzing = false;

        if (ScannerActivity.cameraProvider != null) {
            ScannerActivity.cameraProvider.unbindAll();
        }
        handler.removeCallbacks(runnableAnalyzing);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        isAnalyzing = false;
        if (ScannerActivity.cameraProvider != null) {
            ScannerActivity.cameraProvider.unbindAll();
        }
        handler.removeCallbacks(runnableAnalyzing);
    }
}