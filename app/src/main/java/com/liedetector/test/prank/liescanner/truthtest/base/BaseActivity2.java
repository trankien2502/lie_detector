package com.liedetector.test.prank.liescanner.truthtest.base;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ads.sapp.admob.Admob;
import com.ads.sapp.admob.AppOpenManager;
import com.liedetector.test.prank.liescanner.truthtest.R;
import com.liedetector.test.prank.liescanner.truthtest.ads.ConstantIdAds;
import com.liedetector.test.prank.liescanner.truthtest.ads.ConstantRemote;
import com.liedetector.test.prank.liescanner.truthtest.ads.IsNetWork;
import com.liedetector.test.prank.liescanner.truthtest.util.SystemUtil;

public abstract class BaseActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SystemUtil.setLocale(this);
        super.onCreate(savedInstanceState);
        hideNavigationBar();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        hideNavigationBar();
    }

    public void loadBanner(RelativeLayout view) {
        if (IsNetWork.haveNetworkConnection(this) && ConstantIdAds.listIDAdsBanner.size() != 0 && ConstantRemote.banner) {
            view.removeAllViews();
            RelativeLayout layout = (RelativeLayout) LayoutInflater.from(this).inflate(com.ads.sapp.R.layout.layout_banner_control, null, false);
            view.addView(layout);
            Admob.getInstance().loadBannerFloor(this, ConstantIdAds.listIDAdsBanner);
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }


    }

    public void loadBannerCollapsible(View view) {
        if (IsNetWork.haveNetworkConnection(this) && ConstantIdAds.listIDAdsBannerCollapsible.size() != 0 && ConstantRemote.banner_collapsible) {
            Admob.getInstance().loadCollapsibleBannerFloor(this, ConstantIdAds.listIDAdsBannerCollapsible, "bottom");
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }

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

    private void load() {
        if (ConstantRemote.resume) {
            AppOpenManager.getInstance().enableAppResumeWithActivity(getClass());
        } else {
            AppOpenManager.getInstance().disableAppResumeWithActivity(getClass());
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        //tắt ads resume all
        load();
    }


}
