package com.liedetector.test.prank.liescanner.truthtest.base;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import com.ads.sapp.admob.Admob;
import com.ads.sapp.admob.AppOpenManager;
import com.ads.sapp.ads.CommonAd;
import com.ads.sapp.ads.CommonAdCallback;
import com.liedetector.test.prank.liescanner.truthtest.R;
import com.liedetector.test.prank.liescanner.truthtest.ads.ConstantIdAds;
import com.liedetector.test.prank.liescanner.truthtest.ads.ConstantRemote;
import com.liedetector.test.prank.liescanner.truthtest.ads.IsNetWork;
import com.liedetector.test.prank.liescanner.truthtest.ui.scanner.ScannerActivity;
import com.liedetector.test.prank.liescanner.truthtest.util.SharePrefUtils;
import com.liedetector.test.prank.liescanner.truthtest.util.SystemUtil;

public abstract class BaseActivity<VB extends ViewBinding> extends AppCompatActivity {

    public VB binding;
    public AlertDialog alertDialog;

    public abstract VB getBinding();

    public abstract void initView();

    public abstract void bindView();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SystemUtil.setLocale(this);
        super.onCreate(savedInstanceState);
        binding = getBinding();
        setContentView(binding.getRoot());

        hideFullNavigation();
        createLoadingDialog();
        initView();
        bindView();

    }

    private void load() {
        if (ConstantRemote.resume) {
            AppOpenManager.getInstance().enableAppResumeWithActivity(getClass());
        } else {
            AppOpenManager.getInstance().disableAppResumeWithActivity(getClass());
        }
    }
    public void showLoadingDialog(){
        alertDialog.show();
    }
    public void dismissLoadingDialog(){
        alertDialog.dismiss();
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


    private void createLoadingDialog(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_loading, null);
        builder.setView(dialogView);
        builder.setCancelable(false);

        alertDialog = builder.create();

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
        load();

    }

    @Override
    public void onBackPressed() {
        finishThisActivity();
    }

    public void hideFullNavigation() {
        try {
            int flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            getWindow().getDecorView().setSystemUiVisibility(flags);

            View decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(visibility -> {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    decorView.setSystemUiVisibility(flags);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
