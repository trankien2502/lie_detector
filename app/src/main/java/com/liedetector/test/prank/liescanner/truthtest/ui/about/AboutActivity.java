package com.liedetector.test.prank.liescanner.truthtest.ui.about;

import android.view.View;

import com.liedetector.test.prank.liescanner.truthtest.BuildConfig;
import com.liedetector.test.prank.liescanner.truthtest.R;
import com.liedetector.test.prank.liescanner.truthtest.base.BaseActivity;
import com.liedetector.test.prank.liescanner.truthtest.databinding.ActivityAboutBinding;
import com.liedetector.test.prank.liescanner.truthtest.ui.policy.PolicyActivity;
import com.liedetector.test.prank.liescanner.truthtest.util.EventTracking;

public class AboutActivity extends BaseActivity<ActivityAboutBinding> {
    @Override
    public ActivityAboutBinding getBinding() {
        return ActivityAboutBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        EventTracking.logEvent(this,"view_about_app");
        binding.headerSetting.imgTick.setVisibility(View.INVISIBLE);
        binding.headerSetting.tvTitle.setText(getString(R.string.about));

        String version = getString(R.string.version) + " " + BuildConfig.VERSION_NAME;
        binding.tvVersion.setText(version);
    }

    @Override
    public void bindView() {
        binding.headerSetting.imgLeft.setOnClickListener(view -> onBackPressed());

        binding.tvPolicy.setOnClickListener(view -> startNextActivity(PolicyActivity.class, null));
    }


}
