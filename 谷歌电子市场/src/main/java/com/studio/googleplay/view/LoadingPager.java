package com.studio.googleplay.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.studio.googleplay.R;
import com.studio.googleplay.utils.UiUtils;

/**
 * 根据当前状态来显示不同页面的自定义控件
 * - 未加载 - 加载中 - 加载失败 - 数据为空 - 加载成功
 */
public abstract class LoadingPager extends FrameLayout {
    private static final int STATE_LOAD_UNDO = 1;// 未加载
    private static final int STATE_LOAD_LOADING = 2;// 正在加载
    private static final int STATE_LOAD_ERROR = 3;// 加载失败
    private static final int STATE_LOAD_EMPTY = 4;// 数据为空
    private static final int STATE_LOAD_SUCCESS = 5;// 加载成功

    private int mCurrentState = STATE_LOAD_UNDO;// 当前状态
    private View mLoadingPage, mErrorPage, mEmptyPage, mSuccessPage;

    public LoadingPager(Context context) {
        this(context, null);
    }

    public LoadingPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    //初始化几种布局状态
    private void initView() {
        if (mLoadingPage == null) {
            mLoadingPage = UiUtils.inflate(R.layout.page_loading);
            addView(mLoadingPage);
        }
        if (mErrorPage == null) {
            mErrorPage = UiUtils.inflate(R.layout.page_error);
            Button bt_error = (Button) mErrorPage.findViewById(R.id.bt_error);
            bt_error.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    initData();
                }
            });
            addView(mErrorPage);
        }
        if (mEmptyPage == null) {
            mEmptyPage = UiUtils.inflate(R.layout.page_empty);
            addView(mEmptyPage);
        }

        showRightPager();
    }

    //根据状态显示正确的页面
    private void showRightPager() {
        mLoadingPage.setVisibility((mCurrentState == STATE_LOAD_UNDO ||
                mCurrentState == STATE_LOAD_LOADING) ? View.VISIBLE : View.GONE);
        mErrorPage.setVisibility(mCurrentState == STATE_LOAD_ERROR ? View.VISIBLE : View.GONE);
        mEmptyPage.setVisibility(mCurrentState == STATE_LOAD_EMPTY ? View.VISIBLE : View.GONE);

        //当加载成功的布局不为空,并且当前状态为正在加载的状态时候才添加成功的布局
        //这里初始化成功布局是因为成功布局是在网络加载结束后才有的布局,网络加载结束后调用showRightPager方法
        if (mSuccessPage == null && mCurrentState == STATE_LOAD_SUCCESS) {
            mSuccessPage = onCreateSuccessView();
            if (mSuccessPage != null) {
                addView(mSuccessPage);
            }
        }
        //根据状态显示成功的布局
        if (mSuccessPage != null) {
            mSuccessPage.setVisibility(mCurrentState == STATE_LOAD_SUCCESS ? View.VISIBLE : View.GONE);
        }
    }

    public void initData() {
        new Thread() {
            @Override
            public void run() {
                final ResultState resultState = onLoad();
                UiUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        if (resultState != null) {
                            mCurrentState = resultState.getState();
                            showRightPager();
                        }
                    }
                });
            }
        }.start();
    }

    //创建成功布局由调用者去完成
    public abstract View onCreateSuccessView();

    public abstract ResultState onLoad();

    public enum ResultState {
        STATE_SUCCESS(STATE_LOAD_SUCCESS),
        STATE_ERROR(STATE_LOAD_ERROR),
        STATE_EMPTY(STATE_LOAD_EMPTY);
        private int state;

        private ResultState(int state) {
            this.state = state;
        }

        private int getState() {
            return state;
        }
    }
}
