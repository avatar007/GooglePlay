package com.studio.googleplay.manager;

import android.content.Intent;
import android.net.Uri;

import com.squareup.okhttp.ResponseBody;
import com.studio.googleplay.domain.AppInfo;
import com.studio.googleplay.domain.DownloadInfo;
import com.studio.googleplay.http.OkHttpClientUtils;
import com.studio.googleplay.utils.ConstantValue;
import com.studio.googleplay.utils.IOUtils;
import com.studio.googleplay.utils.UiUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 下载管理者,作为被观察者的对象
 */
public class DownloadManager {

    public static final int STATE_UNDO = 1;//未下载
    public static final int STATE_WAITING = 2;//等待下载
    public static final int STATE_DOWNLOADING = 3;//下载中
    public static final int STATE_PAUSE = 4;//暂停下载
    public static final int STATE_ERROR = 5;//下载错误
    public static final int STATE_SUCCESS = 6;//下载成功

    //存储观察者对象的集合,用来注册观察者和注销观察者
    private ArrayList<DownloadObserver> mObserversList = new ArrayList<>();

    //存储所有下载对象
    private HashMap<String, DownloadInfo> mDownloadMap = new HashMap<>();
    //存储所有下载任务对象
    private HashMap<String, DownloadTask> mDownloadTaskMap = new HashMap<>();

    private static DownloadManager mDownloadManager = new DownloadManager();

    private DownloadManager() {
    }

    public static DownloadManager getInstance() {
        return mDownloadManager;
    }

    //3.提供注册观察者的方法
    public void registerObserver(DownloadObserver observer) {
        if (observer != null) {
            mObserversList.add(observer);
        }
    }

    //4.提供注销观察者的方法
    public void unRegisterObserver(DownloadObserver observer) {
        if (observer != null) {
            mObserversList.remove(observer);
        }
    }

    //5.在合适的位置调用接口中方法,通知外面里面状态的改变
    public void notifyDownloadStateChanged(DownloadInfo downloadInfo) {
        //下载状态改变的通知
        for (DownloadObserver observer : mObserversList) {
            observer.onDownloadStateChanged(downloadInfo);
        }
    }

    //5.在合适的位置调用接口中方法,通知外面里面状态的改变
    public void notifyDownloadProgressChanged(DownloadInfo downloadInfo) {
        //下载进度改变的通知
        for (DownloadObserver observer : mObserversList) {
            observer.onDownloadProgressChanged(downloadInfo);
        }
    }

    //开始下载的逻辑
    public void download(AppInfo info) {
        //下载前要判断这个对象是否下载过,所以用一个集合封装所有下载对象
        //未下载过就从头下载,下载过的就断点下载
        DownloadInfo downloadInfo = mDownloadMap.get(info.id);
        if (downloadInfo == null) {
            downloadInfo = DownloadInfo.copy(info);//生成一个新的下载对象
        }
        downloadInfo.currentState = STATE_WAITING;//更改状态为等待下载,可能在排队
        notifyDownloadStateChanged(downloadInfo);//通知状态发生改变

        mDownloadMap.put(info.id, downloadInfo);//添加到下载的集合中

        //使用线程池开始下载,因为是多线程下载,所以要把下载任务封装到集合中
        DownloadTask task = new DownloadTask(downloadInfo);
        ThreadManager.getInstance().execute(task);
        mDownloadTaskMap.put(info.id, task);
    }

    //下载任务封装
    private class DownloadTask implements Runnable {
        private DownloadInfo downloadInfo;

        public DownloadTask(DownloadInfo info) {
            this.downloadInfo = info;
        }

        @Override
        public void run() {
            //切换下载状态,并通知外界
            downloadInfo.currentState = STATE_DOWNLOADING;
            notifyDownloadStateChanged(downloadInfo);

            File file = new File(downloadInfo.path);
            ResponseBody responseBody = null;
            //文件不存在,文件长度不等当前的下载到的位置,当前下载位置为0的时候重新下载
            if (!file.exists() || file.length() != downloadInfo.currentPos
                    || downloadInfo.currentPos == 0) {
                file.delete();//删除下载不完整的文件,不存在的话删除也没事
                downloadInfo.currentPos = 0;//更改下载位置,从0开始
                try {
                    responseBody = OkHttpClientUtils.buildResponseBody(ConstantValue.SERVER_URL +
                            "download?name=" + downloadInfo.downloadUrl, null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {//暂停过的断点下载
                // range 表示请求服务器从文件的哪个位置开始返回数据
                try {
                    responseBody = OkHttpClientUtils.buildResponseBody(ConstantValue.SERVER_URL
                            + "download?name=" + downloadInfo.downloadUrl
                            + "&range=" + file.length(), null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (responseBody != null) {
                InputStream in = null;
                FileOutputStream out = null;
                try {
                    in = responseBody.byteStream();
                    if (in != null) {
                        //断点下载要追加到原有的文件上,所以file要加第二个参数true,否则会删除之前下载的文件
                        out = new FileOutputStream(file, true);
                        int len = 0;
                        byte[] buffer = new byte[1024 * 8];
                        // 只有状态是正在下载, 才继续轮询. 解决下载过程中中途暂停的问题
                        while ((len = in.read(buffer)) != -1
                                && downloadInfo.currentState == STATE_DOWNLOADING) {
                            out.write(buffer, 0, len);
                            out.flush();// 把剩余数据刷入本地

                            // 更新下载进度
                            downloadInfo.currentPos += len;
                            notifyDownloadProgressChanged(downloadInfo);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    IOUtils.close(in);
                    IOUtils.close(out);
                }
                if (file.length() == downloadInfo.size) {//文件完整下载成功
                    downloadInfo.currentState = STATE_SUCCESS;
                    notifyDownloadStateChanged(downloadInfo);
                } else if (downloadInfo.currentState == STATE_PAUSE) {//中途暂停下载
                    notifyDownloadStateChanged(downloadInfo);
                } else {//下载失败
                    file.delete();
                    downloadInfo.currentPos = 0;
                    downloadInfo.currentState = STATE_ERROR;//下载错误
                    notifyDownloadStateChanged(downloadInfo);
                }

            } else {//网络访问异常,没有获取到下载的内容
                file.delete();
                downloadInfo.currentPos = 0;
                downloadInfo.currentState = STATE_ERROR;//下载错误
                notifyDownloadStateChanged(downloadInfo);
            }

            //下载结束无论成功与否都要从下载对象和下载任务的集合中移除对象
            mDownloadTaskMap.remove(downloadInfo.id);
        }
    }

    //暂停下载的逻辑
    public void pause(AppInfo appInfo) {
        DownloadInfo downloadInfo = mDownloadMap.get(appInfo.id);
        if (downloadInfo != null) {
            //只有等待状态和正在下载的状态才能暂停
            if (downloadInfo.currentState == STATE_DOWNLOADING || downloadInfo.currentState == STATE_WAITING) {
                DownloadTask task = mDownloadTaskMap.get(appInfo.id);//获取当前下载任务
                if (task != null) {
                    //取消正在下载的任务对象(只是取消了等待下载的对象,正在下载的任务对象要从下载的逻辑中中断)
                    ThreadManager.getInstance().cancel(task);
                }
                downloadInfo.currentState = STATE_PAUSE;
                notifyDownloadStateChanged(downloadInfo);
            }
        }

    }

    //下载完成安装的逻辑
    public void install(AppInfo appInfo) {
        DownloadInfo downloadInfo = mDownloadMap.get(appInfo.id);
        if (downloadInfo != null) {
            // 跳到系统的安装页面进行安装
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + downloadInfo.path),
                    "application/vnd.android.package-archive");
            UiUtils.getContext().startActivity(intent);
        }
    }

    //1.定义观察者对象
    public interface DownloadObserver {
        //2.定义观察的内容:下载状态的改变,下载进度的改变,参数为具体哪个下对象
        public void onDownloadStateChanged(DownloadInfo downloadInfo);

        public void onDownloadProgressChanged(DownloadInfo downloadInfo);
    }

    // 根据应用信息返回下载对象
    public DownloadInfo getDownloadInfo(AppInfo info) {
        return mDownloadMap.get(info.id);
    }

}
