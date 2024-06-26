package com.vtdglobal.liedetector.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.vtdglobal.liedetector.adapter.IntroViewPagerAdapter;
import com.vtdglobal.liedetector.databinding.ActivityIntroBinding;

public class IntroActivity extends BaseActivity {
    ActivityIntroBinding mActivityIntroBinding;


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
                    finish();
                }
            }
        });
        mActivityIntroBinding.viewpager2Intro.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position==2) {
                    mActivityIntroBinding.btnNext.setText("Start");
                }
                else {
                    mActivityIntroBinding.btnNext.setText("Next");
                }
            }
        });

    }

    private void initUI() {
        IntroViewPagerAdapter introViewPagerAdapter = new IntroViewPagerAdapter(this);
        mActivityIntroBinding.viewpager2Intro.setAdapter(introViewPagerAdapter);
        mActivityIntroBinding.indicator3.setViewPager(mActivityIntroBinding.viewpager2Intro);
        mActivityIntroBinding.viewpager2Intro.setCurrentItem(0);
    }
}