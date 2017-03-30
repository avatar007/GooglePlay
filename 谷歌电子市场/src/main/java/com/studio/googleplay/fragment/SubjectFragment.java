package com.studio.googleplay.fragment;

import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.studio.googleplay.utils.UiUtils;
import com.studio.googleplay.view.LoadingPager;

/**
 * 主题的fragment
 */
public class SubjectFragment extends BaseFragment {

    @Override
    public View onCreateSuccessView() {
        TextView tv = new TextView(UiUtils.getContext());
        tv.setText("我是加载成功的专题页面");
        tv.setGravity(Gravity.CENTER);
        return tv;
    }

    @Override
    public LoadingPager.ResultState onLoad() {
        return LoadingPager.ResultState.STATE_SUCCESS;
    }
}
