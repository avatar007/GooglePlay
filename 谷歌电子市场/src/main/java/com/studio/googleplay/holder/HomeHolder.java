package com.studio.googleplay.holder;

import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.studio.googleplay.R;
import com.studio.googleplay.domain.AppInfo;
import com.studio.googleplay.utils.ConstantValue;
import com.studio.googleplay.utils.UiUtils;

/**
 * Created by Administrator on 2017/3/31.
 */
public class HomeHolder extends BaseHolder<AppInfo> {

    private TextView tv_name, tv_size, tv_des;
    private ImageView iv_icon;
    private RatingBar rb_star;

    @Override
    public View initView() {
        View view = UiUtils.inflate(R.layout.list_item_home);
        iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_size = (TextView) view.findViewById(R.id.tv_size);
        tv_des = (TextView) view.findViewById(R.id.tv_des);
        rb_star = (RatingBar) view.findViewById(R.id.rb_star);
        return view;
    }

    @Override
    public void refreshView(AppInfo data) {
        Picasso.with(UiUtils.getContext()).load(ConstantValue.SERVER_URL + "image?name=" + data.iconUrl)
                .into(iv_icon);
        tv_name.setText(data.name);
        tv_size.setText(Formatter.formatFileSize(UiUtils.getContext(), data.size));
        tv_des.setText(data.des);
        rb_star.setRating(data.stars);
    }
}
