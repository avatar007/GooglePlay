package com.studio.googleplay.fragment;

import android.view.View;

import com.studio.googleplay.adapter.MyBaseAdapter;
import com.studio.googleplay.domain.CategoryInfo;
import com.studio.googleplay.holder.BaseHolder;
import com.studio.googleplay.holder.CategoryHolder;
import com.studio.googleplay.holder.TitleHolder;
import com.studio.googleplay.http.protocol.CategoryProtocol;
import com.studio.googleplay.utils.UiUtils;
import com.studio.googleplay.view.LoadingPager;
import com.studio.googleplay.view.MyListView;

import java.util.ArrayList;

/**
 * 分类的fragment
 */
public class CategoryFragment extends BaseFragment {

    private ArrayList<CategoryInfo> data;

    @Override
    public View onCreateSuccessView() {
        MyListView view = new MyListView(UiUtils.getContext());
        view.setAdapter(new CategoryAdapter(data));
        return view;
    }

    @Override
    public LoadingPager.ResultState onLoad() {
        CategoryProtocol protocol = new CategoryProtocol();
        data = protocol.getData(0);
        return check(data);
    }

    private class CategoryAdapter extends MyBaseAdapter<CategoryInfo> {

        public CategoryAdapter(ArrayList<CategoryInfo> dataList) {
            super(dataList);
        }

        @Override
        //父类默认2个类型的item 加1即可
        public int getViewTypeCount() {
            return super.getViewTypeCount() + 1;
        }

        @Override
        //重写父类的方法,返回3中类型的item
        public int InnerType(int position) {
            CategoryInfo info = data.get(position);
            if (info.isTitle) {//是标题类型的item(底层从0开始,父类返回的默认类型是常量是1,这里加1)
                return super.InnerType(position) + 1;
            } else { //普通类型item
                return super.InnerType(position);
            }
        }

        @Override
        public BaseHolder<CategoryInfo> getHolder(int position) {
            CategoryInfo info = data.get(position);
            if (info.isTitle) {//返回标题类型的Holder对象
                return new TitleHolder();
            } else {//返回普通类型的Holder对象
                return new CategoryHolder();
            }
        }

        @Override
        public boolean hasMore() {
            return false;
        }

        @Override
        public ArrayList<CategoryInfo> onLoadMore() {
            return null;
        }
    }
}
