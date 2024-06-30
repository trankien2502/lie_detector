package com.vtdglobal.liedetector.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.vtdglobal.liedetector.R;
import com.vtdglobal.liedetector.activity.SettingActivity;
import com.vtdglobal.liedetector.databinding.FragmentSettingBinding;
import com.vtdglobal.liedetector.util.SystemUtil;

public class SettingFragment extends Fragment {

    FragmentSettingBinding mFragmentSettingBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentSettingBinding = FragmentSettingBinding.inflate(getLayoutInflater());
        initUI();
        initListener();
        return mFragmentSettingBinding.getRoot();
    }

    private void initUI() {
        mFragmentSettingBinding.tvLanguageCurrent.setText(SystemUtil.getLanguageName(requireContext()));
    }

    private void initListener() {
        mFragmentSettingBinding.layoutLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new LanguageFragment());
                SettingActivity.mType = SettingActivity.TYPE_LANGUAGE;
                SettingActivity.initHeaderUI();
            }
        });
        mFragmentSettingBinding.layoutAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new AboutFragment());
                SettingActivity.mType = SettingActivity.TYPE_ABOUT;
                SettingActivity.initHeaderUI();
            }
        });
        mFragmentSettingBinding.layoutRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
            }
        });
        mFragmentSettingBinding.layoutShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.add(R.id.layout_setting_content, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();

    }
}