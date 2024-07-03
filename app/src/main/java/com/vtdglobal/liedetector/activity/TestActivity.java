package com.vtdglobal.liedetector.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.vtdglobal.liedetector.R;
import com.vtdglobal.liedetector.databinding.ActivityTestBinding;

public class TestActivity extends BaseActivity {
    ActivityTestBinding activityTestBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTestBinding = ActivityTestBinding.inflate(getLayoutInflater());
        setContentView(activityTestBinding.getRoot());
        activityTestBinding.testImg.setImageBitmap(ImageCaptureActivity.finalBitmap);
    }
}