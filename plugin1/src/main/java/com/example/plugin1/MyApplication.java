package com.example.plugin1;

import android.app.Application;
import android.content.Context;

import dagger.android.DaggerApplication;

/**
 * Created by Administrator on 2019/3/26.
 */

public class MyApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(this);
        //DaggerMyComponent.builder().build().inject(this);
    }
}
