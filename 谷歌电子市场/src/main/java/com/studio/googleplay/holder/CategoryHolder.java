package com.studio.googleplay.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.studio.googleplay.R;
import com.studio.googleplay.domain.CategoryInfo;
import com.studio.googleplay.utils.ConstantValue;
import com.studio.googleplay.utils.UiUtils;

/**
 * Created by Administrator on 2017/4/5.
 */
public class CategoryHolder extends BaseHolder<CategoryInfo> implements View.OnClickListener {
    private TextView tv_name1, tv_name2, tv_name3;
    private ImageView iv_icon1, iv_icon2, iv_icon3;
    private LinearLayout ll_grid1, ll_grid2, ll_grid3;

    @Override
    public View initView() {
        View view = UiUtils.inflate(R.layout.list_item_category);

        tv_name1 = (TextView) view.findViewById(R.id.tv_name1);
        tv_name2 = (TextView) view.findViewById(R.id.tv_name2);
        tv_name3 = (TextView) view.findViewById(R.id.tv_name3);

        iv_icon1 = (ImageView) view.findViewById(R.id.iv_icon1);
        iv_icon2 = (ImageView) view.findViewById(R.id.iv_icon2);
        iv_icon3 = (ImageView) view.findViewById(R.id.iv_icon3);

        ll_grid1 = (LinearLayout) view.findViewById(R.id.ll_grid1);
        ll_grid2 = (LinearLayout) view.findViewById(R.id.ll_grid2);
        ll_grid3 = (LinearLayout) view.findViewById(R.id.ll_grid3);

        ll_grid1.setOnClickListener(this);
        ll_grid2.setOnClickListener(this);
        ll_grid3.setOnClickListener(this);
        return view;
    }

    @Override
    public void refreshView(CategoryInfo data) {
        tv_name1.setText(data.name1);
        tv_name2.setText(data.name2);
        tv_name3.setText(data.name3);
        Picasso.with(UiUtils.getContext()).
                load(ConstantValue.SERVER_URL + "image?name=" + data.url1).into(iv_icon1);
        Picasso.with(UiUtils.getContext()).
                load(ConstantValue.SERVER_URL + "image?name=" + data.url2).into(iv_icon2);
        Picasso.with(UiUtils.getContext()).
                load(ConstantValue.SERVER_URL + "image?name=" + data.url3).into(iv_icon3);
    }

    @Override
    public void onClick(View v) {
        CategoryInfo data = getData();
        switch (v.getId()) {
            case R.id.ll_grid1:
                Toast.makeText(UiUtils.getContext(), data.name1, Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_grid2:
                Toast.makeText(UiUtils.getContext(), data.name2, Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_grid3:
                Toast.makeText(UiUtils.getContext(), data.name3, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
