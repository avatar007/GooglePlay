package com.studio.googleplay.activity;

import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.studio.googleplay.R;
import com.studio.googleplay.fragment.BaseFragment;
import com.studio.googleplay.fragment.FragmentFactory;
import com.studio.googleplay.utils.UiUtils;
import com.studio.googleplay.view.PagerTab;

public class MainActivity extends BaseActivity {

    private PagerTab mPager_tap;
    private ViewPager vp_pager;
    private MyAdapter mAdapter;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPager_tap = (PagerTab) findViewById(R.id.pager_tap);
        vp_pager = (ViewPager) findViewById(R.id.vp_pager);

        mAdapter = new MyAdapter(getSupportFragmentManager());
        vp_pager.setAdapter(mAdapter);
        // viewPager必须在设置adapter后再添加给pager_tab
        mPager_tap.setViewPager(vp_pager);

        mPager_tap
                .setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position,
                                               float positionOffset, int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageSelected(int position) {
                        BaseFragment fragment = FragmentFactory
                                .createFragment(position);
                        fragment.initData();
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                    }
                });
        initActionBar();
    }

    private void initActionBar() {
        // studio中去掉了logo的显示,继承appcompatActivity要加这段代码
        // 若继承的是activity直接在application节点中插入logo属性
        ActionBar actionBar = getSupportActionBar();
        //显示Logo,默认隐藏的,即使设置了logo也要激活显示
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.mipmap.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);

        // 显示左上角返回键,当和侧边栏结合时展示三个杠图片
        actionBar.setDisplayHomeAsUpEnabled(true);
        // home处可以点击
        actionBar.setHomeButtonEnabled(true);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);

        toggle = new ActionBarDrawerToggle(this, drawer, R.drawable.ic_drawer_am,
                R.string.drawer_open, R.string.drawer_close);
        toggle.syncState();// 同步状态, 将DrawerLayout和开关关联在一起
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // 切换抽屉
                toggle.onOptionsItemSelected(item);
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private class MyAdapter extends FragmentPagerAdapter {

        private final String[] mTabName;

        public MyAdapter(FragmentManager fm) {
            super(fm);
            mTabName = UiUtils.getResStringArray(R.array.tab_names);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabName[position];
        }

        @Override
        public Fragment getItem(int position) {
            BaseFragment fragment = FragmentFactory.createFragment(position);
            return fragment;
        }

        @Override
        public int getCount() {
            return mTabName.length;
        }
    }
}
