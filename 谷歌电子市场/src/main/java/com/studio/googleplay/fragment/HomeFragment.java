package com.studio.googleplay.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.studio.googleplay.activity.HomeDetailActivity;
import com.studio.googleplay.adapter.MyBaseAdapter;
import com.studio.googleplay.domain.AppInfo;
import com.studio.googleplay.holder.BaseHolder;
import com.studio.googleplay.holder.HomeHeaderHolder;
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
    private ArrayList<String> mPictureList;

    @Override
    public View onCreateSuccessView() {
        MyListView lv = new MyListView(UiUtils.getContext());

        //给listView添加自动轮播条的头布局
        HomeHeaderHolder holder = new HomeHeaderHolder();
        lv.addHeaderView(holder.getRootView());
        if (mPictureList != null) {
            holder.setData(mPictureList);
        }
        lv.setAdapter(new HomeAdapter(data));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppInfo appInfo = data.get(position - 1);//要去掉头布局
                if (appInfo != null) {
                    Intent intent = new Intent(UiUtils.getContext(), HomeDetailActivity.class);
                    intent.putExtra("packageName", appInfo.packageName);
                    startActivity(intent);
                }
            }
        });
        return lv;
    }

    @Override
    public LoadingPager.ResultState onLoad() {
        HomeProtocol protocol = new HomeProtocol();
        data = protocol.getData(0);
        mPictureList = protocol.getPictureList();
        return check(data);
    }

    private class HomeAdapter extends MyBaseAdapter<AppInfo> {

        public HomeAdapter(ArrayList<AppInfo> dataList) {
            super(dataList);
        }

        @Override
        public BaseHolder<AppInfo> getHolder(int position) {
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