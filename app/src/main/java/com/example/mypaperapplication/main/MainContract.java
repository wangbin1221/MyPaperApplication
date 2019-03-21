package com.example.mypaperapplication.main;

import com.example.mypaperapplication.base.BasePresenter;
import com.example.mypaperapplication.base.BaseView;

/**
 * 定义主页面的契约接口
 * Created by Administrator on 2019/3/21.
 */

public interface MainContract {

    interface MainView extends BaseView<MainPresenter>{
        void showApkData(PluginItem[] packages);
        void showError();
        void showMessage(String message);
    }

    interface MainPresenter extends BasePresenter{
        void findApk();
    }

}
