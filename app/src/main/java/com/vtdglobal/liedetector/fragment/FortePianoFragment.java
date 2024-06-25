package com.vtdglobal.liedetector.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vtdglobal.liedetector.databinding.FragmentFortePianoBinding;


public class FortePianoFragment extends Fragment {
    FragmentFortePianoBinding mFragmentFortePianoBinding;
    public static  final int TYPE_C = 0;
    public static  final int TYPE_C_SHARP = 1;
    public static  final int TYPE_D = 2;
    public static  final int TYPE_D_SHARP = 3;
    public static  final int TYPE_E = 4;
    public static  final int TYPE_F = 5;
    public static  final int TYPE_F_SHARP = 6;
    public static  final int TYPE_G = 7;
    public static  final int TYPE_G_SHARP = 8;
    public static  final int TYPE_A = 9;
    public static  final int TYPE_A_SHARP = 10;
    public static  final int TYPE_B = 11;

    public static int mType = TYPE_C;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentFortePianoBinding = FragmentFortePianoBinding.inflate(inflater,container,false);
        initUI();
        initListener();
        return mFragmentFortePianoBinding.getRoot();
    }
    private void initUI(){
        unSelectedNote();
        switch (mType){
            case TYPE_C:
                mFragmentFortePianoBinding.noteC.setVisibility(View.VISIBLE);
                break;
            case TYPE_C_SHARP:
                mFragmentFortePianoBinding.noteCSharp.setVisibility(View.VISIBLE);
                break;
            case TYPE_D:
                mFragmentFortePianoBinding.noteD.setVisibility(View.VISIBLE);
                break;
            case TYPE_D_SHARP:
                mFragmentFortePianoBinding.noteDSharp.setVisibility(View.VISIBLE);
                break;
            case TYPE_E:
                mFragmentFortePianoBinding.noteE.setVisibility(View.VISIBLE);
                break;
            case TYPE_F:
                mFragmentFortePianoBinding.noteF.setVisibility(View.VISIBLE);
                break;
            case TYPE_F_SHARP:
                mFragmentFortePianoBinding.noteFSharp.setVisibility(View.VISIBLE);
                break;
            case TYPE_G:
                mFragmentFortePianoBinding.noteG.setVisibility(View.VISIBLE);
                break;
            case TYPE_G_SHARP:
                mFragmentFortePianoBinding.noteGSharp.setVisibility(View.VISIBLE);
                break;
            case TYPE_A:
                mFragmentFortePianoBinding.noteA.setVisibility(View.VISIBLE);
                break;
            case TYPE_A_SHARP:
                mFragmentFortePianoBinding.noteASharp.setVisibility(View.VISIBLE);
                break;
            case TYPE_B:
                mFragmentFortePianoBinding.noteB.setVisibility(View.VISIBLE);
                break;
        }
    }
    private void unSelectedNote(){
        mFragmentFortePianoBinding.noteC.setVisibility(View.GONE);
        mFragmentFortePianoBinding.noteCSharp.setVisibility(View.GONE);
        mFragmentFortePianoBinding.noteD.setVisibility(View.GONE);
        mFragmentFortePianoBinding.noteDSharp.setVisibility(View.GONE);
        mFragmentFortePianoBinding.noteE.setVisibility(View.GONE);
        mFragmentFortePianoBinding.noteF.setVisibility(View.GONE);
        mFragmentFortePianoBinding.noteFSharp.setVisibility(View.GONE);
        mFragmentFortePianoBinding.noteG.setVisibility(View.GONE);
        mFragmentFortePianoBinding.noteGSharp.setVisibility(View.GONE);
        mFragmentFortePianoBinding.noteA.setVisibility(View.GONE);
        mFragmentFortePianoBinding.noteASharp.setVisibility(View.GONE);
        mFragmentFortePianoBinding.noteB.setVisibility(View.GONE);
    }
    private void initListener() {
        mFragmentFortePianoBinding.tvNoteA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mType = TYPE_A;
                initUI();
            }
        });
        mFragmentFortePianoBinding.tvNoteASharp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mType = TYPE_A_SHARP;
                initUI();
            }
        });
        mFragmentFortePianoBinding.tvNoteB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mType = TYPE_B;
                initUI();
            }
        });
        mFragmentFortePianoBinding.tvNoteC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mType = TYPE_C;
                initUI();
            }
        });
        mFragmentFortePianoBinding.tvNoteCSharp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mType = TYPE_C_SHARP;
                initUI();
            }
        });
        mFragmentFortePianoBinding.tvNoteD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mType = TYPE_D;
                initUI();
            }
        });
        mFragmentFortePianoBinding.tvNoteDSharp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mType = TYPE_D_SHARP;
                initUI();
            }
        });
        mFragmentFortePianoBinding.tvNoteE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mType = TYPE_E;
                initUI();
            }
        });
        mFragmentFortePianoBinding.tvNoteF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mType = TYPE_F;
                initUI();
            }
        });
        mFragmentFortePianoBinding.tvNoteFSharp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mType = TYPE_F_SHARP;
                initUI();
            }
        });
        mFragmentFortePianoBinding.tvNoteG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mType = TYPE_A;
                initUI();
            }
        });
        mFragmentFortePianoBinding.tvNoteGSharp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mType = TYPE_G_SHARP;
                initUI();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (mediaPlayer != null) {
//            mediaPlayer.release();
//            mediaPlayer = null;
//        }
    }
}