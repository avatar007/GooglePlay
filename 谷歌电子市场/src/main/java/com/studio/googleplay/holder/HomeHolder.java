package com.studio.googleplay.holder;

import android.view.View;
import android.widget.TextView;

import com.studio.googleplay.R;
import com.studio.googleplay.domain.AppInfo;
import com.studio.googleplay.utils.UiUtils;

/**
 * Created by Administrator on 2017/3/31.
 */
public class HomeHolder extends BaseHolder<AppInfo.DetailInfo> {

    private TextView mTv_content;

    @Override
    public View initView() {
        View view = UiUtils.inflate(R.layout.home_list_item);
        mTv_content = (TextView) view.findViewById(R.id.tv_content);
        return view;
    }

    @Override
    public void refreshView(AppInfo.DetailInfo data) {
        mTv_content.setText(data.name);
    }
}
