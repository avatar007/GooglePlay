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
 * 详情页-应用信息
 */
public class DetailAppInfoHolder extends BaseHolder<AppInfo> {

    private ImageView ivIcon;
    private TextView tvName;
    private TextView tvDownloadNum;
    private TextView tvVersion;
    private TextView tvDate;
    private TextView tvSize;
    private RatingBar rbStar;

    @Override
    public View initView() {
        View view = UiUtils.inflate(R.layout.layout_detail_appinfo);

        ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
        tvName = (TextView) view.findViewById(R.id.tv_name);
        tvDownloadNum = (TextView) view.findViewById(R.id.tv_download_num);
        tvVersion = (TextView) view.findViewById(R.id.tv_version);
        tvDate = (TextView) view.findViewById(R.id.tv_date);
        tvSize = (TextView) view.findViewById(R.id.tv_size);
        rbStar = (RatingBar) view.findViewById(R.id.rb_star);

        return view;
    }

    @Override
    public void refreshView(AppInfo data) {
        Picasso.with(UiUtils.getContext()).
                load(ConstantValue.SERVER_URL + "image?name=" + data.iconUrl).into(ivIcon);
        tvName.setText(data.name);
        tvDownloadNum.setText("下载量:" + data.downloadNum);
        tvVersion.setText("版本号:" + data.version);
        tvDate.setText(data.date);
        tvSize.setText(Formatter.formatFileSize(UiUtils.getContext(), data.size));
        rbStar.setRating(data.stars);
    }

}
