package com.studio.googleplay.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.studio.googleplay.holder.BaseHolder;
import com.studio.googleplay.holder.MoreHolder;
import com.studio.googleplay.manager.ThreadManager;
import com.studio.googleplay.utils.UiUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/31.
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter {
    private ArrayList<T> mDataList;
    // 注意:::这里的int值必须从0开始递增,底层是从索引为0开始计数
    private static final int TYPE_MORE = 0;// 加载更多的类型
    private static final int TYPE_NORMAL = 1;// 普通item的类型
    private boolean isLoadMore = false;// 标记当前是否正在加载更多

    // 需要的数据通过构造方法传递过来,泛型用T代替
    public MyBaseAdapter(ArrayList<T> dataList) {
        mDataList = dataList;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getCount() - 1) {// 最后一个条目展示加载更多
            return TYPE_MORE;
        } else {
            return InnerType(position); // 其他情况展示普通类型
        }
    }

    // 子类可能会有三种item的布局类型,所以预留一个方法给子类重写
    public int InnerType(int position) {
        return TYPE_NORMAL;
    }

    @Override
    // item设置2中类型,加载更多的类型
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mDataList.size() + 1;
    }

    @Override
    public T getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseHolder holder = null;
        if (convertView == null) {
            // 初始化了布局,初始化了控件,打了标记
            if (getItemViewType(position) == TYPE_MORE) {
                // 加载加载更多的布局(添加一个参数,是否加载更多需求)
                holder = new MoreHolder(hasMore());
            } else {
                holder = getHolder(position);
            }
        } else {
            holder = (BaseHolder) convertView.getTag();
        }
        // 根据数据刷新界面
        if (getItemViewType(position) != TYPE_MORE) {// 加载普通的item数据
            holder.setData(getItem(position));
        } else {// 加载更多的item数据
            // 当加载更多的布局展示出来就加载更多数据,调用loadMore方法
            // 只有有更多数据时候才调用loadMore方法
            MoreHolder moreHolder = (MoreHolder) holder;
            if (moreHolder.getData() == MoreHolder.LOAD_MORE_TRUE) {
                loadMore(moreHolder);
            }
        }
        return holder.getRootView();// 这里不能返回convertView,为空
    }

    // 有些页面中没有加载更多的需求,所以预留方法给子类重写,默认有加载更多
    public boolean hasMore() {
        return true;
    }

    // 具体的holder由子类去实现,因为每个子类的holder都不一样
    public abstract BaseHolder<T> getHolder(int position);

    // 加载更多的数据
    private void loadMore(final MoreHolder holder) {
        if (!isLoadMore) {// 正在加载更多
            isLoadMore = true;
            /*new Thread() {
                @Override
                public void run() {

                }
            }.start();*/

            ThreadManager.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    final ArrayList<T> moreData = onLoadMore();
                    UiUtils.runOnMainThread(new Runnable() {// 主线程更新UI
                        @Override
                        public void run() {
                            if (moreData != null) {// 加载到了更多
                                if (moreData.size() < 20) {// 没有20条数据,说明没有更多数据了
                                    holder.setData(MoreHolder.LOAD_MORE_NONE);
                                    Toast.makeText(UiUtils.getContext(),
                                            "没有更多数据了", Toast.LENGTH_SHORT)
                                            .show();
                                } else {// 这里设置每页加载20条数据,有20条就加载
                                    holder.setData(MoreHolder.LOAD_MORE_TRUE);
                                }
                                mDataList.addAll(moreData);// 添加到原来的集合中
                                MyBaseAdapter.this.notifyDataSetChanged();// 通知适配器更新

                            } else {// 没有加载到更多,显示加载更多失败
                                holder.setData(MoreHolder.LOAD_MORE_ERROR);
                            }
                            isLoadMore = false;
                        }
                    });
                }
            });
        }
    }

    // 具体加载更多的数据由子类去完成,加载更多要返回集合数据
    public abstract ArrayList<T> onLoadMore();

    // 获取当前集合大小,分页加载用
    public int getListSize() {
        return mDataList.size();
    }
}
