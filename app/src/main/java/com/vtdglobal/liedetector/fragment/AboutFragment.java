package com.vtdglobal.liedetector.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vtdglobal.liedetector.R;
import com.vtdglobal.liedetector.databinding.FragmentAboutBinding;

public class AboutFragment extends Fragment {
    FragmentAboutBinding mFragmentAboutBinding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentAboutBinding = FragmentAboutBinding.inflate(getLayoutInflater());
        return mFragmentAboutBinding.getRoot();
    }
}