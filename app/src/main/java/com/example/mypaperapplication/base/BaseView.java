package com.example.mypaperapplication.base;

/**
 * Created by Administrator on 2019/3/21.
 */
// 所有view都需要继承的接口
public interface BaseView<T> {
    void setPresenter(T presenter);
}
