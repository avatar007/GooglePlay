package com.studio.googleplay.fragment;

import android.graphics.Color;
import android.graphics.drawable.StateListDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.studio.googleplay.http.protocol.HotProtocol;
import com.studio.googleplay.utils.DrawableUtils;
import com.studio.googleplay.utils.UiUtils;
import com.studio.googleplay.view.FlowLayout;
import com.studio.googleplay.view.LoadingPager;

import java.util.ArrayList;
import java.util.Random;

/**
 * 排行
 */
public class HotFragment extends BaseFragment {

    private ArrayList<String> data;

    @Override
    public View onCreateSuccessView() {
        // 支持上下滑动
        ScrollView scrollView = new ScrollView(UiUtils.getContext());
        FlowLayout flow = new FlowLayout(UiUtils.getContext());

        int padding = UiUtils.dip2px(10);
        flow.setPadding(padding, padding, padding, padding);// 设置内边距

        flow.setHorizontalSpacing(UiUtils.dip2px(6));// 水平间距
        flow.setVerticalSpacing(UiUtils.dip2px(8));// 竖直间距

        for (int i = 0; i < data.size(); i++) {
            final String keyword = data.get(i);
            TextView view = new TextView(UiUtils.getContext());
            view.setText(keyword);

            view.setTextColor(Color.WHITE);
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);// 18sp
            view.setPadding(padding, padding, padding, padding);
            view.setGravity(Gravity.CENTER);

            // 生成随机颜色
            Random random = new Random();
            int r = 30 + random.nextInt(200);
            int g = 30 + random.nextInt(200);
            int b = 30 + random.nextInt(200);

            int color = 0xffcecece;// 按下后偏白的背景色

            // GradientDrawable bgNormal = DrawableUtils.getGradientDrawable(
            // Color.rgb(r, g, b), UIUtils.dip2px(6));
            // GradientDrawable bgPress = DrawableUtils.getGradientDrawable(
            // color, UIUtils.dip2px(6));
            // StateListDrawable selector = DrawableUtils.getSelector(bgNormal,
            // bgPress);

            StateListDrawable selector = DrawableUtils.getSelector(Color.rgb(r, g, b), color, UiUtils.dip2px(6));
            view.setBackground(selector);

            flow.addView(view);

            // 只有设置点击事件, 状态选择器才起作用
            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(UiUtils.getContext(), keyword,
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

        scrollView.addView(flow);
        return scrollView;
    }

    @Override
    public LoadingPager.ResultState onLoad() {
        HotProtocol protocol = new HotProtocol();
        data = protocol.getData(0);
        return check(data);
    }

}
