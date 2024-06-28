package com.vtdglobal.liedetector.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.vtdglobal.liedetector.databinding.FragmentIntro3Binding;

public class Intro3Fragment extends Fragment {
    FragmentIntro3Binding mFragmentIntro3Binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentIntro3Binding = FragmentIntro3Binding.inflate(inflater,container,false);
        initUI();
        return mFragmentIntro3Binding.getRoot();
    }

    private void initUI() {

    }
}