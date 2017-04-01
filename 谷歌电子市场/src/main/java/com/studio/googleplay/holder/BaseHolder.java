package com.studio.googleplay.holder;

import android.view.View;

/**
 * Created by Administrator on 2017/3/31.
 */
public abstract class BaseHolder<T> {

    private final View mRootView;
    private T data;

    public BaseHolder() {
        //1.加载布局  2.findViewById
        mRootView = initView();
        //3.设置tag
        mRootView.setTag(this);
    }

    public View getRootView() {
        return mRootView;
    }

    //设置当前item的数据
    public void setData(T data) {
        this.data = data;
        refreshView(data);
    }

    //获取当前Item的数据
    public T getData() {
        return data;
    }

    //初始化item的布局,子类完成
    public abstract View initView();
    //根据数据刷新界面,子类完成
    public abstract void refreshView(T data);
}
