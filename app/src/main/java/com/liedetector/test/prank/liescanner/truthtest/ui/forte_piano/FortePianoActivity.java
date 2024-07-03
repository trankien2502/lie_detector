package com.liedetector.test.prank.liescanner.truthtest.ui.forte_piano;

import android.annotation.SuppressLint;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.liedetector.test.prank.liescanner.truthtest.R;
import com.liedetector.test.prank.liescanner.truthtest.base.BaseActivity;
import com.liedetector.test.prank.liescanner.truthtest.databinding.ActivityFortePianoBinding;
import com.liedetector.test.prank.liescanner.truthtest.ui.setting.SettingActivity;
import com.liedetector.test.prank.liescanner.truthtest.util.EventTracking;

public class FortePianoActivity extends BaseActivity<ActivityFortePianoBinding> {

    MediaPlayer mediaPlayer;
    private SoundPool soundPool;
    ImageView[] pianoNote = null;
    int[] soundNote = null;
    int note;
    private final int[] soundIds = new int[46];

    public static final int TYPE_C = 0;
    public static final int TYPE_C_SHARP = 1;
    public static final int TYPE_D = 2;
    public static final int TYPE_D_SHARP = 3;
    public static final int TYPE_E = 4;
    public static final int TYPE_F = 5;
    public static final int TYPE_F_SHARP = 6;
    public static final int TYPE_G = 7;
    public static final int TYPE_G_SHARP = 8;
    public static final int TYPE_A = 9;
    public static final int TYPE_A_SHARP = 10;
    public static final int TYPE_B = 11;
    public static int mType = TYPE_C;

    @Override
    public ActivityFortePianoBinding getBinding() {
        return ActivityFortePianoBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        binding.header.tvTitle.setText(getString(R.string.forte_piano));
        EventTracking.logEvent(this,"piano_view");
        AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build();
        soundPool = new SoundPool.Builder().setMaxStreams(10).setAudioAttributes(audioAttributes).build();
        initUI();
        initData();
    }

    @Override
    public void bindView() {
        binding.header.imgLeft.setOnClickListener(view -> onBackPressed());

        binding.header.imgSetting.setOnClickListener(view -> {
            EventTracking.logEvent(this,"scanner_setting_click");
            startNextActivity(SettingActivity.class, null);
        });

        initNoteListener();
        initPianoListener();
    }

    private void initPianoListener() {
        for (int i = 0; i < 35; i++) {
            pianoNoteListenner(i);
        }
    }

    private void initData() {
        pianoNote = new ImageView[]{binding.imgPiano0, binding.imgPiano1, binding.imgPiano2, binding.imgPiano3, binding.imgPiano4, binding.imgPiano5, binding.imgPiano6, binding.imgPiano7, binding.imgPiano8, binding.imgPiano9, binding.imgPiano10, binding.imgPiano11, binding.imgPiano12, binding.imgPiano13, binding.imgPiano14, binding.imgPiano15, binding.imgPiano16, binding.imgPiano17, binding.imgPiano18, binding.imgPiano19, binding.imgPiano20, binding.imgPiano21, binding.imgPiano22, binding.imgPiano23, binding.imgPiano24, binding.imgPiano25, binding.imgPiano26, binding.imgPiano27, binding.imgPiano28, binding.imgPiano29, binding.imgPiano30, binding.imgPiano31, binding.imgPiano32, binding.imgPiano33, binding.imgPiano34};
        soundNote = new int[]{R.raw.s31, R.raw.s32, R.raw.s33, R.raw.s34, R.raw.s35, R.raw.s36, R.raw.s37, R.raw.s38, R.raw.s39, R.raw.s40, R.raw.s41, R.raw.s42, R.raw.s43, R.raw.s44, R.raw.s45, R.raw.s46, R.raw.s47, R.raw.s48, R.raw.s49, R.raw.s50, R.raw.s51, R.raw.s52, R.raw.s53, R.raw.s54, R.raw.s55, R.raw.s56, R.raw.s57, R.raw.s58, R.raw.s59, R.raw.s60, R.raw.s61, R.raw.s62, R.raw.s63, R.raw.s64, R.raw.s65, R.raw.s66, R.raw.s67, R.raw.s68, R.raw.s69, R.raw.s70, R.raw.s71, R.raw.s72, R.raw.s73, R.raw.s74, R.raw.s75, R.raw.s76};
        for (int i = 0; i < 46; i++) {
            soundIds[i] = soundPool.load(this, soundNote[i], 1);
        }
    }

    private void setImageNoteResourse(int i, int firstNote, int backNote, int endNote, int whiteNote) {
        switch (i) {
            case 0:
                pianoNote[i].setImageResource(firstNote);
                break;
            case 3:
            case 5:
            case 7:
            case 10:
            case 12:
            case 15:
            case 17:
            case 19:
            case 22:
            case 24:
            case 27:
            case 29:
            case 31:
                pianoNote[i].setImageResource(backNote);
                break;
            case 34:
                pianoNote[i].setImageResource(endNote);
                break;
            default:
                pianoNote[i].setImageResource(whiteNote);
                break;

        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void pianoNoteListenner(int i) {
        setImageNoteResourse(i, R.drawable.img_black_note_below_default, R.drawable.img_black_note_default, R.drawable.img_black_note_above_default, R.drawable.img_white_note_default);
        pianoNote[i].setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        setImageNoteResourse(i, R.drawable.img_black_note_below_press, R.drawable.img_black_note_press, R.drawable.img_black_note_above_press, R.drawable.img_white_note_press);
                        soundPool.play(soundIds[i + note], 1, 1, 0, 0, 1);
                        break;
                    case MotionEvent.ACTION_UP:
                        //runnable = () -> pianoNote[i].setImageResource(R.drawable.img_white_note_default);
                        setImageNoteResourse(i, R.drawable.img_black_note_below_default, R.drawable.img_black_note_default, R.drawable.img_black_note_above_default, R.drawable.img_white_note_default);
                        break;
                }
                return true;
            }
        });
    }

    private void getNote() {
        switch (mType) {
            case TYPE_C:
                note = 0;
                break;
            case TYPE_C_SHARP:
                note = 1;
                break;
            case TYPE_D:
                note = 2;
                break;
            case TYPE_D_SHARP:
                note = 3;
                break;
            case TYPE_E:
                note = 4;
                break;
            case TYPE_F:
                note = 5;
                break;
            case TYPE_F_SHARP:
                note = 6;
                break;
            case TYPE_G:
                note = 7;
                break;
            case TYPE_G_SHARP:
                note = 8;
                break;
            case TYPE_A:
                note = 9;
                break;
            case TYPE_A_SHARP:
                note = 10;
                break;
            case TYPE_B:
                note = 11;
                break;
        }
    }

    private void initUI() {
        unSelectedNote();
        getNote();
        switch (mType) {
            case TYPE_C:
                binding.noteC.setVisibility(View.VISIBLE);
                break;
            case TYPE_C_SHARP:
                binding.noteCSharp.setVisibility(View.VISIBLE);
                break;
            case TYPE_D:
                binding.noteD.setVisibility(View.VISIBLE);
                break;
            case TYPE_D_SHARP:
                binding.noteDSharp.setVisibility(View.VISIBLE);
                break;
            case TYPE_E:
                binding.noteE.setVisibility(View.VISIBLE);
                break;
            case TYPE_F:
                binding.noteF.setVisibility(View.VISIBLE);
                break;
            case TYPE_F_SHARP:
                binding.noteFSharp.setVisibility(View.VISIBLE);
                break;
            case TYPE_G:
                binding.noteG.setVisibility(View.VISIBLE);
                break;
            case TYPE_G_SHARP:
                binding.noteGSharp.setVisibility(View.VISIBLE);
                break;
            case TYPE_A:
                binding.noteA.setVisibility(View.VISIBLE);
                break;
            case TYPE_A_SHARP:
                binding.noteASharp.setVisibility(View.VISIBLE);
                break;
            case TYPE_B:
                binding.noteB.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void unSelectedNote() {
        binding.noteC.setVisibility(View.GONE);
        binding.noteCSharp.setVisibility(View.GONE);
        binding.noteD.setVisibility(View.GONE);
        binding.noteDSharp.setVisibility(View.GONE);
        binding.noteE.setVisibility(View.GONE);
        binding.noteF.setVisibility(View.GONE);
        binding.noteFSharp.setVisibility(View.GONE);
        binding.noteG.setVisibility(View.GONE);
        binding.noteGSharp.setVisibility(View.GONE);
        binding.noteA.setVisibility(View.GONE);
        binding.noteASharp.setVisibility(View.GONE);
        binding.noteB.setVisibility(View.GONE);
    }

    private void initNoteListener() {
        binding.tvNoteA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mType = TYPE_A;
                initUI();
            }
        });
        binding.tvNoteASharp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mType = TYPE_A_SHARP;
                initUI();
            }
        });
        binding.tvNoteB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mType = TYPE_B;
                initUI();
            }
        });
        binding.tvNoteC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mType = TYPE_C;
                initUI();
            }
        });
        binding.tvNoteCSharp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mType = TYPE_C_SHARP;
                initUI();
            }
        });
        binding.tvNoteD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mType = TYPE_D;
                initUI();
            }
        });
        binding.tvNoteDSharp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mType = TYPE_D_SHARP;
                initUI();
            }
        });
        binding.tvNoteE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mType = TYPE_E;
                initUI();
            }
        });
        binding.tvNoteF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mType = TYPE_F;
                initUI();
            }
        });
        binding.tvNoteFSharp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mType = TYPE_F_SHARP;
                initUI();
            }
        });
        binding.tvNoteG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mType = TYPE_G;
                initUI();
            }
        });
        binding.tvNoteGSharp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mType = TYPE_G_SHARP;
                initUI();
            }
        });
    }

    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        soundPool.release();
        soundPool = null;
    }

}
