package com.studio.googleplay.fragment;

import android.view.View;
import android.widget.TextView;

import com.studio.googleplay.utils.UiUtils;
import com.studio.googleplay.view.LoadingPager;

/**
 * 首页的fragment
 */
public class HomeFragment extends BaseFragment {

    @Override
    public View onCreateSuccessView() {
        TextView tv = new TextView(UiUtils.getContext());
        tv.setText(getClass().getSimpleName());
        return tv;
    }

    @Override
    public LoadingPager.ResultState onLoad() {
        return LoadingPager.ResultState.STATE_SUCCESS;
    }
}
