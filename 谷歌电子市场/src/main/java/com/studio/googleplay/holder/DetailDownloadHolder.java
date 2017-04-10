package com.studio.googleplay.holder;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.studio.googleplay.R;
import com.studio.googleplay.domain.AppInfo;
import com.studio.googleplay.domain.DownloadInfo;
import com.studio.googleplay.manager.DownloadManager;
import com.studio.googleplay.utils.UiUtils;
import com.studio.googleplay.view.ProgressHorizontal;

/**
 * Created by Administrator on 2017/4/9.
 */
public class DetailDownloadHolder extends BaseHolder<AppInfo> implements View.OnClickListener, DownloadManager.DownloadObserver {
    private DownloadManager mDM;

    private int mCurrentState;
    private float mProgress;

    private FrameLayout flProgress;
    private Button btnDownload;
    private ProgressHorizontal pbProgress;

    @Override
    public View initView() {
        View view = UiUtils.inflate(R.layout.layout_detail_download);

        btnDownload = (Button) view.findViewById(R.id.btn_download);
        btnDownload.setOnClickListener(this);

        // 初始化自定义进度条
        flProgress = (FrameLayout) view.findViewById(R.id.fl_progress);
        flProgress.setOnClickListener(this);

        pbProgress = new ProgressHorizontal(UiUtils.getContext());
        pbProgress.setProgressBackgroundResource(R.drawable.progress_bg);// 进度条背景图片
        pbProgress.setProgressResource(R.drawable.progress_normal);// 进度条图片
        pbProgress.setProgressTextColor(Color.WHITE);// 进度文字颜色
        pbProgress.setProgressTextSize(UiUtils.dip2px(18));// 进度文字大小

        // 宽高填充父窗体
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);

        // 给帧布局添加自定义进度条
        flProgress.addView(pbProgress, params);

        mDM = DownloadManager.getInstance();
        mDM.registerObserver(this);// 注册观察者, 监听状态和进度变化
        return view;
    }

    @Override
    public void refreshView(AppInfo data) {
        DownloadInfo downloadInfo = mDM.getDownloadInfo(data);
        if (downloadInfo != null) {//之前下载过
            mProgress = downloadInfo.currentPos;
            mCurrentState = downloadInfo.currentState;
        } else {//未下载过
            mProgress = 0;
            mCurrentState = DownloadManager.STATE_UNDO;
        }

        refreshUI(mCurrentState, mProgress);
    }

    private void refreshUI(int currentState, float progress) {
        mCurrentState = currentState;
        mProgress = progress;

        switch (currentState) {
            case DownloadManager.STATE_UNDO:// 未下载
                flProgress.setVisibility(View.GONE);
                btnDownload.setVisibility(View.VISIBLE);
                btnDownload.setText("下载");
                break;

            case DownloadManager.STATE_WAITING:// 等待下载
                flProgress.setVisibility(View.GONE);
                btnDownload.setVisibility(View.VISIBLE);
                btnDownload.setText("等待中..");
                break;

            case DownloadManager.STATE_DOWNLOADING:// 正在下载
                flProgress.setVisibility(View.VISIBLE);
                btnDownload.setVisibility(View.GONE);
                pbProgress.setCenterText("");
                pbProgress.setProgress(mProgress);// 设置下载进度
                break;

            case DownloadManager.STATE_PAUSE:// 下载暂停
                flProgress.setVisibility(View.VISIBLE);
                btnDownload.setVisibility(View.GONE);
                pbProgress.setCenterText("暂停");
                pbProgress.setProgress(mProgress);
                break;

            case DownloadManager.STATE_ERROR:// 下载失败
                flProgress.setVisibility(View.GONE);
                btnDownload.setVisibility(View.VISIBLE);
                btnDownload.setText("下载失败");
                break;

            case DownloadManager.STATE_SUCCESS:// 下载成功
                flProgress.setVisibility(View.GONE);
                btnDownload.setVisibility(View.VISIBLE);
                btnDownload.setText("安装");
                break;
        }
    }

    // 主线程更新ui
    private void refreshUIOnMainThread(final DownloadInfo info) {
        // 判断下载对象是否是当前应用
        AppInfo appInfo = getData();
        if (appInfo.id.equals(info.id)) {
            UiUtils.runOnMainThread(new Runnable() {

                @Override
                public void run() {
                    refreshUI(info.currentState, info.getCurrentProgress());
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_download:
            case R.id.fl_progress:
                // 根据当前状态来决定下一步操作
                if (mCurrentState == DownloadManager.STATE_UNDO
                        || mCurrentState == DownloadManager.STATE_ERROR
                        || mCurrentState == DownloadManager.STATE_PAUSE) {
                    mDM.download(getData());// 开始下载
                    System.out.println("点击开始下载了");
                } else if (mCurrentState == DownloadManager.STATE_DOWNLOADING
                        || mCurrentState == DownloadManager.STATE_WAITING) {
                    mDM.pause(getData());// 暂停下载
                    System.out.println("点击暂停下载了");
                } else if (mCurrentState == DownloadManager.STATE_SUCCESS) {
                    mDM.install(getData());// 开始安装
                    System.out.println("点击开始安装了");
                }
                break;
        }
    }

    @Override
    //此方法有时在主线程有时在子线程调用
    public void onDownloadStateChanged(DownloadInfo downloadInfo) {
        refreshUIOnMainThread(downloadInfo);
    }

    @Override
    //此方法是在子线程中调用的
    public void onDownloadProgressChanged(DownloadInfo downloadInfo) {
        refreshUIOnMainThread(downloadInfo);
    }
}
