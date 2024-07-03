package com.vtdglobal.liedetector.activity;

import static com.vtdglobal.liedetector.activity.PermissionActivity.CAMERA_PERMISSION_REQUEST_CODE;
import static com.vtdglobal.liedetector.activity.PermissionActivity.MICRO_PERMISSION_REQUEST_CODE;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.PixelCopy;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;
import com.vtdglobal.liedetector.R;
import com.vtdglobal.liedetector.databinding.ActivityGhostBinding;
import com.vtdglobal.liedetector.fragment.EyeFragment;
import com.vtdglobal.liedetector.fragment.FingerPrintFragment;
import com.vtdglobal.liedetector.fragment.SoundFragment;
import com.vtdglobal.liedetector.util.CameraUtil;

import java.nio.ByteBuffer;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GhostActivity extends BaseActivity {

    ActivityGhostBinding mActivityGhostBinding;
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
    ImageView[] ghostSign = null;
    boolean isGhostAppeared = false;
    int timeToSee = 30, timeToLeave = 15;
    public static int appearedGhost;
    boolean isOn = true,isLightSign = false;
    public static boolean isStartGetGhost;
    int displayTextTime = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityGhostBinding = ActivityGhostBinding.inflate(getLayoutInflater());
        setContentView(mActivityGhostBinding.getRoot());
        initData();
        initUI();
        initUIHeader();
        initListenerHeader();
        initListener();
        startSecondHandAnimation();
        startCameraBack();
        isStartGetGhost = true;
        getGhost();
    }

    private void initUI() {
        Handler handler1 = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Random randomEvp = new Random();
                if (!isGhostAppeared) {
                    for (int i = 0; i < 6; i++) ghostImage[i].setVisibility(View.GONE);
                    for (int i = 0; i < 6; i++) ghostSign[i].setVisibility(View.GONE);
                    mActivityGhostBinding.imgPointSign.setVisibility(View.GONE);
                    mActivityGhostBinding.layoutCameraGhost.setVisibility(View.GONE);
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
                        mActivityGhostBinding.imgPointSign.setVisibility(View.VISIBLE);
                    } else {
                        mActivityGhostBinding.imgPointSign.setVisibility(View.GONE);
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
        Handler handler2 = new Handler();
        handler2.post(new Runnable() {
            @Override
            public void run() {
                displayTextTime--;
                if (displayTextTime > 0) {
                    mActivityGhostBinding.tvGhostScan.setVisibility(View.VISIBLE);
                    handler2.postDelayed(this, 1000);
                } else {
                    mActivityGhostBinding.tvGhostScan.setVisibility(View.GONE);
                    handler2.removeCallbacks(this);
                }
            }
        });


    }
    private void getGhost() {
        Random random = new Random();
        boolean isSoonToSee = random.nextBoolean();
        int timeRandomSee = random.nextInt(11);
        timeToSee = 30;
        if (isSoonToSee) timeToSee -= timeRandomSee;
        else timeToSee += timeRandomSee;
        ghostAppearRunable = new Runnable() {
            @Override
            public void run() {
                if (isStartGetGhost) {
                    timeToSee--;
                    mActivityGhostBinding.layoutSignGhost.setVisibility(View.GONE);
                    if (timeToSee > 0) {
                        ghostHandler.postDelayed(this, 1000);
                    } else {
                        ghostAppear();
                    }
                } else {
                    ghostHandler.postDelayed(this, 1000);
                }
            }
        };
        ghostHandler.postDelayed(ghostAppearRunable, 1000);
    }

    private void ghostAppear() {
        isGhostAppeared = true;
        ghostHandler.removeCallbacks(ghostAppearRunable);
        Random random = new Random();
        boolean isSoonToLeave = random.nextBoolean();
        int timeRandomLeave = random.nextInt(11);
        timeToLeave = 15;
        if (isSoonToLeave) timeToLeave -= timeRandomLeave;
        else timeToLeave += timeRandomLeave;
        appearedGhost = random.nextInt(6);
        ghostLeaveRunnable = new Runnable() {
            @Override
            public void run() {
                ghostImage[appearedGhost].setImageResource(ghost[appearedGhost]);
                ghostImage[appearedGhost].setVisibility(View.VISIBLE);
                mActivityGhostBinding.imgPointSign.setVisibility(View.VISIBLE);
                timeToLeave--;
                mActivityGhostBinding.layoutCameraGhost.setVisibility(View.VISIBLE);
                mActivityGhostBinding.layoutSignGhost.setVisibility(View.VISIBLE);
                if (timeToLeave > 0) {
                    if (isOn) {
                        ghostSign[appearedGhost].setVisibility(View.VISIBLE);
                    } else {
                        ghostSign[appearedGhost].setVisibility(View.GONE);
                    }
                    isOn = !isOn;
                    ghostHandler.postDelayed(this, 1000);
                } else {
                    isGhostAppeared = false;
                    ghostHandler.removeCallbacks(this);
                    getGhost();
                }
            }
        };
        ghostHandler.postDelayed(ghostLeaveRunnable, 1000);
    }

    private void initData() {
        evp = new ImageView[]{
                mActivityGhostBinding.evp0, mActivityGhostBinding.evp1, mActivityGhostBinding.evp2, mActivityGhostBinding.evp3, mActivityGhostBinding.evp4, mActivityGhostBinding.evp5,
                mActivityGhostBinding.evp6, mActivityGhostBinding.evp7, mActivityGhostBinding.evp8, mActivityGhostBinding.evp9, mActivityGhostBinding.evp10, mActivityGhostBinding.evp11,
                mActivityGhostBinding.evp12, mActivityGhostBinding.evp13, mActivityGhostBinding.evp14, mActivityGhostBinding.evp15, mActivityGhostBinding.evp16, mActivityGhostBinding.evp17,
                mActivityGhostBinding.evp18, mActivityGhostBinding.evp19, mActivityGhostBinding.evp20, mActivityGhostBinding.evp21, mActivityGhostBinding.evp22, mActivityGhostBinding.evp23,
                mActivityGhostBinding.evp24, mActivityGhostBinding.evp25, mActivityGhostBinding.evp26, mActivityGhostBinding.evp27, mActivityGhostBinding.evp28, mActivityGhostBinding.evp29,
                mActivityGhostBinding.evp30, mActivityGhostBinding.evp31, mActivityGhostBinding.evp32, mActivityGhostBinding.evp33, mActivityGhostBinding.evp34, mActivityGhostBinding.evp35,
                mActivityGhostBinding.evp36, mActivityGhostBinding.evp37, mActivityGhostBinding.evp38, mActivityGhostBinding.evp39, mActivityGhostBinding.evp40, mActivityGhostBinding.evp41,
                mActivityGhostBinding.evp42, mActivityGhostBinding.evp43, mActivityGhostBinding.evp44, mActivityGhostBinding.evp45, mActivityGhostBinding.evp46, mActivityGhostBinding.evp47,
                mActivityGhostBinding.evp48, mActivityGhostBinding.evp49, mActivityGhostBinding.evp50, mActivityGhostBinding.evp51, mActivityGhostBinding.evp52, mActivityGhostBinding.evp53
        };
        evpLevel = new int[]{
                R.drawable.img_evp_0, R.drawable.img_evp_1, R.drawable.img_evp_2, R.drawable.img_evp_3, R.drawable.img_evp_4,
                R.drawable.img_evp_5, R.drawable.img_evp_6, R.drawable.img_evp_7, R.drawable.img_evp_8, R.drawable.img_evp_9,
                R.drawable.img_evp_10, R.drawable.img_evp_11, R.drawable.img_evp_12, R.drawable.img_evp_13
        };
        ghost = new int[]{
                R.drawable.img_ghost_1, R.drawable.img_ghost_2, R.drawable.img_ghost_3, R.drawable.img_ghost_4, R.drawable.img_ghost_5, R.drawable.img_ghost_6};
        ghostImage = new ImageView[]{
                mActivityGhostBinding.imgGhost1, mActivityGhostBinding.imgGhost2, mActivityGhostBinding.imgGhost3,
                mActivityGhostBinding.imgGhost4, mActivityGhostBinding.imgGhost5, mActivityGhostBinding.imgGhost6
        };
        ghostSign = new ImageView[]{
                mActivityGhostBinding.layoutSignGhost1, mActivityGhostBinding.layoutSignGhost2, mActivityGhostBinding.layoutSignGhost3,
                mActivityGhostBinding.layoutSignGhost4, mActivityGhostBinding.layoutSignGhost5, mActivityGhostBinding.layoutSignGhost6
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

    private void initListener() {
        mActivityGhostBinding.layoutCameraGhost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isStartGetGhost) return;
                isStartGetGhost = false;
                if (imageCapture == null) {
                    Toast.makeText(GhostActivity.this, "Camera is not available!", Toast.LENGTH_SHORT).show();
                    isStartGetGhost = true;
                    return;
                }
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
                                startNextActivity(ImageCaptureActivity.class, null);
                            }

                            @Override
                            public void onError(@NonNull ImageCaptureException exception) {
                                Toast.makeText(GhostActivity.this, "Error capturing image", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void initUIHeader() {
        mActivityGhostBinding.header.tvTitle.setText(R.string.ghost);
    }

    private void initListenerHeader() {
        mActivityGhostBinding.header.imgLeft.setOnClickListener(view -> finish());
        mActivityGhostBinding.header.imgSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GhostActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
    }

    private void startSecondHandAnimation() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RotateAnimation rotate = new RotateAnimation(degrees, degrees + 15,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotate.setDuration(100);
                rotate.setFillAfter(true);
                rotate.setInterpolator(new LinearInterpolator());
                mActivityGhostBinding.imgRadarScan.startAnimation(rotate);

                degrees += 15;
                if (degrees == 360) {
                    degrees = 0;
                }

                handler.postDelayed(this, 100);
            }
        }, 100);
    }

    private void bindPreViewToCaptureImage() {
//        imageCapture = new ImageCapture.Builder()
//                .setTargetRotation(mActivityGhostBinding.previewView.getDisplay().getRotation())
//                .build();
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
                    preview.setSurfaceProvider(mActivityGhostBinding.previewView.getSurfaceProvider());
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
        if (cameraProvider != null) {
            cameraProvider.unbindAll();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        isStartGetGhost = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isStartGetGhost = true;
    }
}