package com.liedetector.test.prank.liescanner.truthtest.ui.permission;

import com.liedetector.test.prank.liescanner.truthtest.base.BaseActivity;
import com.liedetector.test.prank.liescanner.truthtest.databinding.ActivityPermissionBinding;
import com.liedetector.test.prank.liescanner.truthtest.ui.home.HomeActivity;

public class PermissionActivity extends BaseActivity<ActivityPermissionBinding> {


    @Override
    public ActivityPermissionBinding getBinding() {
        return ActivityPermissionBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {

    }

    @Override
    public void bindView() {
        binding.tvContinue.setOnClickListener(v -> {
            startNextActivity(HomeActivity.class, null);
            finishAffinity();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
