package com.vtdglobal.liedetector.fragment;

import android.annotation.SuppressLint;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.vtdglobal.liedetector.R;
import com.vtdglobal.liedetector.databinding.FragmentFortePianoBinding;


public class FortePianoFragment extends Fragment {
    FragmentFortePianoBinding mFragmentFortePianoBinding;
    MediaPlayer mediaPlayer;
//    Handler handler = new Handler();
//    Runnable runnable;
    private SoundPool soundPool;
    ImageView[] pianoNote = null;
    int[] soundNote = null;
    int note;
    private final int[] soundIds = new int[46];

    public static  final int TYPE_C = 0;
    public static  final int TYPE_C_SHARP = 1;
    public static  final int TYPE_D = 2;
    public static  final int TYPE_D_SHARP = 3;
    public static  final int TYPE_E = 4;
    public static  final int TYPE_F = 5;
    public static  final int TYPE_F_SHARP = 6;
    public static  final int TYPE_G = 7;
    public static  final int TYPE_G_SHARP = 8;
    public static  final int TYPE_A = 9;
    public static  final int TYPE_A_SHARP = 10;
    public static  final int TYPE_B = 11;

    public static int mType = TYPE_C;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentFortePianoBinding = FragmentFortePianoBinding.inflate(inflater,container,false);
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(10)
                .setAudioAttributes(audioAttributes)
                .build();
        initUI();
        initData();
        initNoteListener();
        initPianoListener();



        return mFragmentFortePianoBinding.getRoot();
    }

    private void initPianoListener() {
        for (int i = 0;i<35;i++){
            pianoNoteListenner(i);
        }
    }
    private void initData(){
        pianoNote = new ImageView[]{
                mFragmentFortePianoBinding.imgPiano0,mFragmentFortePianoBinding.imgPiano1,mFragmentFortePianoBinding.imgPiano2,
                mFragmentFortePianoBinding.imgPiano3,mFragmentFortePianoBinding.imgPiano4,mFragmentFortePianoBinding.imgPiano5,
                mFragmentFortePianoBinding.imgPiano6,mFragmentFortePianoBinding.imgPiano7,mFragmentFortePianoBinding.imgPiano8,
                mFragmentFortePianoBinding.imgPiano9,mFragmentFortePianoBinding.imgPiano10,mFragmentFortePianoBinding.imgPiano11,
                mFragmentFortePianoBinding.imgPiano12,mFragmentFortePianoBinding.imgPiano13,mFragmentFortePianoBinding.imgPiano14,
                mFragmentFortePianoBinding.imgPiano15,mFragmentFortePianoBinding.imgPiano16,mFragmentFortePianoBinding.imgPiano17,
                mFragmentFortePianoBinding.imgPiano18,mFragmentFortePianoBinding.imgPiano19,mFragmentFortePianoBinding.imgPiano20,
                mFragmentFortePianoBinding.imgPiano21,mFragmentFortePianoBinding.imgPiano22,mFragmentFortePianoBinding.imgPiano23,
                mFragmentFortePianoBinding.imgPiano24,mFragmentFortePianoBinding.imgPiano25,mFragmentFortePianoBinding.imgPiano26,
                mFragmentFortePianoBinding.imgPiano27,mFragmentFortePianoBinding.imgPiano28,mFragmentFortePianoBinding.imgPiano29,
                mFragmentFortePianoBinding.imgPiano30,mFragmentFortePianoBinding.imgPiano31,mFragmentFortePianoBinding.imgPiano32,
                mFragmentFortePianoBinding.imgPiano33,mFragmentFortePianoBinding.imgPiano34};
        soundNote = new int[]{
                R.raw.s31,R.raw.s32,R.raw.s33,R.raw.s34,R.raw.s35,R.raw.s36,R.raw.s37,
                R.raw.s38,R.raw.s39,R.raw.s40,R.raw.s41,R.raw.s42,R.raw.s43,R.raw.s44,
                R.raw.s45,R.raw.s46,R.raw.s47,R.raw.s48,R.raw.s49,R.raw.s50,R.raw.s51,
                R.raw.s52,R.raw.s53,R.raw.s54,R.raw.s55,R.raw.s56,R.raw.s57,R.raw.s58,
                R.raw.s59,R.raw.s60,R.raw.s61,R.raw.s62,R.raw.s63,R.raw.s64,R.raw.s65,
                R.raw.s66,R.raw.s67,R.raw.s68,R.raw.s69,R.raw.s70,R.raw.s71,R.raw.s72,
                R.raw.s73,R.raw.s74,R.raw.s75,R.raw.s76
        };
        for (int i=0;i<46;i++){
            soundIds[i] = soundPool.load(getContext(), soundNote[i], 1);
        }
    }
    private void setImageNoteResourse(int i,int firstNote, int backNote, int endNote, int whiteNote){
        switch (i){
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
        setImageNoteResourse(i,R.drawable.img_black_note_below_default,R.drawable.img_black_note_default,R.drawable.img_black_note_above_default,R.drawable.img_white_note_default);
        pianoNote[i].setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        setImageNoteResourse(i,R.drawable.img_black_note_below_press,R.drawable.img_black_note_press,R.drawable.img_black_note_above_press,R.drawable.img_white_note_press);
                        soundPool.play(soundIds[i+note], 1, 1, 0, 0, 1);
                        break;
                    case MotionEvent.ACTION_UP:
                        //runnable = () -> pianoNote[i].setImageResource(R.drawable.img_white_note_default);
                        setImageNoteResourse(i,R.drawable.img_black_note_below_default,R.drawable.img_black_note_default,R.drawable.img_black_note_above_default,R.drawable.img_white_note_default);
                        break;
                }
                return true;
            }
        });
    }
    private void getNote(){
        switch (mType){
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
    private void initUI(){
        unSelectedNote();
        getNote();
        switch (mType){
            case TYPE_C:
                mFragmentFortePianoBinding.noteC.setVisibility(View.VISIBLE);
                break;
            case TYPE_C_SHARP:
                mFragmentFortePianoBinding.noteCSharp.setVisibility(View.VISIBLE);
                break;
            case TYPE_D:
                mFragmentFortePianoBinding.noteD.setVisibility(View.VISIBLE);
                break;
            case TYPE_D_SHARP:
                mFragmentFortePianoBinding.noteDSharp.setVisibility(View.VISIBLE);
                break;
            case TYPE_E:
                mFragmentFortePianoBinding.noteE.setVisibility(View.VISIBLE);
                break;
            case TYPE_F:
                mFragmentFortePianoBinding.noteF.setVisibility(View.VISIBLE);
                break;
            case TYPE_F_SHARP:
                mFragmentFortePianoBinding.noteFSharp.setVisibility(View.VISIBLE);
                break;
            case TYPE_G:
                mFragmentFortePianoBinding.noteG.setVisibility(View.VISIBLE);
                break;
            case TYPE_G_SHARP:
                mFragmentFortePianoBinding.noteGSharp.setVisibility(View.VISIBLE);
                break;
            case TYPE_A:
                mFragmentFortePianoBinding.noteA.setVisibility(View.VISIBLE);
                break;
            case TYPE_A_SHARP:
                mFragmentFortePianoBinding.noteASharp.setVisibility(View.VISIBLE);
                break;
            case TYPE_B:
                mFragmentFortePianoBinding.noteB.setVisibility(View.VISIBLE);
                break;
        }
    }
    private void unSelectedNote(){
        mFragmentFortePianoBinding.noteC.setVisibility(View.GONE);
        mFragmentFortePianoBinding.noteCSharp.setVisibility(View.GONE);
        mFragmentFortePianoBinding.noteD.setVisibility(View.GONE);
        mFragmentFortePianoBinding.noteDSharp.setVisibility(View.GONE);
        mFragmentFortePianoBinding.noteE.setVisibility(View.GONE);
        mFragmentFortePianoBinding.noteF.setVisibility(View.GONE);
        mFragmentFortePianoBinding.noteFSharp.setVisibility(View.GONE);
        mFragmentFortePianoBinding.noteG.setVisibility(View.GONE);
        mFragmentFortePianoBinding.noteGSharp.setVisibility(View.GONE);
        mFragmentFortePianoBinding.noteA.setVisibility(View.GONE);
        mFragmentFortePianoBinding.noteASharp.setVisibility(View.GONE);
        mFragmentFortePianoBinding.noteB.setVisibility(View.GONE);
    }
    private void initNoteListener() {
        mFragmentFortePianoBinding.tvNoteA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mType = TYPE_A;
                initUI();
            }
        });
        mFragmentFortePianoBinding.tvNoteASharp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mType = TYPE_A_SHARP;
                initUI();
            }
        });
        mFragmentFortePianoBinding.tvNoteB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mType = TYPE_B;
                initUI();
            }
        });
        mFragmentFortePianoBinding.tvNoteC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mType = TYPE_C;
                initUI();
            }
        });
        mFragmentFortePianoBinding.tvNoteCSharp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mType = TYPE_C_SHARP;
                initUI();
            }
        });
        mFragmentFortePianoBinding.tvNoteD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mType = TYPE_D;
                initUI();
            }
        });
        mFragmentFortePianoBinding.tvNoteDSharp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mType = TYPE_D_SHARP;
                initUI();
            }
        });
        mFragmentFortePianoBinding.tvNoteE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mType = TYPE_E;
                initUI();
            }
        });
        mFragmentFortePianoBinding.tvNoteF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mType = TYPE_F;
                initUI();
            }
        });
        mFragmentFortePianoBinding.tvNoteFSharp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mType = TYPE_F_SHARP;
                initUI();
            }
        });
        mFragmentFortePianoBinding.tvNoteG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mType = TYPE_G;
                initUI();
            }
        });
        mFragmentFortePianoBinding.tvNoteGSharp.setOnClickListener(new View.OnClickListener() {
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