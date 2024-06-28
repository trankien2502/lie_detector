package com.vtdglobal.liedetector.fragment;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.vtdglobal.liedetector.R;
import com.vtdglobal.liedetector.activity.SoundsActivity;
import com.vtdglobal.liedetector.adapter.SoundAdapter;
import com.vtdglobal.liedetector.adapter.SoundsAdapter;
import com.vtdglobal.liedetector.databinding.FragmentFunnyBinding;
import com.vtdglobal.liedetector.databinding.FragmentSoundsBinding;
import com.vtdglobal.liedetector.model.Sound;

import java.util.ArrayList;
import java.util.List;

public class SoundsFragment extends Fragment {

    FragmentSoundsBinding mFragmentSoundsBinding;
    List<Sound> soundsList;
    SoundsAdapter soundsAdapter;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentSoundsBinding = FragmentSoundsBinding.inflate(inflater,container,false);
        getListSoundFunny();
        initUI();
        initListener();
        return mFragmentSoundsBinding.getRoot();

    }

    private void initListener() {
    }

    private void initUI() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        mFragmentSoundsBinding.rcvSounds.setLayoutManager(gridLayoutManager);

        soundsAdapter = new SoundsAdapter(getContext(),soundsList,this::onClickSoundFunny);
        mFragmentSoundsBinding.rcvSounds.setAdapter(soundsAdapter);
    }

    private void onClickSoundFunny(Sound sound) {
        switch (sound.getId()){
            case Sound.SOUND_ID_17:
                replaceFragment(new HairClipperFragment());
                SoundsActivity.mTypeSound = SoundsActivity.TYPE_HAIR_CLIPPER;
                SoundsActivity.initHeaderUI();
                break;
            case Sound.SOUND_ID_18:
                replaceFragment(new FunnyFragment());
                SoundsActivity.mTypeSound = SoundsActivity.TYPE_FUNNY;
                SoundsActivity.initHeaderUI();
                break;
            case Sound.SOUND_ID_19:
                replaceFragment(new HilariousFragment());
                SoundsActivity.mTypeSound = SoundsActivity.TYPE_HILARIOUS;
                SoundsActivity.initHeaderUI();
                break;
            case Sound.SOUND_ID_20:
                replaceFragment(new FortePianoFragment());
                SoundsActivity.mTypeSound = SoundsActivity.TYPE_FORTE_PIANO;
                SoundsActivity.initHeaderUI();
                break;
        }
    }

    private void getListSoundFunny() {
        soundsList = new ArrayList<>();
        soundsList.add(new Sound(Sound.SOUND_ID_17,getString(R.string.hair_clipper),R.drawable.img_clipper));
        soundsList.add(new Sound(Sound.SOUND_ID_18,getString(R.string.funny),R.drawable.img_audio_wave));
        soundsList.add(new Sound(Sound.SOUND_ID_19,getString(R.string.hilarious),R.drawable.img_sound_wave1));
        soundsList.add(new Sound(Sound.SOUND_ID_20,getString(R.string.forte_piano),R.drawable.img_piano1));


    }
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.layout_sounds_content, fragment);
        //transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();

    }


}