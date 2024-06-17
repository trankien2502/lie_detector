package com.vtdglobal.liedetector.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.vtdglobal.liedetector.R;
import com.vtdglobal.liedetector.databinding.ActivityEyeScannerBinding;

public class EyeScannerActivity extends AppCompatActivity {
    ActivityEyeScannerBinding mActivityEyeScannerBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityEyeScannerBinding = ActivityEyeScannerBinding.inflate(getLayoutInflater());
        setContentView(mActivityEyeScannerBinding.getRoot());
        initUI();
        initListener();
    }

    private void initListener() {
    }

    private void initUI() {
    }
}