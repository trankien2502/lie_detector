package com.vtdglobal.liedetector.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.vtdglobal.liedetector.R;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideNavigationBar();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        hideNavigationBar();
    }
    private void hideNavigationBar() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }
    public void startNextActivity(Class activity, Bundle bundle) {
        Intent intent = new Intent(this, activity);
        if (bundle == null) {
            bundle = new Bundle();
        }
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.in_right, R.anim.out_left);
    }

    public void finishThisActivity() {
        finish();
        overridePendingTransition(R.anim.in_left, R.anim.out_right);
    }


    @Override
    protected void onResume() {
        super.onResume();
        //tắt ads resume all
    }



}
