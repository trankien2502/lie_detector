package com.liedetector.test.prank.liescanner.truthtest.ui.ghost_result;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.core.content.FileProvider;

import com.liedetector.test.prank.liescanner.truthtest.R;
import com.liedetector.test.prank.liescanner.truthtest.base.BaseActivity2;
import com.liedetector.test.prank.liescanner.truthtest.databinding.ActivityImageCaptureBinding;
import com.liedetector.test.prank.liescanner.truthtest.ui.ghost.GhostActivity;
import com.liedetector.test.prank.liescanner.truthtest.ui.setting.SettingActivity;
import com.liedetector.test.prank.liescanner.truthtest.util.EventTracking;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageCaptureActivity extends BaseActivity2 {
    @SuppressLint("StaticFieldLeak")
    public static ActivityImageCaptureBinding mActivityImageCaptureBinding;
    public static Bitmap finalBitmap;
    ImageView[] ghostImage = null;
    int[] ghost = null;
    private final String AUTHORITY = "com.example.liedetector.fileprovider";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityImageCaptureBinding = ActivityImageCaptureBinding.inflate(getLayoutInflater());
        setContentView(mActivityImageCaptureBinding.getRoot());
        initData();
        initUI();
        initListener();
    }

    private void initUI() {
        mActivityImageCaptureBinding.imgImageCapture.setImageBitmap(GhostActivity.capturedBitmap);
        ghostImage[GhostActivity.appearedGhostImage].setImageResource(ghost[GhostActivity.appearedGhost]);
        ghostImage[GhostActivity.appearedGhostImage].setVisibility(View.VISIBLE);
        initUIHeader();
        EventTracking.logEvent(this,"ghost_result_view");
    }

    private void initData() {
        ghostImage = new ImageView[]{
                mActivityImageCaptureBinding.imgGhost11,mActivityImageCaptureBinding.imgGhost22,mActivityImageCaptureBinding.imgGhost33,
                mActivityImageCaptureBinding.imgGhost44,mActivityImageCaptureBinding.imgGhost55, mActivityImageCaptureBinding.imgGhost4,
                mActivityImageCaptureBinding.imgGhost5
        };
        ghost = new int[]{
                R.drawable.img_ghost_11, R.drawable.img_ghost_22, R.drawable.img_ghost_33, R.drawable.img_ghost_44, R.drawable.img_ghost_55,
                R.drawable.img_ghost_1, R.drawable.img_ghost_2, R.drawable.img_ghost_3, R.drawable.img_ghost_4, R.drawable.img_ghost_5
        };
    }

    private void initListener() {
        mActivityImageCaptureBinding.header.imgLeft.setOnClickListener(view -> onBackPressed());
        mActivityImageCaptureBinding.header.imgSetting.setOnClickListener(view -> startNextActivity(SettingActivity.class,null));
        mActivityImageCaptureBinding.layoutShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventTracking.logEvent(ImageCaptureActivity.this,"ghost_result_share_click");
                finalBitmap = getBitmapFromView(mActivityImageCaptureBinding.cardView1);
                Bitmap bitmap = getBitmapFromView(mActivityImageCaptureBinding.cardView1);
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
    private void shareImage(Uri uri) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/png");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Intent chooser = Intent.createChooser(shareIntent, "Share Image via");
        startActivity(chooser);
    }

    private void initUIHeader() {
        mActivityImageCaptureBinding.header.tvTitle.setText(R.string.image_capture);
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
        for (int i=0;i<5;i++) ghostImage[i].setVisibility(View.GONE);
        GhostActivity.isStartGetGhost =true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        setResult(RESULT_OK);
        EventTracking.logEvent(ImageCaptureActivity.this,"ghost_result_back_click");
        finish();
    }
}