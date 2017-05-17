package com.studio.googleplay.domain;

import android.os.Environment;

import com.studio.googleplay.manager.DownloadManager;

import java.io.File;

/**
 * 下载对象的字段封装
 */
public class DownloadInfo {
    public String id;
    public String name;
    public String downloadUrl;
    public String packageName;
    public long size;

    public long currentPos;// 当前下载位置
    public int currentState;// 当前下载状态
    public String path;//文件下载路径

    // 获取当前下载进度
    public float getCurrentProgress() {
        if (size == 0) {
            return 0;
        }
        float currentProgress = (currentPos / (float) size);
        return currentProgress;
    }

    // 下载需要的字段在AppInfo中,所以要从AppInfo中拷贝字段
    public static DownloadInfo copy(AppInfo appInfo) {
        DownloadInfo downloadInfo = new DownloadInfo();
        downloadInfo.id = appInfo.id;
        downloadInfo.name = appInfo.name;
        downloadInfo.downloadUrl = appInfo.downloadUrl;
        downloadInfo.packageName = appInfo.packageName;
        downloadInfo.size = appInfo.size;

        // 顺便初始化进度和状态的值,下载路径
        downloadInfo.currentPos = 0;
        downloadInfo.currentState = DownloadManager.STATE_UNDO;
        downloadInfo.path = downloadInfo.getDownloadPath();
        return downloadInfo;
    }

    // 提供获取下载路径的方法,存储到SD卡中,加权限
    public String getDownloadPath() {
        String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String dirPath = sdPath + File.separator + "GOOGLE_MARKET" + File.separator + "download";
        if (createDir(dirPath)) {
            return dirPath + File.separator + name + ".apk";
        }
        return null;
    }

    // 检查下载的文件目录是否存在
    public boolean createDir(String dir) {
        File dirFile = new File(dir);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return dirFile.mkdirs();
        }
        return true;
    }
}
