package com.vtdglobal.liedetector.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.vtdglobal.liedetector.fragment.FortePianoFragment;
import com.vtdglobal.liedetector.fragment.FunnyFragment;
import com.vtdglobal.liedetector.fragment.HairClipperFragment;
import com.vtdglobal.liedetector.fragment.HilariousFragment;
import com.vtdglobal.liedetector.fragment.Intro1Fragment;
import com.vtdglobal.liedetector.fragment.Intro2Fragment;
import com.vtdglobal.liedetector.fragment.Intro3Fragment;
import com.vtdglobal.liedetector.fragment.SoundsFragment;

public class SoundsViewPagerAdapter extends FragmentStateAdapter {
    public SoundsViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new SoundsFragment();
            case 1:
                return new HairClipperFragment();
            case 2:
                return new FunnyFragment();
            case 3:
                return new HilariousFragment();
            default:
                return new FortePianoFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
