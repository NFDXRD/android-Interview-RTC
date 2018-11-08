package com.zijingdemo;

import android.app.Application;

import com.zjrtc.ZjRTCViewManager;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ZjRTCViewManager.init(this);
    }
}
