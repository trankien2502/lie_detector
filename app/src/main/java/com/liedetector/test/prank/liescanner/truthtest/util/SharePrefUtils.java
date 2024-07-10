package com.liedetector.test.prank.liescanner.truthtest.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharePrefUtils {
    public static String email = "admobah21@gmail.com";
    public static String subject = "Lie Detector Test - Prank";
    private static SharedPreferences mSharePref;

    public static void init(Context context) {
        if (mSharePref == null) {
            mSharePref = PreferenceManager.getDefaultSharedPreferences(context);
        }
    }

    public static void putString(String key, String value) {
        SharedPreferences.Editor edit = mSharePref.edit();
        edit.putString(key, value);
        edit.apply();
    }

    public static String getString(String key, String value) {
        return mSharePref.getString(key, value);
    }

    public static void putInt(String key, int value) {
        SharedPreferences.Editor edit = mSharePref.edit();
        edit.putInt(key, value);
        edit.apply();
    }

    public static int getInt(String key, int value) {
        return mSharePref.getInt(key, value);
    }

    public static void putLong(String key, long value) {
        SharedPreferences.Editor edit = mSharePref.edit();
        edit.putLong(key, value);
        edit.apply();
    }

    public static long getLong(String key, long value) {
        return mSharePref.getLong(key, value);
    }

    public static void putBoolean(String key, boolean value) {
        SharedPreferences.Editor edit = mSharePref.edit();
        edit.putBoolean(key, value);
        edit.apply();
    }

    public static boolean getBoolean(String key, boolean value) {
        SharedPreferences sharedPreferences = mSharePref;
        if (sharedPreferences != null) {
            return sharedPreferences.getBoolean(key, value);
        }
        return true;
    }

    public static void increaseCountFirstHelp(Context context) {
        SharedPreferences pre = context.getSharedPreferences("dataAudio", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.putInt("first", pre.getInt("first", 0) + 1);
        editor.apply();
    }

    public static int getCountOpenFirstHelp(Context context) {
        SharedPreferences pre = context.getSharedPreferences("dataAudio", Context.MODE_PRIVATE);
        return pre.getInt("first", 0);
    }
    public static int getCountExitApp(Context context){
        SharedPreferences pre = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        return pre.getInt("countExit", 1);
    }
    public static void increaseCountExitApp(Context context) {
        SharedPreferences pre = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.putInt("countExit", pre.getInt("countExit", 1) + 1);
        editor.apply();
        editor.commit();
    }



    public static boolean isRated(Context context) {
        SharedPreferences pre = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        return pre.getBoolean("rated", false);
    }
    public static boolean getResumeBackFromSetting(Context context) {
        SharedPreferences pre = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        return pre.getBoolean("resume_back_from_setting", true);
    }
    public static void setResumeBack(Context context, boolean isResumeBackFromSetting) {
        SharedPreferences pre = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.putBoolean("resume_back_from_setting", isResumeBackFromSetting);
        editor.apply();
        editor.commit();
    }


    public static int getCountOpenApp(Context context) {
        SharedPreferences pre = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        return pre.getInt("counts", 1);
    }

    public static void increaseCountOpenApp(Context context) {
        SharedPreferences pre = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.putInt("counts", pre.getInt("counts", 0) + 1);
        editor.commit();
    }


    public static void forceRated(Context context) {
        SharedPreferences pre = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.putBoolean("rated", true);
        editor.commit();
    }
}
