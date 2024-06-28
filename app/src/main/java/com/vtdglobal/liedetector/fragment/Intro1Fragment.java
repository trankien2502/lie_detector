package com.vtdglobal.liedetector.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vtdglobal.liedetector.databinding.FragmentIntro1Binding;

public class Intro1Fragment extends Fragment {
    FragmentIntro1Binding mFragmentIntro1Binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentIntro1Binding = FragmentIntro1Binding.inflate(inflater,container,false);
        initUI();
        return mFragmentIntro1Binding.getRoot();
    }

    private void initUI() {

    }
}