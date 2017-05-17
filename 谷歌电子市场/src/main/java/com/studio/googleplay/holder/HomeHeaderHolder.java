package com.studio.googleplay.holder;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;
import com.studio.googleplay.R;
import com.studio.googleplay.utils.ConstantValue;
import com.studio.googleplay.utils.UiUtils;

import java.util.ArrayList;

/**
 * listView自动轮播条的头布局
 */
public class HomeHeaderHolder extends BaseHolder<ArrayList<String>> {

    private ViewPager mVpView;
    private LinearLayout mLlRoot;
    private ArrayList<String> data;
    private int previousPos;

    @Override
    public View initView() {
        //外层的相对布局
        RelativeLayout rlRoot = new RelativeLayout(UiUtils.getContext());
        AbsListView.LayoutParams rlParams = new AbsListView.LayoutParams
                (AbsListView.LayoutParams.MATCH_PARENT, UiUtils.dip2px(180));
        rlRoot.setLayoutParams(rlParams);

        //viewPager的布局
        mVpView = new ViewPager(UiUtils.getContext());
        RelativeLayout.LayoutParams vpParams = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        rlRoot.addView(mVpView, vpParams);

        //指示器的线性布局
        mLlRoot = new LinearLayout(UiUtils.getContext());
        mLlRoot.setOrientation(LinearLayout.HORIZONTAL);
        RelativeLayout.LayoutParams llParams = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        int padding = UiUtils.dip2px(10);
        mLlRoot.setPadding(padding, padding, padding, padding);//设置线性布局的内边距
        //设置线性布局的相对位置
        llParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        llParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        rlRoot.addView(mLlRoot, llParams);
        return rlRoot;
    }

    @Override
    public void refreshView(final ArrayList<String> data) {
        this.data = data;
        mVpView.setAdapter(new DetailAdapter());
        mVpView.setCurrentItem(data.size() * 10000);//设置轮播条的起始位置

        //初始化指示器的点
        for (int i = 0; i < data.size(); i++) {
            ImageView point = new ImageView(UiUtils.getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i == 0) {
                point.setImageResource(R.drawable.indicator_selected);
            } else {
                params.leftMargin = UiUtils.dip2px(3);//设置每个指示器的边距
                point.setImageResource(R.drawable.indicator_normal);
            }
            mLlRoot.addView(point, params);
        }

        //添加选中后的监听(设置选中的指示器的点)
        mVpView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                position = position % data.size();
                ImageView iv = (ImageView) mLlRoot.getChildAt(position);
                iv.setImageResource(R.drawable.indicator_selected);

                ImageView preIv = (ImageView) mLlRoot.getChildAt(previousPos);
                preIv.setImageResource(R.drawable.indicator_normal);
                previousPos = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        //自动轮播,handler发延迟消息
        UiUtils.getHandler().removeCallbacksAndMessages(null);
        UiUtils.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int currentItem = mVpView.getCurrentItem();
                currentItem++;
                mVpView.setCurrentItem(currentItem);
                UiUtils.getHandler().postDelayed(this, 3000);
            }
        }, 3000);

//        HomeHeaderTask task = new HomeHeaderTask();
//        task.start();
    }

   /* private class HomeHeaderTask implements Runnable {
        public void start() {
            UiUtils.getHandler().removeCallbacksAndMessages(null);
            UiUtils.getHandler().postDelayed(this, 3000);
        }
        @Override
        public void run() {
            int currentItem = mVpView.getCurrentItem();
            currentItem++;
            mVpView.setCurrentItem(currentItem);
            UiUtils.getHandler().postDelayed(this, 3000);
        }
    }*/

    private class DetailAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position = position % data.size();
            ImageView view = new ImageView(UiUtils.getContext());
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            String url = data.get(position);
            Picasso.with(UiUtils.getContext()).
                    load(ConstantValue.SERVER_URL + "image?name=" + url).into(view);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
