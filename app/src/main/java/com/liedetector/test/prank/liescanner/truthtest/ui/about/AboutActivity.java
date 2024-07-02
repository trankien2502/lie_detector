package com.liedetector.test.prank.liescanner.truthtest.ui.about;

import android.view.View;

import com.liedetector.test.prank.liescanner.truthtest.R;
import com.liedetector.test.prank.liescanner.truthtest.base.BaseActivity;
import com.liedetector.test.prank.liescanner.truthtest.databinding.ActivityAboutBinding;
import com.liedetector.test.prank.liescanner.truthtest.ui.policy.PolicyActivity;

public class AboutActivity extends BaseActivity<ActivityAboutBinding> {
    @Override
    public ActivityAboutBinding getBinding() {
        return null;
    }

    @Override
    public void initView() {
        binding.headerSetting.imgTick.setVisibility(View.INVISIBLE);
        binding.headerSetting.tvTitle.setText(getString(R.string.about));
    }

    @Override
    public void bindView() {
        binding.headerSetting.imgLeft.setOnClickListener(view -> onBackPressed());

        binding.tvPolicy.setOnClickListener(view -> {
            startNextActivity(PolicyActivity.class, null);
        });
    }


}
