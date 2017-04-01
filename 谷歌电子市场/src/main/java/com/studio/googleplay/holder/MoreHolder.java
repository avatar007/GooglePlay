package com.studio.googleplay.holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.studio.googleplay.R;
import com.studio.googleplay.utils.UiUtils;

/**
 * Created by Administrator on 2017/3/31.
 */
public class MoreHolder extends BaseHolder<Integer> {
    public static final int LOAD_MORE_TRUE = 1;//有加载更多
    public static final int LOAD_MORE_NONE = 2;//没有加载更多
    public static final int LOAD_MORE_ERROR = 3;//加载更多错误
    private LinearLayout mLl_load_more;
    private TextView mTv_load_error;

    public MoreHolder(boolean hasMore) {
        //这里直接使用了父类的中setData方法,此方法中调用了refreshView方法,
        // 传递一个状态,然后根据状态调用refreshView方法,处理界面效果
        if (hasMore) {//有加载更多
            setData(LOAD_MORE_TRUE);
        } else {//没有加载更多
            setData(LOAD_MORE_NONE);
        }
    }

    @Override
    public View initView() {
        View view = UiUtils.inflate(R.layout.list_item_more);
        mLl_load_more = (LinearLayout) view.findViewById(R.id.ll_load_more);
        mTv_load_error = (TextView) view.findViewById(R.id.tv_load_error);
        return view;
    }

    @Override
    public void refreshView(Integer data) {
        switch (data) {
            case LOAD_MORE_TRUE://有加载更多,显示加载更多,隐藏加载更多失败
                mLl_load_more.setVisibility(View.VISIBLE);
                mTv_load_error.setVisibility(View.GONE);
                break;
            case LOAD_MORE_NONE://没有加载更多,都隐藏
                mLl_load_more.setVisibility(View.GONE);
                mTv_load_error.setVisibility(View.GONE);
                break;
            case LOAD_MORE_ERROR://有加载更多,加载失败
                mLl_load_more.setVisibility(View.GONE);
                mTv_load_error.setVisibility(View.VISIBLE);
                break;
        }
    }
}
