package com.liedetector.test.prank.liescanner.truthtest;

import android.app.Application;

import com.liedetector.test.prank.liescanner.truthtest.util.SharePrefUtils;

public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        SharePrefUtils.init(this);

    }

}

