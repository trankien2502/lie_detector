package com.vtdglobal.liedetector.fragment;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.vtdglobal.liedetector.R;
import com.vtdglobal.liedetector.adapter.SoundAdapter;
import com.vtdglobal.liedetector.databinding.FragmentFunnyBinding;
import com.vtdglobal.liedetector.model.Sound;

import java.util.ArrayList;
import java.util.List;


public class FunnyFragment extends Fragment {

    FragmentFunnyBinding mFragmentFunnyBinding;
    List<Sound> soundList;
    SoundAdapter soundAdapter;
    MediaPlayer mediaPlayer;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentFunnyBinding = FragmentFunnyBinding.inflate(inflater,container,false);
        getListSoundFunny();
        initUI();
        initListener();

        return mFragmentFunnyBinding.getRoot();
    }

    private void initListener() {
    }

    private void initUI() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3);
        mFragmentFunnyBinding.rcvSoundFunny.setLayoutManager(gridLayoutManager);

         soundAdapter = new SoundAdapter(soundList,this::onClickSoundFunny);
        mFragmentFunnyBinding.rcvSoundFunny.setAdapter(soundAdapter);
    }

    private void onClickSoundFunny(Sound sound) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(getActivity(), sound.getSound());
        mediaPlayer.start();

        //Toast.makeText(getContext(), "Choose "+sound.getName(), Toast.LENGTH_SHORT).show();
    }

    private void getListSoundFunny() {
        soundList = new ArrayList<>();
        soundList.add(new Sound(Sound.SOUND_ID_0,getString(R.string.sound_name_0),R.drawable.img_air_horn_1,R.raw.sound_alarm));
        soundList.add(new Sound(Sound.SOUND_ID_1,getString(R.string.sound_name_1),R.drawable.img_laugh,R.raw.sound_alarm));
        soundList.add(new Sound(Sound.SOUND_ID_2,getString(R.string.sound_name_2),R.drawable.img_clapping,R.raw.sound_alarm));
        soundList.add(new Sound(Sound.SOUND_ID_3,getString(R.string.sound_name_3),R.drawable.img_whistle,R.raw.sound_alarm));
        soundList.add(new Sound(Sound.SOUND_ID_4,getString(R.string.sound_name_4),R.drawable.img_fart,R.raw.sound_alarm));
        soundList.add(new Sound(Sound.SOUND_ID_5,getString(R.string.sound_name_5),R.drawable.img_punch,R.raw.sound_alarm));
        soundList.add(new Sound(Sound.SOUND_ID_6,getString(R.string.sound_name_6),R.drawable.img_omg,R.raw.sound_alarm));
        soundList.add(new Sound(Sound.SOUND_ID_7,getString(R.string.sound_name_7),R.drawable.img_police,R.raw.sound_alarm));
        soundList.add(new Sound(Sound.SOUND_ID_8,getString(R.string.sound_name_8),R.drawable.img_remote,R.raw.sound_alarm));

    }
}