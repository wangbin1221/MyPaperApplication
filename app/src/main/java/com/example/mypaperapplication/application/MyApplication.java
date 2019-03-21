package com.example.mypaperapplication.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

/**
 * Created by Administrator on 2019/3/21.
 */

public class MyApplication extends MultiDexApplication {
    private static Application sInstance;
    public static Application getInstance(){
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public Context getBaseContext() {
        return super.getBaseContext();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        sInstance = this;
        MultiDex.install(this);
    }
}
