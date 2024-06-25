package com.vtdglobal.liedetector.fragment;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vtdglobal.liedetector.R;
import com.vtdglobal.liedetector.databinding.FragmentHairClipperBinding;

public class HairClipperFragment extends Fragment {

    FragmentHairClipperBinding mFragmentHairClipperBinding;
    boolean isClipperOn = false;
    MediaPlayer mediaPlayer;
    private int currentPosition;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentHairClipperBinding = FragmentHairClipperBinding.inflate(inflater,container,false);
        initListener();
        return mFragmentHairClipperBinding.getRoot();
    }

    private void initListener() {
        mFragmentHairClipperBinding.imgClipper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isClipperOn){
                    isClipperOn = false;
                    mFragmentHairClipperBinding.imgClipper.setImageResource(R.drawable.img_clipper_off);
                    onClickClipperOff();
                } else {
                    isClipperOn = true;
                    mFragmentHairClipperBinding.imgClipper.setImageResource(R.drawable.img_clipper_on);
                    onClickClipperOn();
                }
            }
        });
    }

    private void onClickClipperOn() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(getActivity(), R.raw.hair_clipper);
            mediaPlayer.seekTo(0);
            mediaPlayer.start();
        } else if (!mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(currentPosition);
            mediaPlayer.start();
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.seekTo(0);
                mp.start();
            }
        });
    }

    private void onClickClipperOff() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            currentPosition = mediaPlayer.getCurrentPosition();
        }
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