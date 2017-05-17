package com.studio.googleplay.holder;

import android.view.View;
import android.widget.TextView;

import com.studio.googleplay.R;
import com.studio.googleplay.domain.CategoryInfo;
import com.studio.googleplay.utils.UiUtils;

/**
 * 分类模块标题holder
 */
public class TitleHolder extends BaseHolder<CategoryInfo> {

    public TextView tvTitle;


    @Override
    public View initView() {
        View view = UiUtils.inflate(R.layout.list_item_title);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        return view;
    }

    @Override
    public void refreshView(CategoryInfo data) {
        tvTitle.setText(data.title);
    }

}
