package com.vtdglobal.liedetector.model;

public class Language {
    public static final String ENGLISH = "en";
    public static final String FRENCH = "fr";
    public static final String PORTUGUESE = "pt";
    public static final String GERMAN = "de";
    public static final String SPANISH = "es";
    public static final String HINDI = "hi";
    public static final String INDONESIA = "in";
    public static final String CHINA = "zh";

    public static final String LANGUAGE_0 = "English";
    public static final String LANGUAGE_1 = "French";
    public static final String LANGUAGE_2 = "Portuguese";
    public static final String LANGUAGE_3 = "German";
    public static final String LANGUAGE_4 = "Spanish";
    public static final String LANGUAGE_5 = "Hindi";
    public static final String LANGUAGE_6 = "Indonesia";
    public static final String LANGUAGE_7 = "China";

    private String code;
    private String name;
    private int image;
    private boolean isActive;

    public Language(String code, String name, int image) {
        this.code = code;
        this.name = name;
        this.image = image;
    }

    public Language(String code, String name, int image, boolean isActive) {
        this.code = code;
        this.name = name;
        this.image = image;
        this.isActive = isActive;
    }

    public Language() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
