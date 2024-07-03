package com.liedetector.test.prank.liescanner.truthtest.ui.hair_clipper;

import android.media.MediaPlayer;
import android.os.Vibrator;

import com.liedetector.test.prank.liescanner.truthtest.R;
import com.liedetector.test.prank.liescanner.truthtest.base.BaseActivity;
import com.liedetector.test.prank.liescanner.truthtest.databinding.ActivityHairClipperBinding;
import com.liedetector.test.prank.liescanner.truthtest.ui.setting.SettingActivity;
import com.liedetector.test.prank.liescanner.truthtest.util.EventTracking;

public class HairClipperActivity extends BaseActivity<ActivityHairClipperBinding> {

    boolean isClipperOn = false;
    MediaPlayer mediaPlayer;
    Vibrator vibrator;
    private int currentPosition;

    @Override
    public ActivityHairClipperBinding getBinding() {
        return ActivityHairClipperBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        binding.header.tvTitle.setText(getString(R.string.hair_clipper));
        EventTracking.logEvent(this,"hair_clipper_view");
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    @Override
    public void bindView() {
        binding.header.imgLeft.setOnClickListener(view -> onBackPressed());

        binding.header.imgSetting.setOnClickListener(view -> {
            startNextActivity(SettingActivity.class, null);
            EventTracking.logEvent(this,"scanner_setting_click");
        });

        binding.imgClipper.setOnClickListener(view -> {
            if (isClipperOn) {
                EventTracking.logEvent(this,"hair_clipper_sound_click","","off");
                isClipperOn = false;
                binding.imgClipper.setImageResource(R.drawable.img_clipper_off);
                onClickClipperOff();
            } else {
                EventTracking.logEvent(this,"hair_clipper_sound_click","","on");
                isClipperOn = true;
                binding.imgClipper.setImageResource(R.drawable.img_clipper_on);
                onClickClipperOn();
            }
        });
    }

    private void onClickClipperOn() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.hair_clipper);
            mediaPlayer.seekTo(0);
            mediaPlayer.start();
        } else if (!mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(currentPosition);
            mediaPlayer.start();
        }
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(new long[]{0, 1000}, 0);
        }
        mediaPlayer.setOnCompletionListener(mp -> {
            mp.seekTo(0);
            mp.start();
        });
    }

    private void onClickClipperOff() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            currentPosition = mediaPlayer.getCurrentPosition();
        }

        vibrator.cancel();

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (isClipperOn) vibrator.cancel();
        isClipperOn = false;
        binding.imgClipper.setImageResource(R.drawable.img_clipper_off);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (isClipperOn) vibrator.cancel();
    }

}
