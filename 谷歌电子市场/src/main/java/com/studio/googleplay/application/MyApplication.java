package com.studio.googleplay.application;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

/**
 * Created by Administrator on 2017/3/29.
 */
public class MyApplication extends Application {
    private static Context sContext;
    private static Handler sHandler;
    private static int mainThreadId;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        sHandler = new Handler();
        mainThreadId = android.os.Process.myTid();
    }

    public static Context getContext() {
        return sContext;
    }

    public static Handler getHandler() {
        return sHandler;
    }

    public static int getMainThreadId() {
        return mainThreadId;
    }
}
