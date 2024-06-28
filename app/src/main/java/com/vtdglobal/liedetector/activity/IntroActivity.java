package com.vtdglobal.liedetector.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.vtdglobal.liedetector.R;
import com.vtdglobal.liedetector.adapter.IntroAdapter;
import com.vtdglobal.liedetector.adapter.IntroViewPagerAdapter;
import com.vtdglobal.liedetector.databinding.ActivityIntroBinding;
import com.vtdglobal.liedetector.util.SharePrefUtils;

public class IntroActivity extends BaseActivity {
    ActivityIntroBinding mActivityIntroBinding;
    ImageView[] dots = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityIntroBinding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(mActivityIntroBinding.getRoot());
        initUI();
        initListener();
    }

    private void initListener() {
        mActivityIntroBinding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentItem = mActivityIntroBinding.viewpager2Intro.getCurrentItem();
                if (currentItem < 2) {
                    mActivityIntroBinding.viewpager2Intro.setCurrentItem(currentItem + 1);
                } else {
                    Intent intent = new Intent(IntroActivity.this, PermissionActivity.class);
                    startActivity(intent);
                    finishAffinity();
                }
            }
        });

    }

    private void initUI() {
        IntroViewPagerAdapter introViewPagerAdapter = new IntroViewPagerAdapter(this);
        mActivityIntroBinding.viewpager2Intro.setAdapter(introViewPagerAdapter);
        mActivityIntroBinding.viewpager2Intro.setCurrentItem(0);

        dots = new ImageView[]{mActivityIntroBinding.ivCircle01, mActivityIntroBinding.ivCircle02, mActivityIntroBinding.ivCircle03};

        mActivityIntroBinding.viewpager2Intro.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                changeContentInit(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

    }
    public void goToNextScreen() {
        if (SharePrefUtils.getCountOpenApp(this) > 1) {
            startNextActivity(MainActivity.class, null);
        } else {
            startNextActivity(PermissionActivity.class, null);
        }
    }
    private void changeContentInit(int position) {
        for (int i = 0; i < 3; i++) {
            if (i == position) dots[i].setImageResource(R.drawable.ic_intro_s);
            else dots[i].setImageResource(R.drawable.ic_intro_sn);
        }
        switch (position) {
            case 0:
            case 1:
                mActivityIntroBinding.btnNext.setText(R.string.next);
                break;
            case 2:
                mActivityIntroBinding.btnNext.setText("Start");
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        changeContentInit(mActivityIntroBinding.viewpager2Intro.getCurrentItem());
    }
}