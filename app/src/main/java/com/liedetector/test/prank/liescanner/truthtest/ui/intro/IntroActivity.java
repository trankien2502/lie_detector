package com.liedetector.test.prank.liescanner.truthtest.ui.intro;

import android.Manifest;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.liedetector.test.prank.liescanner.truthtest.R;
import com.liedetector.test.prank.liescanner.truthtest.base.BaseActivity;
import com.liedetector.test.prank.liescanner.truthtest.databinding.ActivityIntroBinding;
import com.liedetector.test.prank.liescanner.truthtest.ui.main.MainActivity;
import com.liedetector.test.prank.liescanner.truthtest.ui.permission.PermissionActivity;
import com.liedetector.test.prank.liescanner.truthtest.util.EventTracking;
import com.liedetector.test.prank.liescanner.truthtest.util.PermissionManager;

public class IntroActivity extends BaseActivity<ActivityIntroBinding> {
    ImageView[] dots = null;
    IntroAdapter introAdapter;

    @Override
    public ActivityIntroBinding getBinding() {
        return ActivityIntroBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        dots = new ImageView[]{binding.ivCircle01, binding.ivCircle02, binding.ivCircle03};

        introAdapter = new IntroAdapter(this);

        binding.viewPager2.setAdapter(introAdapter);

        binding.viewPager2.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                changeContentInit(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void bindView() {
        binding.btnNext.setOnClickListener(view -> {
            switch (binding.viewPager2.getCurrentItem()){
                case 0:
                    EventTracking.logEvent(this,"intro1_next_click");
                    break;
                case 1:
                    EventTracking.logEvent(this,"intro2_next_click");
                    break;
                case 2:
                    EventTracking.logEvent(this,"intro3_next_click");
                    break;

            }
            if (binding.viewPager2.getCurrentItem() < 2) {
                binding.viewPager2.setCurrentItem(binding.viewPager2.getCurrentItem() + 1);
            } else {
                startNextActivity();
            }
        });

    }

    private void changeContentInit(int position) {
        for (int i = 0; i < 3; i++) {
            if (i == position) dots[i].setImageResource(R.drawable.ic_intro_s);
            else dots[i].setImageResource(R.drawable.ic_intro_sn);
        }
        switch (position){
            case 0:
                EventTracking.logEvent(this,"intro1_view");
                break;
            case 1:
                EventTracking.logEvent(this,"intro2_view");
                break;
            case 2:
                EventTracking.logEvent(this,"intro3_view");
                break;

        }
    }

    public void startNextActivity() {
        if (PermissionManager.checkRecordPermission(this) && PermissionManager.checkCameraPermission(this)) {
            startNextActivity(MainActivity.class, null);
        } else {
            startNextActivity(PermissionActivity.class, null);
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    protected void onStart() {
        super.onStart();
        changeContentInit(binding.viewPager2.getCurrentItem());
    }
}