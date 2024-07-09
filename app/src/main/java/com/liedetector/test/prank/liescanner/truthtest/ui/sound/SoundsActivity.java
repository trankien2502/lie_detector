package com.liedetector.test.prank.liescanner.truthtest.ui.sound;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.GridLayoutManager;

import com.ads.sapp.admob.Admob;
import com.ads.sapp.ads.CommonAd;
import com.ads.sapp.funtion.AdCallback;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.liedetector.test.prank.liescanner.truthtest.R;
import com.liedetector.test.prank.liescanner.truthtest.ads.ConstantIdAds;
import com.liedetector.test.prank.liescanner.truthtest.ads.ConstantRemote;
import com.liedetector.test.prank.liescanner.truthtest.ads.IsNetWork;
import com.liedetector.test.prank.liescanner.truthtest.base.BaseActivity;
import com.liedetector.test.prank.liescanner.truthtest.databinding.ActivitySoundsBinding;
import com.liedetector.test.prank.liescanner.truthtest.ui.forte_piano.FortePianoActivity;
import com.liedetector.test.prank.liescanner.truthtest.ui.funny.FunnyActivity;
import com.liedetector.test.prank.liescanner.truthtest.ui.hair_clipper.HairClipperActivity;
import com.liedetector.test.prank.liescanner.truthtest.ui.hilarious.HilariousActivity;
import com.liedetector.test.prank.liescanner.truthtest.ui.main.MainActivity;
import com.liedetector.test.prank.liescanner.truthtest.ui.setting.SettingActivity;
import com.liedetector.test.prank.liescanner.truthtest.ui.sound.adapter.SoundsAdapter;
import com.liedetector.test.prank.liescanner.truthtest.ui.sound.model.Sound;
import com.liedetector.test.prank.liescanner.truthtest.util.EventTracking;

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
        EventTracking.logEvent(this,"sound_view");
        getListSoundFunny();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        binding.rcvSounds.setLayoutManager(gridLayoutManager);

        soundsAdapter = new SoundsAdapter(this, soundsList, this::onClickSoundFunny);
        binding.rcvSounds.setAdapter(soundsAdapter);
        loadBanner(binding.rlBanner);
    }

    private void loadBannerSounds() {
        if (IsNetWork.haveNetworkConnection(this) && ConstantIdAds.listIDAdsBanner.size() != 0 && ConstantRemote.banner) {
            binding.rlBanner.removeAllViews();
            binding.rlBanner.addView(binding.banner);
            Admob.getInstance().loadBannerFloor(this, ConstantIdAds.listIDAdsBanner);
            binding.rlBanner.setVisibility(View.VISIBLE);
        } else {
            binding.rlBanner.setVisibility(View.GONE);
        }
    }

    @Override
    public void bindView() {
        binding.header.imgLeft.setOnClickListener(view -> onBackPressed());

        binding.header.imgSetting.setOnClickListener(view -> {
//            startNextActivity(SettingActivity.class, null);
            resultLauncher.launch(new Intent(SoundsActivity.this, SettingActivity.class));
            EventTracking.logEvent(this,"scanner_setting_click");
        });

    }
    public ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            binding.rlBanner.removeAllViews();
            RelativeLayout layout = (RelativeLayout) LayoutInflater.from(this).inflate(com.ads.sapp.R.layout.layout_banner_control, null, false);
            binding.rlBanner.addView(layout);
            if (IsNetWork.haveNetworkConnection(this) && ConstantIdAds.listIDAdsBanner.size() != 0 && ConstantRemote.banner) {
                findViewById(R.id.rlBanner).setVisibility(View.VISIBLE);
                Admob.getInstance().loadBannerFloor(this, ConstantIdAds.listIDAdsBanner);
            } else {
                findViewById(R.id.rlBanner).setVisibility(View.GONE);
            }
        }
    });

    private void onClickSoundFunny(Sound sound) {
        switch (sound.getId()) {
            case Sound.SOUND_ID_17:
                EventTracking.logEvent(this,"sound_hair_clipper_click");
                startNextActivity(HairClipperActivity.class, null);
                break;
            case Sound.SOUND_ID_18:
                EventTracking.logEvent(this,"sound_funny_click");
                startNextActivity(FunnyActivity.class, null);
                break;
            case Sound.SOUND_ID_19:
                EventTracking.logEvent(this,"sound_hilarious_click");
                startNextActivity(HilariousActivity.class, null);
                break;
            case Sound.SOUND_ID_20:
                EventTracking.logEvent(this,"sound_piano_click");
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

    @Override
    protected void onResume() {
        super.onResume();
        loadBanner(binding.rlBanner);
    }
}