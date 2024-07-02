package com.liedetector.test.prank.liescanner.truthtest.ui.sound;

import androidx.recyclerview.widget.GridLayoutManager;

import com.liedetector.test.prank.liescanner.truthtest.R;
import com.liedetector.test.prank.liescanner.truthtest.base.BaseActivity;
import com.liedetector.test.prank.liescanner.truthtest.databinding.ActivitySoundsBinding;
import com.liedetector.test.prank.liescanner.truthtest.ui.forte_piano.FortePianoActivity;
import com.liedetector.test.prank.liescanner.truthtest.ui.funny.FunnyActivity;
import com.liedetector.test.prank.liescanner.truthtest.ui.hair_clipper.HairClipperActivity;
import com.liedetector.test.prank.liescanner.truthtest.ui.hilarious.HilariousActivity;
import com.liedetector.test.prank.liescanner.truthtest.ui.setting.SettingActivity;
import com.liedetector.test.prank.liescanner.truthtest.ui.sound.adapter.SoundsAdapter;
import com.liedetector.test.prank.liescanner.truthtest.ui.sound.model.Sound;

import java.util.ArrayList;
import java.util.List;

public class SoundsActivity extends BaseActivity<ActivitySoundsBinding> {

    List<Sound> soundsList;
    SoundsAdapter soundsAdapter;

    @Override
    public ActivitySoundsBinding getBinding() {
        return ActivitySoundsBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        getListSoundFunny();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        binding.rcvSounds.setLayoutManager(gridLayoutManager);

        soundsAdapter = new SoundsAdapter(this, soundsList, this::onClickSoundFunny);
        binding.rcvSounds.setAdapter(soundsAdapter);
    }

    @Override
    public void bindView() {
        binding.header.imgLeft.setOnClickListener(view -> onBackPressed());

        binding.header.imgSetting.setOnClickListener(view -> startNextActivity(SettingActivity.class, null));

    }

    private void onClickSoundFunny(Sound sound) {
        switch (sound.getId()) {
            case Sound.SOUND_ID_17:
                startNextActivity(HairClipperActivity.class, null);
                break;
            case Sound.SOUND_ID_18:
                startNextActivity(FunnyActivity.class, null);
                break;
            case Sound.SOUND_ID_19:
                startNextActivity(HilariousActivity.class, null);
                break;
            case Sound.SOUND_ID_20:
                startNextActivity(FortePianoActivity.class, null);
                break;
        }
    }

    private void getListSoundFunny() {
        soundsList = new ArrayList<>();
        soundsList.add(new Sound(Sound.SOUND_ID_17, getString(R.string.hair_clipper), R.drawable.img_clipper));
        soundsList.add(new Sound(Sound.SOUND_ID_18, getString(R.string.funny), R.drawable.img_audio_wave));
        soundsList.add(new Sound(Sound.SOUND_ID_19, getString(R.string.hilarious), R.drawable.img_sound_wave1));
        soundsList.add(new Sound(Sound.SOUND_ID_20, getString(R.string.forte_piano), R.drawable.img_piano1));


    }


}