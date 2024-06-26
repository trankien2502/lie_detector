package com.example.baseproduct;

import android.app.Application;

import com.example.baseproduct.util.SharePrefUtils;

public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        SharePrefUtils.init(this);

    }

}

