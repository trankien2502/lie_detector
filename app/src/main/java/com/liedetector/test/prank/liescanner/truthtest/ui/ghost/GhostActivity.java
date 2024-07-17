package com.liedetector.test.prank.liescanner.truthtest.ui.ghost;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.ads.sapp.admob.AppOpenManager;
import com.ads.sapp.ads.CommonAd;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.common.util.concurrent.ListenableFuture;
import com.liedetector.test.prank.liescanner.truthtest.R;
import com.liedetector.test.prank.liescanner.truthtest.ads.ConstantIdAds;
import com.liedetector.test.prank.liescanner.truthtest.ads.ConstantRemote;
import com.liedetector.test.prank.liescanner.truthtest.ads.IsNetWork;
import com.liedetector.test.prank.liescanner.truthtest.base.BaseActivity;
import com.liedetector.test.prank.liescanner.truthtest.databinding.ActivityGhostBinding;
import com.liedetector.test.prank.liescanner.truthtest.ui.ghost_result.ImageCaptureActivity;
import com.liedetector.test.prank.liescanner.truthtest.ui.setting.SettingActivity;
import com.liedetector.test.prank.liescanner.truthtest.util.EventTracking;


import java.nio.ByteBuffer;
import java.util.Random;

public class GhostActivity extends BaseActivity<ActivityGhostBinding> {

    ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    ProcessCameraProvider cameraProvider;
    Preview preview;
    CameraSelector cameraSelector;
    private final Handler handler = new Handler();
    Handler ghostHandler = new Handler();
    private int degrees = 0;
    public static Bitmap capturedBitmap;
    ImageCapture imageCapture;
    Runnable ghostAppearRunable, ghostLeaveRunnable;
    ImageView[] evp = null;
    int[] evpLevel = null;
    int[] ghost = null;
    ImageView[] ghostImage = null;
    ImageView[] pointSign = null;
    ImageView[] ghostSign = null;
    boolean isGhostAppeared = false;
    int timeToSee = 30, timeToLeave = 15;
    int timeShowSignGhost;
    public static int appearedGhost;
    public static int appearedGhostImage;
    boolean isLightSign = false, isGhostSignShow = true;
    boolean isStartGetGhost, isActivityGhost;
    int displayTextTime = 10;
    MediaPlayer mediaPlayerBackground, mediaPlayerGhost;

    @Override
    public ActivityGhostBinding getBinding() {
        return ActivityGhostBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        EventTracking.logEvent(this, "ghost_view");
        //loadBanner();
        initData();
        initUI();
        isStartGetGhost = true;
        isActivityGhost = true;
        Log.d("isCheckGhost", "isStartGetGhost: " + isStartGetGhost);
        getGhost();
    }

    @Override
    public void bindView() {
        binding.header.imgLeft.setOnClickListener(view -> {
            onBackPressed();
        });
        binding.header.imgSetting.setOnClickListener(view -> {
            isActivityGhost = false;
            resultLauncher.launch(new Intent(GhostActivity.this, SettingActivity.class));
        });
        binding.layoutCameraGhost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventTracking.logEvent(GhostActivity.this, "ghost_camera_click");
                if (!isStartGetGhost) return;
                isStartGetGhost = false;
                isActivityGhost = false;
                Log.d("isCheckGhost", "isStartGetGhost: " + isStartGetGhost);
                Log.d("isCheckGhost", "isActivityGhost: " + isActivityGhost);
                if (imageCapture == null) {
                    Toast.makeText(GhostActivity.this, "Camera is not available!", Toast.LENGTH_SHORT).show();
                    isStartGetGhost = true;
                    isActivityGhost = true;
                    Log.d("isCheckGhost", "isStartGetGhost: " + isStartGetGhost);
                    Log.d("isCheckGhost", "isActivityGhost: " + isActivityGhost);
                    return;
                }
                showLoadingDialog();
                timeToLeave += 2;
                imageCapture.takePicture(ContextCompat.getMainExecutor(GhostActivity.this),
                        new ImageCapture.OnImageCapturedCallback() {
                            @Override
                            public void onCaptureSuccess(@NonNull ImageProxy image) {
                                @ExperimentalGetImage Image media = image.getImage();
                                @OptIn(markerClass = ExperimentalGetImage.class) Bitmap bitmap = imageToBitmap(media);
                                int rotationDegrees = image.getImageInfo().getRotationDegrees();
                                if (rotationDegrees != 0) {
                                    Matrix matrix = new Matrix();
                                    matrix.postRotate(rotationDegrees);
                                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                                }
                                image.close();
                                capturedBitmap = bitmap;
                                AppOpenManager.getInstance().disableAppResumeWithActivity(GhostActivity.class);
                                //startNextActivity(ImageCaptureActivity.class, null);
                                resultLauncher.launch(new Intent(GhostActivity.this, ImageCaptureActivity.class));
                                dismissLoadingDialog();
                            }

                            @Override
                            public void onError(@NonNull ImageCaptureException exception) {
                                dismissLoadingDialog();
                                Toast.makeText(GhostActivity.this, "Error capturing image", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

//    private void loadBanner() {
//        new Thread(() -> runOnUiThread(() -> {
//            if (IsNetWork.haveNetworkConnection(this) && ConstantIdAds.listIDAdsBanner.size() != 0 && ConstantRemote.banner) {
//                CommonAd.getInstance().loadCollapsibleBannerFloor(this, ConstantIdAds.listIDAdsBanner, "bottom");
//                findViewById(R.id.banner).setVisibility(View.VISIBLE);
//            } else {
//                findViewById(R.id.banner).setVisibility(View.GONE);
//            }
//        })).start();
//    }


    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        EventTracking.logEvent(this, "ghost_back_click");
        finish();
    }


    private void playBackgroundSound() {
        if (mediaPlayerBackground != null) {
            mediaPlayerBackground.release();
        }
        mediaPlayerBackground = MediaPlayer.create(this, R.raw.background_sound);
        mediaPlayerBackground.start();
        mediaPlayerBackground.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
            }
        });
    }

    private void playGhostAppearSound() {
        if (mediaPlayerGhost != null) {
            mediaPlayerGhost.release();
        }
        mediaPlayerGhost = MediaPlayer.create(this, R.raw.ghost_appear_sound);
        if (isGhostAppeared)
            mediaPlayerGhost.start();
        mediaPlayerGhost.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (isGhostAppeared) {
                    mediaPlayer.seekTo(0);
                    mediaPlayer.start();
                }

            }
        });

    }

    private void stopGhostAppearSound() {
        if (mediaPlayerGhost != null) {
            mediaPlayerGhost.release();
            mediaPlayerGhost = null;
        }
    }

    private void stopBackgroundSound() {
        if (mediaPlayerBackground != null) {
            mediaPlayerBackground.release();
            mediaPlayerBackground = null;
        }
    }

    private void initUI() {
        binding.header.imgSetting.setVisibility(View.VISIBLE);
        binding.header.tvTitle.setText(R.string.ghost);
        startCameraBack();
        playBackgroundSound();
        startSecondHandAnimation();
        showEvpLevel();
        showTextScan();
    }

    private void showEvpLevel() {
        Handler handler1 = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Random randomEvp = new Random();
                if (!isGhostAppeared) {
                    for (int i = 0; i < 7; i++) ghostImage[i].setVisibility(View.GONE);
                    for (int i = 0; i < 7; i++) pointSign[i].setVisibility(View.GONE);
                    for (int i = 0; i < 7; i++) ghostSign[i].setVisibility(View.GONE);
                    binding.layoutPointSign.setVisibility(View.GONE);
                    binding.layoutCameraGhost.setVisibility(View.GONE);
                    Random randomLine = new Random();
                    int line = randomLine.nextInt(5);
                    if (line == 1 || line == 3) {
                        for (int i = 0; i < 54; i++) {
                            if (i <= 10 || i >= 43) {
                                evp[i].setImageResource(evpLevel[0]);
                            } else {
                                int x = 1 + randomEvp.nextInt(5);
                                evp[i].setImageResource(evpLevel[x]);
                            }
                        }
                    } else {

                        for (int i = 0; i < 54; i++) {
                            if (i <= 10 || i >= 43 || i >= 18 && i <= 21 || i >= 26 && i <= 30 || i >= 36 && i <= 40) {
                                evp[i].setImageResource(evpLevel[0]);
                            } else {
                                int x = 1 + randomEvp.nextInt(6);
                                evp[i].setImageResource(evpLevel[x]);
                            }
                        }
                    }

                } else {
                    if (isLightSign) {
                        binding.layoutPointSign.setVisibility(View.VISIBLE);
                    } else {
                        binding.layoutPointSign.setVisibility(View.GONE);
                    }
                    isLightSign = !isLightSign;
                    for (int i = 0; i < 54; i++) {
                        if (i <= 7 || i >= 47) {
                            int x = randomEvp.nextInt(2);
                            evp[i].setImageResource(evpLevel[0]);
                        } else if (i <= 18 || i >= 36) {
                            int x = randomEvp.nextInt(4);
                            evp[i].setImageResource(evpLevel[x]);
                        } else if (i <= 22 || i >= 32) {
                            int x = 6 + randomEvp.nextInt(4);
                            evp[i].setImageResource(evpLevel[x]);
                        } else {
                            int x = 10 + randomEvp.nextInt(4);
                            evp[i].setImageResource(evpLevel[x]);
                        }
                    }
                }
                handler1.postDelayed(this, 250);
            }
        };
        handler1.postDelayed(runnable, 250);
    }

    private void showTextScan() {
        Handler handler2 = new Handler();
        handler2.post(new Runnable() {
            @Override
            public void run() {
                displayTextTime--;
                if (displayTextTime > 0) {
                    binding.tvGhostScan.setVisibility(View.VISIBLE);
                    handler2.postDelayed(this, 1000);
                } else {
                    binding.tvGhostScan.setVisibility(View.GONE);
                    handler2.removeCallbacks(this);
                }
            }
        });
    }

    private void randomToSeeGhost() {
        Random random = new Random();
        boolean isSoonToSee = random.nextBoolean();
        int timeRandomSee = random.nextInt(11);
        timeToSee = 30;
        if (isSoonToSee) timeToSee -= timeRandomSee;
        else timeToSee += timeRandomSee;
    }

    private void getGhost() {
        randomToSeeGhost();
        ghostAppearRunable = new Runnable() {
            @Override
            public void run() {
                if (isStartGetGhost) {
                    timeToSee--;
                    binding.layoutSignGhost.setVisibility(View.GONE);
                    if (timeToSee > 0) {
                        ghostHandler.postDelayed(this, 1000);
                    } else {
                        ghostHandler.removeCallbacks(this);
                        ghostAppear();
                    }
                } else {
                    ghostHandler.postDelayed(this, 1000);
                }
                Log.d("isCheckGhost", "isStartGetGhost: " + isStartGetGhost + " " + timeToSee);
            }
        };
        ghostHandler.postDelayed(ghostAppearRunable, 1000);
    }

    private void ghostAppear() {
        isGhostAppeared = true;
        Log.d("isCheckGhost", "isGhostAppeared: " + isGhostAppeared);
        Random random = new Random();
        boolean isSoonToLeave = random.nextBoolean();
        int timeRandomLeave = random.nextInt(11);
        timeToLeave = 15;
        if (isSoonToLeave) timeToLeave -= timeRandomLeave;
        else timeToLeave += timeRandomLeave;
        timeShowSignGhost = timeToLeave - 1;
        appearedGhost = random.nextInt(10);
        appearedGhostImage = random.nextInt(7);
        playGhostAppearSound();
        ghostLeaveRunnable = new Runnable() {
            @Override
            public void run() {
                ghostImage[appearedGhostImage].setImageResource(ghost[appearedGhost]);
                ghostImage[appearedGhostImage].setVisibility(View.VISIBLE);
                pointSign[appearedGhostImage].setVisibility(View.VISIBLE);
                timeToLeave--;
                binding.layoutCameraGhost.setVisibility(View.VISIBLE);
                binding.layoutSignGhost.setVisibility(View.VISIBLE);
                if (timeToLeave > 0) {
                    if (timeToLeave < timeShowSignGhost) {
                        if (isGhostSignShow)
                            ghostSign[appearedGhostImage].setVisibility(View.VISIBLE);
                        else ghostSign[appearedGhostImage].setVisibility(View.INVISIBLE);
                        isGhostSignShow = !isGhostSignShow;
                        new Handler().postDelayed(() -> {
                            if (isGhostSignShow)
                                ghostSign[appearedGhostImage].setVisibility(View.VISIBLE);
                            else ghostSign[appearedGhostImage].setVisibility(View.INVISIBLE);
                            isGhostSignShow = !isGhostSignShow;
                        }, 500);
                    }
                    Log.d("isCheckGhost", "isStartGetGhost: " + isStartGetGhost + " " + timeToLeave);
                    ghostHandler.postDelayed(this, 1000);
                } else {
                    isGhostAppeared = false;
                    //isStartGetGhost = true;
                    Log.d("isCheckGhost", "isGhostAppeared: " + isGhostAppeared);
                    ghostHandler.removeCallbacks(this);
                    stopGhostAppearSound();
                    getGhost();
                }
            }
        };
        ghostHandler.postDelayed(ghostLeaveRunnable, 1000);
    }

    private void initData() {
        evp = new ImageView[]{
                binding.evp0, binding.evp1, binding.evp2, binding.evp3, binding.evp4, binding.evp5,
                binding.evp6, binding.evp7, binding.evp8, binding.evp9, binding.evp10, binding.evp11,
                binding.evp12, binding.evp13, binding.evp14, binding.evp15, binding.evp16, binding.evp17,
                binding.evp18, binding.evp19, binding.evp20, binding.evp21, binding.evp22, binding.evp23,
                binding.evp24, binding.evp25, binding.evp26, binding.evp27, binding.evp28, binding.evp29,
                binding.evp30, binding.evp31, binding.evp32, binding.evp33, binding.evp34, binding.evp35,
                binding.evp36, binding.evp37, binding.evp38, binding.evp39, binding.evp40, binding.evp41,
                binding.evp42, binding.evp43, binding.evp44, binding.evp45, binding.evp46, binding.evp47,
                binding.evp48, binding.evp49, binding.evp50, binding.evp51, binding.evp52, binding.evp53
        };
        evpLevel = new int[]{
                R.drawable.img_evp_0, R.drawable.img_evp_1, R.drawable.img_evp_2, R.drawable.img_evp_3, R.drawable.img_evp_4,
                R.drawable.img_evp_5, R.drawable.img_evp_6, R.drawable.img_evp_7, R.drawable.img_evp_8, R.drawable.img_evp_9,
                R.drawable.img_evp_10, R.drawable.img_evp_11, R.drawable.img_evp_12, R.drawable.img_evp_13
        };
        ghost = new int[]{
                R.drawable.img_ghost_11, R.drawable.img_ghost_22, R.drawable.img_ghost_33,
                R.drawable.img_ghost_44, R.drawable.img_ghost_55,
                R.drawable.img_ghost_1, R.drawable.img_ghost_2, R.drawable.img_ghost_3, R.drawable.img_ghost_4, R.drawable.img_ghost_5
        };
        ghostImage = new ImageView[]{
                binding.imgGhost11, binding.imgGhost22, binding.imgGhost33,
                binding.imgGhost44, binding.imgGhost55, binding.imgGhost4,
                binding.imgGhost5
        };
        pointSign = new ImageView[]{
                binding.imgPointSign1, binding.imgPointSign7, binding.imgPointSign3,
                binding.imgPointSign4, binding.imgPointSign5, binding.imgPointSign6,
                binding.imgPointSign2
        };
        ghostSign = new ImageView[]{
                binding.signGhost11, binding.signGhost22, binding.signGhost33,
                binding.signGhost44, binding.signGhost55, binding.signGhost4,
                binding.signGhost5
        };
    }


    private Bitmap rotateBitmap(Bitmap source, int rotationAngle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(rotationAngle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    private Bitmap imageToBitmap(Image image) {
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    private void checkToBackGhostActivity() {
        if (isGhostAppeared) {
            playGhostAppearSound();
            isStartGetGhost = false;
            new Handler().postDelayed(() -> {
                isStartGetGhost = true;
            }, timeToLeave);
        } else {
            isStartGetGhost = true;
        }
        Log.d("isCheckGhost", "isStartGetGhost: " + isStartGetGhost);
        playBackgroundSound();
    }

    public ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            isActivityGhost = true;
            Log.d("isCheckGhost", "isActivityGhost: " + isActivityGhost);
            checkToBackGhostActivity();
        }
    });

    private void startSecondHandAnimation() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RotateAnimation rotate = new RotateAnimation(degrees, degrees + 15,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotate.setDuration(100);
                rotate.setFillAfter(true);
                rotate.setInterpolator(new LinearInterpolator());
                binding.imgRadarScan.startAnimation(rotate);

                degrees += 15;
                if (degrees == 360) {
                    degrees = 0;
                }

                handler.postDelayed(this, 100);
            }
        }, 100);
    }

    private void bindPreViewToCaptureImage() {
        cameraProvider.unbindAll();
        imageCapture = new ImageCapture.Builder().build();
        cameraProvider.bindToLifecycle((LifecycleOwner) GhostActivity.this, cameraSelector, preview, imageCapture);
    }

    private void bindPreviewBack() {
        cameraProvider.unbindAll();
        cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview);
    }

    private void startCameraBack() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    cameraProvider = cameraProviderFuture.get();
                    preview = new Preview.Builder().build();
                    cameraSelector = new CameraSelector.Builder()
                            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                            .build();
                    preview.setSurfaceProvider(binding.previewView.getSurfaceProvider());
                    //bindPreviewBack();
                    bindPreViewToCaptureImage();
                } catch (Exception e) {
                    Log.e("CameraXApp", "Error: ", e);
                }
            }
        }, ContextCompat.getMainExecutor(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ghostHandler.removeCallbacks(ghostAppearRunable);
        ghostHandler.removeCallbacks(ghostLeaveRunnable);
        stopBackgroundSound();
        stopGhostAppearSound();
        if (cameraProvider != null) {
            cameraProvider.unbindAll();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        isStartGetGhost = false;
        Log.d("isCheckGhost", "isStartGetGhost: " + isStartGetGhost);
        stopBackgroundSound();
        stopGhostAppearSound();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isStartGetGhost = false;
        Log.d("isCheckGhost", "isStartGetGhost: " + isStartGetGhost);
        stopBackgroundSound();
        stopGhostAppearSound();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (IsNetWork.haveNetworkConnection(this)) {
            if (ConstantRemote.resume) {
                AppOpenManager.getInstance().enableAppResumeWithActivity(GhostActivity.class);
                AppOpenManager.getInstance().setEnableScreenContentCallback(true);
                AppOpenManager.getInstance().setFullScreenContentCallback(
                        new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent();
                                if (isActivityGhost){
                                    checkToBackGhostActivity();
                                    AppOpenManager.getInstance().removeFullScreenContentCallback();
                                    Log.d("adresumeghost", "Dissmiss");
                                }

                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                super.onAdFailedToShowFullScreenContent(adError);
                                if (isActivityGhost){
                                    checkToBackGhostActivity();
                                    AppOpenManager.getInstance().removeFullScreenContentCallback();
                                    Log.d("adresumeghost", "fail");
                                }

                            }
                        }
                );

            } else {
                AppOpenManager.getInstance().disableAppResumeWithActivity(GhostActivity.class);
                checkToBackGhostActivity();
                Log.d("adresumeghost", "false_config");
            }
        } else {
            checkToBackGhostActivity();
        }


    }


}