package com.liedetector.test.prank.liescanner.truthtest.ui.ghost_result;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import androidx.core.content.FileProvider;


import com.ads.sapp.admob.Admob;
import com.ads.sapp.admob.AppOpenManager;
import com.ads.sapp.ads.CommonAd;
import com.ads.sapp.ads.CommonAdCallback;
import com.ads.sapp.funtion.AdCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.liedetector.test.prank.liescanner.truthtest.R;
import com.liedetector.test.prank.liescanner.truthtest.ads.ConstantIdAds;
import com.liedetector.test.prank.liescanner.truthtest.ads.ConstantRemote;
import com.liedetector.test.prank.liescanner.truthtest.ads.IsNetWork;
import com.liedetector.test.prank.liescanner.truthtest.base.BaseActivity;
import com.liedetector.test.prank.liescanner.truthtest.databinding.ActivityImageCaptureBinding;
import com.liedetector.test.prank.liescanner.truthtest.ui.ghost.GhostActivity;
import com.liedetector.test.prank.liescanner.truthtest.util.EventTracking;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageCaptureActivity extends BaseActivity<ActivityImageCaptureBinding> {
    public static Bitmap finalBitmap;
    private final String AUTHORITY = "com.liedetector.test.prank.liescanner.truthtest.fileprovider";

    @Override
    public ActivityImageCaptureBinding getBinding() {
        return ActivityImageCaptureBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        binding.header.imgSetting.setVisibility(View.INVISIBLE);
        binding.header.tvTitle.setText(R.string.image_capture);
        binding.imgImageCapture.setImageBitmap(GhostActivity.capturedBitmap);
        binding.imgGhostFilter.setImageBitmap(GhostActivity.capturedGhost);
//        ghostImage[GhostActivity.appearedGhostImage].setImageResource(ghost[GhostActivity.appearedGhost]);
//        ghostImage[GhostActivity.appearedGhostImage].setVisibility(View.VISIBLE);
        EventTracking.logEvent(this, "ghost_result_view");
    }

    @Override
    public void bindView() {
        binding.header.imgLeft.setOnClickListener(view -> {
            onBackPressed();
        });
        binding.layoutShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventTracking.logEvent(ImageCaptureActivity.this, "ghost_result_share_click");
                finalBitmap = getBitmapFromView(binding.cardView1);
                Bitmap bitmap = getBitmapFromView(binding.cardView1);
                try {
                    File cachePath = new File(getExternalCacheDir(), "images");
                    cachePath.mkdirs(); // tạo thư mục nếu chưa tồn tại
                    File file = new File(cachePath, "shared_image.png");
                    FileOutputStream fOut = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                    shareImage(FileProvider.getUriForFile(ImageCaptureActivity.this, AUTHORITY, file));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK || result.getResultCode() == RESULT_CANCELED) {
            //reLoadAds();
        }
    });

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        EventTracking.logEvent(ImageCaptureActivity.this, "ghost_result_back_click");
        finish();
    }

//    private void loadInterGhostCapture() {
//        new Thread(() -> {
//            if (IsNetWork.haveNetworkConnection(this) && ConstantIdAds.mInterGhostCaptureBack == null && !ConstantIdAds.listIDAdsInterGhostCaptureBack.isEmpty() && ConstantRemote.inter_ghost_capture_back) {
//                runOnUiThread(() -> ConstantIdAds.mInterGhostCaptureBack = CommonAd.getInstance().getInterstitialAds(this, ConstantIdAds.listIDAdsInterGhostCaptureBack));
//            }
//        }).start();
//    }

//    private void reLoadAds() {
//        if (IsNetWork.haveNetworkConnection(this)) {
//            binding.nativeGhostCapture.setVisibility(View.VISIBLE);
//            @SuppressLint("InflateParams") NativeAdView adViewLoad = (NativeAdView) LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_native_loading, null, false);
//            binding.nativeGhostCapture.removeAllViews();
//            binding.nativeGhostCapture.addView(adViewLoad);
//            loadAds();
//        } else {
//            binding.nativeGhostCapture.setVisibility(View.INVISIBLE);
//        }
//    }

//    private void initData() {
//        ghostImage = new ImageView[]{
//                binding.imgGhost11, binding.imgGhost22, binding.imgGhost33,
//                binding.imgGhost44, binding.imgGhost55, binding.imgGhost4,
//                binding.imgGhost5
//        };
//        ghost = new int[]{
//                R.drawable.img_ghost_11, R.drawable.img_ghost_22, R.drawable.img_ghost_33, R.drawable.img_ghost_44, R.drawable.img_ghost_55,
//                R.drawable.img_ghost_1, R.drawable.img_ghost_2, R.drawable.img_ghost_3, R.drawable.img_ghost_4, R.drawable.img_ghost_5
//        };
//    }

    private void shareImage(Uri uri) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/png");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        AppOpenManager.getInstance().disableAppResumeWithActivity(ImageCaptureActivity.class);
        Intent chooser = Intent.createChooser(shareIntent, "Share Image via");
        //startActivity(chooser);
        resultLauncher.launch(chooser);
    }

    private Bitmap getBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


}