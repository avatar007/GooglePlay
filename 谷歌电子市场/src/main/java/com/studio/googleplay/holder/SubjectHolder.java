package com.studio.googleplay.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.studio.googleplay.R;
import com.studio.googleplay.domain.SubjectInfo;
import com.studio.googleplay.utils.ConstantValue;
import com.studio.googleplay.utils.UiUtils;

/**
 * Created by Administrator on 2017/4/4.
 */
public class SubjectHolder extends BaseHolder<SubjectInfo> {
    private ImageView iv_pic;
    private TextView tv_title;

    @Override
    public View initView() {
        View view = UiUtils.inflate(R.layout.list_item_subject);
        iv_pic = (ImageView) view.findViewById(R.id.iv_pic);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        return view;
    }

    @Override
    public void refreshView(SubjectInfo data) {
        Picasso.with(UiUtils.getContext()).
                load(ConstantValue.SERVER_URL + "image?name=" + data.url).into(iv_pic);
        tv_title.setText(data.des);
    }
}
