package com.vtdglobal.liedetector.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.vtdglobal.liedetector.databinding.FragmentIntro2Binding;

public class Intro2Fragment extends Fragment {
    FragmentIntro2Binding mFragmentIntro2Binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentIntro2Binding = FragmentIntro2Binding.inflate(inflater,container,false);
        initUI();
        return mFragmentIntro2Binding.getRoot();
    }

    private void initUI() {

    }
}