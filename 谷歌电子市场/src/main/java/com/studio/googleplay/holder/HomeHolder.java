package com.studio.googleplay.holder;

import android.text.format.Formatter;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.studio.googleplay.R;
import com.studio.googleplay.domain.AppInfo;
import com.studio.googleplay.domain.DownloadInfo;
import com.studio.googleplay.manager.DownloadManager;
import com.studio.googleplay.utils.ConstantValue;
import com.studio.googleplay.utils.UiUtils;
import com.studio.googleplay.view.ProgressArc;

/**
 * Created by Administrator on 2017/3/31.
 */
public class HomeHolder extends BaseHolder<AppInfo> implements DownloadManager.DownloadObserver, View.OnClickListener {

    private TextView tv_name, tv_size, tv_des;
    private ImageView iv_icon;
    private RatingBar rb_star;
    private ProgressArc pbProgress;
    private DownloadManager mDM;

    private int mCurrentState;
    private float mProgress;
    private TextView tvDownload;

    @Override
    public View initView() {
        View view = UiUtils.inflate(R.layout.list_item_home);
        iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_size = (TextView) view.findViewById(R.id.tv_size);
        tv_des = (TextView) view.findViewById(R.id.tv_des);
        rb_star = (RatingBar) view.findViewById(R.id.rb_star);

        //初始化圆形进度条布局
        FrameLayout flProgress = (FrameLayout) view.findViewById(R.id.fl_download);
        tvDownload = (TextView) view.findViewById(R.id.tv_download);
        flProgress.setOnClickListener(this);
        pbProgress = new ProgressArc(UiUtils.getContext());
        // 设置圆形进度条直径
        pbProgress.setArcDiameter(UiUtils.dip2px(26));
        // 设置进度条颜色
        pbProgress.setProgressColor(UiUtils.getResColor(R.color.progress));
        // 设置进度条宽高布局参数
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                UiUtils.dip2px(27), UiUtils.dip2px(27));
        flProgress.addView(pbProgress, params);

        //注册观察者对象
        mDM = DownloadManager.getInstance();
        mDM.registerObserver(this);
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

        // 判断当前应用是否下载过
        DownloadInfo downloadInfo = mDM.getDownloadInfo(data);
        if (downloadInfo != null) {
            // 之前下载过
            mCurrentState = downloadInfo.currentState;
            mProgress = downloadInfo.getCurrentProgress();
        } else {
            // 没有下载过
            mCurrentState = DownloadManager.STATE_UNDO;
            mProgress = 0;
        }

        refreshUI(mCurrentState, mProgress, data.id);
    }

    /**
     * 刷新界面
     *
     * @param progress
     * @param state
     */
    private void refreshUI(int state, float progress, String id) {
        // 由于listView重用机制, 要确保刷新之前, 确实是同一个应用
        if (!getData().id.equals(id)) {
            return;
        }
        mCurrentState = state;
        mProgress = progress;
        switch (state) {
            case DownloadManager.STATE_UNDO:
                // 自定义进度条背景
                pbProgress.setBackgroundResource(R.drawable.ic_download);
                // 没有进度
                pbProgress.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
                tvDownload.setText("下载");
                break;
            case DownloadManager.STATE_WAITING:
                pbProgress.setBackgroundResource(R.drawable.ic_download);
                // 等待模式
                pbProgress.setStyle(ProgressArc.PROGRESS_STYLE_WAITING);
                tvDownload.setText("等待");
                break;
            case DownloadManager.STATE_DOWNLOADING:
                pbProgress.setBackgroundResource(R.drawable.ic_pause);
                // 下载中模式
                pbProgress.setStyle(ProgressArc.PROGRESS_STYLE_DOWNLOADING);
                pbProgress.setProgress(progress, true);
                tvDownload.setText((int) (progress * 100) + "%");
                break;
            case DownloadManager.STATE_PAUSE:
                pbProgress.setBackgroundResource(R.drawable.ic_resume);
                pbProgress.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
                break;
            case DownloadManager.STATE_ERROR:
                pbProgress.setBackgroundResource(R.drawable.ic_redownload);
                pbProgress.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
                tvDownload.setText("下载失败");
                break;
            case DownloadManager.STATE_SUCCESS:
                pbProgress.setBackgroundResource(R.drawable.ic_install);
                pbProgress.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
                tvDownload.setText("安装");
                break;
        }
    }

    // 主线程更新ui 3-4
    private void refreshUIOnMainThread(final DownloadInfo info) {
        // 判断下载对象是否是当前应用
        AppInfo appInfo = getData();
        if (appInfo.id.equals(info.id)) {
            UiUtils.runOnMainThread(new Runnable() {

                @Override
                public void run() {
                    refreshUI(info.currentState, info.getCurrentProgress(), info.id);
                }
            });
        }
    }

    @Override
    public void onDownloadStateChanged(DownloadInfo downloadInfo) {
        refreshUIOnMainThread(downloadInfo);
    }

    @Override
    public void onDownloadProgressChanged(DownloadInfo downloadInfo) {
        refreshUIOnMainThread(downloadInfo);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_download:
                // 根据当前状态来决定下一步操作
                if (mCurrentState == DownloadManager.STATE_UNDO
                        || mCurrentState == DownloadManager.STATE_ERROR
                        || mCurrentState == DownloadManager.STATE_PAUSE) {
                    mDM.download(getData());// 开始下载
                } else if (mCurrentState == DownloadManager.STATE_DOWNLOADING
                        || mCurrentState == DownloadManager.STATE_WAITING) {
                    mDM.pause(getData());// 暂停下载
                } else if (mCurrentState == DownloadManager.STATE_SUCCESS) {
                    mDM.install(getData());// 开始安装
                }
                break;
        }
    }
}
