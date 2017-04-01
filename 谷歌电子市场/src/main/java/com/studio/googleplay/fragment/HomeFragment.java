package com.studio.googleplay.fragment;

import android.view.View;
import android.widget.ListView;

import com.studio.googleplay.adapter.MyBaseAdapter;
import com.studio.googleplay.domain.AppInfo;
import com.studio.googleplay.holder.BaseHolder;
import com.studio.googleplay.holder.HomeHolder;
import com.studio.googleplay.http.protocol.BaseProtocol;
import com.studio.googleplay.http.protocol.HomeProtocol;
import com.studio.googleplay.utils.UiUtils;
import com.studio.googleplay.view.LoadingPager;

import java.util.ArrayList;

/**
 * 首页的fragment
 */
public class HomeFragment extends BaseFragment {

    private ArrayList<AppInfo.DetailInfo> data;

    @Override
    public View onCreateSuccessView() {
        ListView lv = new ListView(UiUtils.getContext());
        lv.setAdapter(new HomeAdapter(data));
        return lv;
    }

    @Override
    public LoadingPager.ResultState onLoad() {
        BaseProtocol protocol = new HomeProtocol();
        AppInfo appInfo = (AppInfo) protocol.getData(0);
        if (appInfo != null) {
            data = appInfo.list;
        }
        return check(data);
    }

    private class HomeAdapter extends MyBaseAdapter<AppInfo.DetailInfo> {

        public HomeAdapter(ArrayList<AppInfo.DetailInfo> dataList) {
            super(dataList);
        }

        @Override
        public BaseHolder<AppInfo.DetailInfo> getHolder() {
            return new HomeHolder();
        }

        @Override
        public ArrayList<AppInfo.DetailInfo> onLoadMore() {
            BaseProtocol protocol = new HomeProtocol();
            AppInfo data = (AppInfo) protocol.getData(HomeFragment.this.data.size());
            if (data != null){
                return data.list;
            }
            return null;
        }
    }
}