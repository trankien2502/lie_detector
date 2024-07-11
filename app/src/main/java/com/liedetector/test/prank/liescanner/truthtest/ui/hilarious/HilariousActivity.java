package com.liedetector.test.prank.liescanner.truthtest.ui.hilarious;

import android.content.Intent;
import android.media.MediaPlayer;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.GridLayoutManager;

import com.liedetector.test.prank.liescanner.truthtest.R;
import com.liedetector.test.prank.liescanner.truthtest.base.BaseActivity;
import com.liedetector.test.prank.liescanner.truthtest.databinding.ActivityHilariousBinding;
import com.liedetector.test.prank.liescanner.truthtest.ui.funny.adapter.SoundAdapter;
import com.liedetector.test.prank.liescanner.truthtest.ui.setting.SettingActivity;
import com.liedetector.test.prank.liescanner.truthtest.ui.sound.model.Sound;
import com.liedetector.test.prank.liescanner.truthtest.util.EventTracking;

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
        EventTracking.logEvent(this,"hilarious_view");
        getListSoundHilarious();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        binding.rcvSoundHilarious.setLayoutManager(gridLayoutManager);

        soundAdapter = new SoundAdapter(this, soundList, this::onClickSoundHilarious);
        binding.rcvSoundHilarious.setAdapter(soundAdapter);
        loadBanner(binding.rlBanner);
    }

    @Override
    public void bindView() {
        binding.header.imgLeft.setOnClickListener(view -> {
            setResult(RESULT_OK);
            onBackPressed();
        });

        binding.header.imgSetting.setOnClickListener(view -> {
            resultLauncher.launch(new Intent(this,SettingActivity.class));
            EventTracking.logEvent(this,"scanner_setting_click");
        });


    }
    public ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            try {
                loadBanner(binding.rlBanner);
            } catch (Exception e){
                binding.rlBanner.setVisibility(View.GONE);
            }
        }
    });

    private void onClickSoundHilarious(Sound sound) {
        switch (sound.getId()){
            case Sound.SOUND_ID_9:
                EventTracking.logEvent(this,"hilarious_sound_click","","cat");
                break;
            case Sound.SOUND_ID_10:
                EventTracking.logEvent(this,"hilarious_sound_click","","broke");
                break;
            case Sound.SOUND_ID_11:
                EventTracking.logEvent(this,"hilarious_sound_click","","mouse");
                break;
            case Sound.SOUND_ID_12:
                EventTracking.logEvent(this,"hilarious_sound_click","","dog");
                break;
            case Sound.SOUND_ID_13:
                EventTracking.logEvent(this,"hilarious_sound_click","","bomb");
                break;
            case Sound.SOUND_ID_14:
                EventTracking.logEvent(this,"hilarious_sound_click","","fart");
                break;
            case Sound.SOUND_ID_15:
                EventTracking.logEvent(this,"hilarious_sound_click","","door");
                break;
            case Sound.SOUND_ID_16:
                EventTracking.logEvent(this,"hilarious_sound_click","","ghost");
                break;

        }
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

    @Override
    protected void onResume() {
        super.onResume();
        //loadBanner(binding.rlBanner);
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        setResult(RESULT_OK);
        finish();
    }
}
