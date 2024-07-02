package com.liedetector.test.prank.liescanner.truthtest.ui.funny;

import android.media.MediaPlayer;

import androidx.recyclerview.widget.GridLayoutManager;

import com.liedetector.test.prank.liescanner.truthtest.R;
import com.liedetector.test.prank.liescanner.truthtest.base.BaseActivity;
import com.liedetector.test.prank.liescanner.truthtest.databinding.ActivityFunnyBinding;
import com.liedetector.test.prank.liescanner.truthtest.ui.funny.adapter.SoundAdapter;
import com.liedetector.test.prank.liescanner.truthtest.ui.setting.SettingActivity;
import com.liedetector.test.prank.liescanner.truthtest.ui.sound.model.Sound;

import java.util.ArrayList;
import java.util.List;

public class FunnyActivity extends BaseActivity<ActivityFunnyBinding> {

    List<Sound> soundList;
    SoundAdapter soundAdapter;
    MediaPlayer mediaPlayer;

    @Override
    public ActivityFunnyBinding getBinding() {
        return ActivityFunnyBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        binding.header.tvTitle.setText(getString(R.string.funny));

        getListSoundFunny();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        binding.rcvSoundFunny.setLayoutManager(gridLayoutManager);

        soundAdapter = new SoundAdapter(this, soundList, this::onClickSoundFunny);
        binding.rcvSoundFunny.setAdapter(soundAdapter);
    }

    @Override
    public void bindView() {
        binding.header.imgLeft.setOnClickListener(view -> onBackPressed());

        binding.header.imgSetting.setOnClickListener(view -> startNextActivity(SettingActivity.class, null));


    }

    private void onClickSoundFunny(Sound sound) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(this, sound.getSound());
        mediaPlayer.start();
    }

    private void getListSoundFunny() {
        soundList = new ArrayList<>();
        soundList.add(new Sound(Sound.SOUND_ID_0, getString(R.string.sound_name_0), R.drawable.img_air_horn_1, R.raw.air_horn));
        soundList.add(new Sound(Sound.SOUND_ID_1, getString(R.string.sound_name_1), R.drawable.img_laugh, R.raw.laugh));
        soundList.add(new Sound(Sound.SOUND_ID_2, getString(R.string.sound_name_2), R.drawable.img_clapping, R.raw.claps));
        soundList.add(new Sound(Sound.SOUND_ID_3, getString(R.string.sound_name_3), R.drawable.img_whistle, R.raw.whistle1));
        soundList.add(new Sound(Sound.SOUND_ID_4, getString(R.string.sound_name_4), R.drawable.img_fart, R.raw.fart));
        soundList.add(new Sound(Sound.SOUND_ID_5, getString(R.string.sound_name_5), R.drawable.img_punch, R.raw.punch));
        soundList.add(new Sound(Sound.SOUND_ID_6, getString(R.string.sound_name_6), R.drawable.img_omg, R.raw.ohmygod));
        soundList.add(new Sound(Sound.SOUND_ID_7, getString(R.string.sound_name_7), R.drawable.img_police, R.raw.police));
        soundList.add(new Sound(Sound.SOUND_ID_8, getString(R.string.sound_name_8), R.drawable.img_remote, R.raw.remote));

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
