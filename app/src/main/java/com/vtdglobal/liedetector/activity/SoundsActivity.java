package com.vtdglobal.liedetector.activity;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.vtdglobal.liedetector.R;
import com.vtdglobal.liedetector.databinding.ActivitySoundsBinding;
import com.vtdglobal.liedetector.fragment.SoundsFragment;

public class SoundsActivity extends BaseActivity {
    @SuppressLint("StaticFieldLeak")
    static ActivitySoundsBinding mActivitySoundsBinding;
    public static final int TYPE_SOUNDS = 1;
    public static final int TYPE_HAIR_CLIPPER = 2;
    public static final int TYPE_FUNNY = 3;
    public static final int TYPE_HILARIOUS = 4;
    public static final int TYPE_FORTE_PIANO = 5;

    public static final String TEXT_SOUNDS = "SOUNDS";
    public static final String TEXT_HAIR_CLIPPER = "HAIR CLIPPER";
    public static final String TEXT_FUNNY = "FUNNY";
    public static final String TEXT_HILARIOUS = "HILARIOUS";
    public static final String TEXT_FORTE_PIANO = "FORTE PIANO";

    public static int mTypeSound = TYPE_SOUNDS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivitySoundsBinding = ActivitySoundsBinding.inflate(getLayoutInflater());
        setContentView(mActivitySoundsBinding.getRoot());
        initUI();
        initHeaderListener();
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                onClickBackButton();
                // Thực hiện hành động tùy chỉnh khi nút back được nhấn
//                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
//                    onClickBackButton();
//                } else {
//                    // Nếu không có fragment nào trong back stack, thực hiện hành động mặc định
//                    finish();
//                }
            }
        });
    }


    private void initHeaderListener() {
        mActivitySoundsBinding.header.imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickBackButton();
            }
        });
        mActivitySoundsBinding.header.imgSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SoundsActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initUI() {
        replaceFragment(new SoundsFragment());
    }
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layout_sounds_content, fragment);
//        transaction.addToBackStack(null);
        transaction.commit();

    }
    private void onClickBackButton(){
        if (mTypeSound == TYPE_SOUNDS) finish();
        else {
            replaceFragment(new SoundsFragment());
            mTypeSound = TYPE_SOUNDS;
            initHeaderUI();
        }
    }
    public static void initHeaderUI(){
        switch (mTypeSound){
            case TYPE_SOUNDS:
                mActivitySoundsBinding.header.tvTitle.setText(TEXT_SOUNDS);
                break;
            case TYPE_FORTE_PIANO:
                mActivitySoundsBinding.header.tvTitle.setText(TEXT_FORTE_PIANO);
                break;
            case TYPE_FUNNY:
                mActivitySoundsBinding.header.tvTitle.setText(TEXT_FUNNY);
                break;
            case TYPE_HAIR_CLIPPER:
                mActivitySoundsBinding.header.tvTitle.setText(TEXT_HAIR_CLIPPER);
                break;
            case TYPE_HILARIOUS:
                mActivitySoundsBinding.header.tvTitle.setText(TEXT_HILARIOUS);
                break;
        }
    }



}