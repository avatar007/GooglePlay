package com.studio.googleplay.fragment;

import android.view.View;

import com.studio.googleplay.adapter.MyBaseAdapter;
import com.studio.googleplay.domain.AppInfo;
import com.studio.googleplay.holder.AppHolder;
import com.studio.googleplay.holder.BaseHolder;
import com.studio.googleplay.http.protocol.AppProtocol;
import com.studio.googleplay.utils.UiUtils;
import com.studio.googleplay.view.LoadingPager;
import com.studio.googleplay.view.MyListView;

import java.util.ArrayList;

/**
 * 应用的fragment
 */
public class AppFragment extends BaseFragment {
    private ArrayList<AppInfo> data;

    @Override
    public View onCreateSuccessView() {
        MyListView lv = new MyListView(UiUtils.getContext());
        lv.setAdapter(new AppAdapter(data));
        return lv;
    }

    @Override
    public LoadingPager.ResultState onLoad() {
        AppProtocol protocol = new AppProtocol();
        data = protocol.getData(0);
        return check(data);
    }

    class AppAdapter extends MyBaseAdapter<AppInfo> {

        public AppAdapter(ArrayList dataList) {
            super(dataList);
        }

        @Override
        public BaseHolder getHolder() {
            return new AppHolder();
        }

        @Override
        public ArrayList onLoadMore() {
            AppProtocol protocol = new AppProtocol();
            ArrayList<AppInfo> moreData = protocol.getData(getListSize());
            return moreData;
        }
    }
}
