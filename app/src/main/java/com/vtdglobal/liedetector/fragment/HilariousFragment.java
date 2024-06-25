package com.vtdglobal.liedetector.fragment;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.vtdglobal.liedetector.R;
import com.vtdglobal.liedetector.adapter.SoundAdapter;
import com.vtdglobal.liedetector.databinding.FragmentHilariousBinding;
import com.vtdglobal.liedetector.model.Sound;

import java.util.ArrayList;
import java.util.List;



public class HilariousFragment extends Fragment {
    List<Sound> soundList;
    FragmentHilariousBinding mFragmentHilariousBinding;
    MediaPlayer mediaPlayer;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentHilariousBinding = FragmentHilariousBinding.inflate(inflater,container,false);
        getListSoundHilarious();
        initUI();
        return mFragmentHilariousBinding.getRoot();
    }

    private void initUI() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3);
        mFragmentHilariousBinding.rcvSoundHilarious.setLayoutManager(gridLayoutManager);

        SoundAdapter soundAdapter = new SoundAdapter(getContext(),soundList,this::onClickSoundHilarious);
        mFragmentHilariousBinding.rcvSoundHilarious.setAdapter(soundAdapter);
    }

    private void onClickSoundHilarious(Sound sound) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(getActivity(), sound.getSound());
        mediaPlayer.start();
        //Toast.makeText(getContext(), "Choose "+sound.getName(), Toast.LENGTH_SHORT).show();
    }

    private void getListSoundHilarious() {
        soundList = new ArrayList<>();
        soundList.add(new Sound(Sound.SOUND_ID_9,getString(R.string.sound_name_9),R.drawable.img_cat,R.raw.cat));
        soundList.add(new Sound(Sound.SOUND_ID_10,getString(R.string.sound_name_10),R.drawable.img_broke,R.raw.broke));
        soundList.add(new Sound(Sound.SOUND_ID_11,getString(R.string.sound_name_11),R.drawable.img_mouse,R.raw.mouse));
        soundList.add(new Sound(Sound.SOUND_ID_12,getString(R.string.sound_name_12),R.drawable.img_dog,R.raw.dog));
        soundList.add(new Sound(Sound.SOUND_ID_13,getString(R.string.sound_name_13),R.drawable.img_bomb,R.raw.bomb));
        soundList.add(new Sound(Sound.SOUND_ID_14,getString(R.string.sound_name_14),R.drawable.img_fart,R.raw.fart));
        soundList.add(new Sound(Sound.SOUND_ID_15,getString(R.string.sound_name_15),R.drawable.img_door,R.raw.door));
        soundList.add(new Sound(Sound.SOUND_ID_16,getString(R.string.sound_name_16),R.drawable.img_ghost,R.raw.ghost));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}