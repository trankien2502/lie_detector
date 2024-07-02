package com.liedetector.test.prank.liescanner.truthtest.ui.hilarious;

import android.media.MediaPlayer;

import androidx.recyclerview.widget.GridLayoutManager;

import com.liedetector.test.prank.liescanner.truthtest.R;
import com.liedetector.test.prank.liescanner.truthtest.base.BaseActivity;
import com.liedetector.test.prank.liescanner.truthtest.databinding.ActivityHilariousBinding;
import com.liedetector.test.prank.liescanner.truthtest.ui.funny.adapter.SoundAdapter;
import com.liedetector.test.prank.liescanner.truthtest.ui.setting.SettingActivity;
import com.liedetector.test.prank.liescanner.truthtest.ui.sound.model.Sound;

import java.util.ArrayList;
import java.util.List;

public class HilariousActivity extends BaseActivity<ActivityHilariousBinding> {

    List<Sound> soundList;
    MediaPlayer mediaPlayer;
    SoundAdapter soundAdapter;

    @Override
    public ActivityHilariousBinding getBinding() {
        return ActivityHilariousBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        binding.header.tvTitle.setText(getString(R.string.hilarious));

        getListSoundHilarious();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        binding.rcvSoundHilarious.setLayoutManager(gridLayoutManager);

        soundAdapter = new SoundAdapter(this, soundList, this::onClickSoundHilarious);
        binding.rcvSoundHilarious.setAdapter(soundAdapter);
    }

    @Override
    public void bindView() {
        binding.header.imgLeft.setOnClickListener(view -> onBackPressed());

        binding.header.imgSetting.setOnClickListener(view -> startNextActivity(SettingActivity.class, null));


    }

    private void onClickSoundHilarious(Sound sound) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(this, sound.getSound());
        mediaPlayer.start();
    }

    private void getListSoundHilarious() {
        soundList = new ArrayList<>();
        soundList.add(new Sound(Sound.SOUND_ID_9, getString(R.string.sound_name_9), R.drawable.img_cat, R.raw.cat));
        soundList.add(new Sound(Sound.SOUND_ID_10, getString(R.string.sound_name_10), R.drawable.img_broke, R.raw.broke));
        soundList.add(new Sound(Sound.SOUND_ID_11, getString(R.string.sound_name_11), R.drawable.img_mouse, R.raw.mouse));
        soundList.add(new Sound(Sound.SOUND_ID_12, getString(R.string.sound_name_12), R.drawable.img_dog, R.raw.dog));
        soundList.add(new Sound(Sound.SOUND_ID_13, getString(R.string.sound_name_13), R.drawable.img_bomb, R.raw.bomb));
        soundList.add(new Sound(Sound.SOUND_ID_14, getString(R.string.sound_name_14), R.drawable.img_fart, R.raw.fart));
        soundList.add(new Sound(Sound.SOUND_ID_15, getString(R.string.sound_name_15), R.drawable.img_door, R.raw.door));
        soundList.add(new Sound(Sound.SOUND_ID_16, getString(R.string.sound_name_16), R.drawable.img_ghost, R.raw.ghost));

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
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
