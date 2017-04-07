package com.studio.googleplay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;

import com.studio.googleplay.R;
import com.studio.googleplay.domain.AppInfo;
import com.studio.googleplay.holder.DetailAppInfoHolder;
import com.studio.googleplay.holder.DetailDesHolder;
import com.studio.googleplay.holder.DetailSafeHolder;
import com.studio.googleplay.holder.DetailScreenHolder;
import com.studio.googleplay.http.protocol.HomeDetailProtocol;
import com.studio.googleplay.utils.UiUtils;
import com.studio.googleplay.view.LoadingPager;

/**
 * Created by Administrator on 2017/4/6.
 */
public class HomeDetailActivity extends BaseActivity {

	private String mPackageName;
	private AppInfo data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LoadingPager loadingPager = new LoadingPager(this) {
			@Override
			public View onCreateSuccessView() {
				return HomeDetailActivity.this.onCreateSuccessView();
			}

			@Override
			public ResultState onLoad() {
				return HomeDetailActivity.this.onLoad();
			}
		};
		setContentView(loadingPager);
		// 获取传递过来的包名
		Intent intent = getIntent();
		mPackageName = intent.getStringExtra("packageName");

		loadingPager.initData();// 初始化数据会走onLoad方法
		initActionBar();
	}

	private void initActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private View onCreateSuccessView() {
		View view = UiUtils.inflate(R.layout.page_home_detail);

		// 添加应用详情页的布局
		FrameLayout flDetailInfo = (FrameLayout) view
				.findViewById(R.id.fl_detail_appinfo);
		DetailAppInfoHolder appInfoHolder = new DetailAppInfoHolder();
		appInfoHolder.setData(data);
		flDetailInfo.addView(appInfoHolder.getRootView());

		// 添加安全信息页面的布局
		FrameLayout flSafeInfo = (FrameLayout) view
				.findViewById(R.id.fl_detail_safe);
		DetailSafeHolder safeHolder = new DetailSafeHolder();
		safeHolder.setData(data);
		flSafeInfo.addView(safeHolder.getRootView());

		// 添加截图模块页面布局
		HorizontalScrollView hsvScreenInfo = (HorizontalScrollView) view
				.findViewById(R.id.hsv_detail_screen);
		DetailScreenHolder screenHolder = new DetailScreenHolder();
		screenHolder.setData(data);
		hsvScreenInfo.addView(screenHolder.getRootView());

		// 添加描述模块页面布局
		FrameLayout flDesInfo = (FrameLayout) view
				.findViewById(R.id.fl_detail_des);
		DetailDesHolder desHolder = new DetailDesHolder();
		desHolder.setData(data);
		flDesInfo.addView(desHolder.getRootView());
		return view;
	}

	private LoadingPager.ResultState onLoad() {
		HomeDetailProtocol protocol = new HomeDetailProtocol(mPackageName);
		data = protocol.getData(0);
		if (data != null) {
			return LoadingPager.ResultState.STATE_SUCCESS;
		} else {
			return LoadingPager.ResultState.STATE_ERROR;
		}
	}

}
