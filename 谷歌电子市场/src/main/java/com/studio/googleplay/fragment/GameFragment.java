package com.studio.googleplay.fragment;

import android.view.View;

import com.studio.googleplay.view.LoadingPager;

/**
 * 游戏的fragment
 */
public class GameFragment extends BaseFragment {

    @Override
    public View onCreateSuccessView() {
        return null;
    }

    @Override
    public LoadingPager.ResultState onLoad() {
        return LoadingPager.ResultState.STATE_EMPTY;
    }
}
