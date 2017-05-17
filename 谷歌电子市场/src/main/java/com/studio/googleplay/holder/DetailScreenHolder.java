package com.studio.googleplay.holder;

import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.studio.googleplay.R;
import com.studio.googleplay.domain.AppInfo;
import com.studio.googleplay.utils.ConstantValue;
import com.studio.googleplay.utils.UiUtils;

/**
 * Created by Administrator on 2017/4/6.
 */
public class DetailScreenHolder extends BaseHolder<AppInfo> {
    private ImageView[] mImageViews;

    @Override
    public View initView() {
        View view = UiUtils.inflate(R.layout.layout_detail_screeninfo);
        mImageViews = new ImageView[5];
        mImageViews[0] = (ImageView) view.findViewById(R.id.iv_pic1);
        mImageViews[1] = (ImageView) view.findViewById(R.id.iv_pic2);
        mImageViews[2] = (ImageView) view.findViewById(R.id.iv_pic3);
        mImageViews[3] = (ImageView) view.findViewById(R.id.iv_pic4);
        mImageViews[4] = (ImageView) view.findViewById(R.id.iv_pic5);

        return view;
    }

    @Override
    public void refreshView(AppInfo data) {
        for (int i = 0; i < 5; i++) {
            if (i < data.screen.size()) {
                String url = data.screen.get(i);
                Picasso.with(UiUtils.getContext()).
                        load(ConstantValue.SERVER_URL + "image?name=" + url).into(mImageViews[i]);
            }else {
                mImageViews[i].setVisibility(View.GONE);
            }

        }
    }
}
