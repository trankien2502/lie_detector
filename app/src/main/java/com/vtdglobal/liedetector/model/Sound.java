package com.vtdglobal.liedetector.model;

import com.vtdglobal.liedetector.R;

public class Sound {
    private int id;
    private String name;
    private int image;
    private int sound;
    //ID FUNNY
    public static final int SOUND_ID_0 = 0;
    public static final int SOUND_ID_1 = 1;
    public static final int SOUND_ID_2 = 2;
    public static final int SOUND_ID_3 = 3;
    public static final int SOUND_ID_4 = 4;
    public static final int SOUND_ID_5 = 5;
    public static final int SOUND_ID_6 = 6;
    public static final int SOUND_ID_7 = 7;
    public static final int SOUND_ID_8 = 8;
    //ID HILARIOUS
    public static final int SOUND_ID_9 = 9;
    public static final int SOUND_ID_10 = 10;
    public static final int SOUND_ID_11 = 11;
    public static final int SOUND_ID_12 = 12;
    public static final int SOUND_ID_13 = 13;
    public static final int SOUND_ID_14 = 14;
    public static final int SOUND_ID_15 = 15;
    public static final int SOUND_ID_16 = 16;

    //ID SOUNDS
    public static final int SOUND_ID_17 = 17;
    public static final int SOUND_ID_18 = 18;
    public static final int SOUND_ID_19 = 19;
    public static final int SOUND_ID_20 = 20;


    public Sound() {
    }

    public Sound(int id, String name, int image, int sound) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.sound = sound;
    }

    public Sound(int id, String name, int image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public int getSound() {
        return sound;
    }

    public void setSound(int sound) {
        this.sound = sound;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
