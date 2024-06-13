package com.vtdglobal.liedetector.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vtdglobal.liedetector.R;
import com.vtdglobal.liedetector.activity.SoundsActivity;
import com.vtdglobal.liedetector.databinding.FragmentSoundsBinding;

public class SoundsFragment extends Fragment {

    FragmentSoundsBinding mFragmentSoundsBinding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentSoundsBinding = FragmentSoundsBinding.inflate(inflater,container,false);
        initListener();
        return mFragmentSoundsBinding.getRoot();

    }

    private void initListener() {
        mFragmentSoundsBinding.layoutHairClipperBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new HairClipperFragment());
                SoundsActivity.mTypeSound = SoundsActivity.TYPE_HAIR_CLIPPER;
                SoundsActivity.initHeaderUI();
            }
        });
        mFragmentSoundsBinding.layoutFunnyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new FunnyFragment());
                SoundsActivity.mTypeSound = SoundsActivity.TYPE_FUNNY;
                SoundsActivity.initHeaderUI();
            }
        });
        mFragmentSoundsBinding.layoutHilariousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new HilariousFragment());
                SoundsActivity.mTypeSound = SoundsActivity.TYPE_HILARIOUS;
                SoundsActivity.initHeaderUI();
            }
        });
        mFragmentSoundsBinding.layoutPianoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new FortePianoFragment());
                SoundsActivity.mTypeSound = SoundsActivity.TYPE_FORTE_PIANO;
                SoundsActivity.initHeaderUI();
            }
        });
    }
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.layout_sounds_content, fragment);
        //transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();

    }


}