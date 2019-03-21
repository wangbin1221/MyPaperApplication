package com.example.mypaperapplication.main;

import android.os.Environment;

import com.example.mypaperapplication.application.MyApplication;
import com.example.mypaperapplication.constants.Constants;
import com.ryg.dynamicload.internal.DLPluginManager;
import com.ryg.utils.DLUtils;

import java.io.File;

/**
 * Created by Administrator on 2019/3/21.
 */

public class MainPresenter implements MainContract.MainPresenter {

    private MainContract.MainView mMainView;
    public MainPresenter(MainContract.MainView view){
        mMainView = view;
    }
    @Override
    public void start() {
        mMainView.setPresenter(this);
    }

    private PluginItem[] mPluginPackages;
    @Override
    public void findApk() {
        File file = new File(Environment.getExternalStorageDirectory(),Constants.APK_PATH);
        if (!file.exists()){
            mMainView.showMessage("no apk");
            file.mkdirs();
            return ;
        }
        File[] files = file.listFiles();
        mPluginPackages = new PluginItem[files.length];
        int index=0;
        for (File plugin: files){
            PluginItem item = new PluginItem();
            item.pluginPath = plugin.getAbsolutePath();
            item.packageInfo = DLUtils.getPackageInfo(MyApplication.getInstance(), item.pluginPath);
            if (item.packageInfo.activities != null && item.packageInfo.activities.length > 0) {
                item.launcherActivityName = item.packageInfo.activities[0].name;
            }
            if (item.packageInfo.services != null && item.packageInfo.services.length > 0) {
                item.launcherServiceName = item.packageInfo.services[0].name;
            }
            mPluginPackages[index] = item;
            DLPluginManager.getInstance(MyApplication.getInstance()).loadApk(item.pluginPath);
            //DLPluginPackage pluginPackage = DLPluginManager.getInstance(MyApplication.getInstance()).loadApk(plugin.toString());

            index++;
        }
        mMainView.showMessage("加载apk成功");
        mMainView.showApkData(mPluginPackages);
    }


}
