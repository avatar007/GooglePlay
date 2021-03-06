package com.studio.googleplay.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.studio.googleplay.utils.UiUtils;
import com.studio.googleplay.view.LoadingPager;

/**
 * Created by Administrator on 2017/3/30.
 */
public abstract class BaseFragment extends Fragment {

    private LoadingPager mLoadingPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLoadingPager = new LoadingPager(UiUtils.getContext()) {
            @Override
            public View onCreateSuccessView() {
                //同名方法,要用BaseFragment类去调用
                return BaseFragment.this.onCreateSuccessView();
            }

            @Override
            public ResultState onLoad() {
                return BaseFragment.this.onLoad();
            }
        };
        return mLoadingPager;
    }

    //创建成功布局由子类去完成
    public abstract View onCreateSuccessView();

    public abstract LoadingPager.ResultState onLoad();

    public void initData() {
        mLoadingPager.initData();
    }
}
