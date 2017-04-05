package com.studio.googleplay.fragment;

import android.view.View;

import com.studio.googleplay.adapter.MyBaseAdapter;
import com.studio.googleplay.domain.SubjectInfo;
import com.studio.googleplay.holder.BaseHolder;
import com.studio.googleplay.holder.SubjectHolder;
import com.studio.googleplay.http.protocol.SubjectProtocol;
import com.studio.googleplay.utils.UiUtils;
import com.studio.googleplay.view.LoadingPager;
import com.studio.googleplay.view.MyListView;

import java.util.ArrayList;

/**
 * 主题的fragment
 */
public class SubjectFragment extends BaseFragment {
    private ArrayList<SubjectInfo> data;

    @Override
    public View onCreateSuccessView() {
        MyListView lv = new MyListView(UiUtils.getContext());
        lv.setAdapter(new SubjectAdapter(data));
        return lv;
    }

    @Override
    public LoadingPager.ResultState onLoad() {
        SubjectProtocol protocol = new SubjectProtocol();
        data = protocol.getData(0);
        return check(data);
    }

    class SubjectAdapter extends MyBaseAdapter<SubjectInfo> {

        public SubjectAdapter(ArrayList<SubjectInfo> dataList) {
            super(dataList);
        }

        @Override
        public BaseHolder<SubjectInfo> getHolder() {
            return new SubjectHolder();
        }

        @Override
        public ArrayList<SubjectInfo> onLoadMore() {
            SubjectProtocol protocol = new SubjectProtocol();
            ArrayList<SubjectInfo> moreData = protocol.getData(getListSize());
            return moreData;
        }
    }
}
