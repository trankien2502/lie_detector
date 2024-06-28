package com.vtdglobal.liedetector.model;

public class Language {
    public static final int ENGLISH = 0;
    public static final int FRENCH = 1;
    public static final int PORTUGUESE = 2;
    public static final int GERMAN = 3;
    public static final int SPANISH = 4;
    public static final int HINDI = 5;
    public static final int INDONESIA = 6;
    public static final int CHINA = 7;

    public static final String LANGUAGE_0 = "English";
    public static final String LANGUAGE_1 = "French";
    public static final String LANGUAGE_2 = "Portuguese";
    public static final String LANGUAGE_3 = "German";
    public static final String LANGUAGE_4 = "Spanish";
    public static final String LANGUAGE_5 = "Hindi";
    public static final String LANGUAGE_6 = "Indonesia";
    public static final String LANGUAGE_7 = "China";

    private int id;
    private String name;
    private int image;
    private boolean isActive;

    public Language(int id, String name, int image) {
        this.id = id;
        this.name = name;
        this.image = image;
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
