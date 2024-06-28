package com.vtdglobal.liedetector.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.vtdglobal.liedetector.fragment.Intro1Fragment;
import com.vtdglobal.liedetector.fragment.Intro2Fragment;
import com.vtdglobal.liedetector.fragment.Intro3Fragment;

public class IntroViewPagerAdapter extends FragmentStateAdapter {
    public IntroViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new Intro1Fragment();
        } else if (position==1) {
            return new Intro2Fragment();
        }
        return new Intro3Fragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
