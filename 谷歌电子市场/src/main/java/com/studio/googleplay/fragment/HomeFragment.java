package com.studio.googleplay.fragment;

import android.view.View;

import com.studio.googleplay.adapter.MyBaseAdapter;
import com.studio.googleplay.domain.AppInfo;
import com.studio.googleplay.holder.BaseHolder;
import com.studio.googleplay.holder.HomeHolder;
import com.studio.googleplay.http.protocol.HomeProtocol;
import com.studio.googleplay.utils.UiUtils;
import com.studio.googleplay.view.LoadingPager;
import com.studio.googleplay.view.MyListView;

import java.util.ArrayList;

/**
 * 首页的fragment
 */
public class HomeFragment extends BaseFragment {

    private ArrayList<AppInfo> data;

    @Override
    public View onCreateSuccessView() {
        MyListView lv = new MyListView(UiUtils.getContext());
        lv.setAdapter(new HomeAdapter(data));
        return lv;
    }

    @Override
    public LoadingPager.ResultState onLoad() {
        HomeProtocol protocol = new HomeProtocol();
        data = protocol.getData(0);
        return check(data);
    }

    private class HomeAdapter extends MyBaseAdapter<AppInfo> {

        public HomeAdapter(ArrayList<AppInfo> dataList) {
            super(dataList);
        }

        @Override
        public BaseHolder<AppInfo> getHolder() {
            return new HomeHolder();
        }

        @Override
        public ArrayList<AppInfo> onLoadMore() {
            HomeProtocol protocol = new HomeProtocol();
            ArrayList<AppInfo> moreData = protocol.getData(getListSize());
            return moreData;
        }
    }
}