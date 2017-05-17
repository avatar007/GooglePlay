package com.studio.googleplay.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Process;
import android.view.View;

import com.studio.googleplay.application.MyApplication;

/**
 * Created by Administrator on 2017/3/29.
 */
public class UiUtils {
    //获取上下文对象
    public static Context getContext() {
        return MyApplication.getContext();
    }

    //获取Handler对象
    public static Handler getHandler() {
        return MyApplication.getHandler();
    }

    //获取主线程id
    public static int getMainThreadId() {
        return MyApplication.getMainThreadId();
    }

    ///////////////////获取系统的资源文件/////////////////
    //根据id获取字符串
    public static String getResString(int id) {
        return getContext().getResources().getString(id);
    }

    //根据id获取字符串数组
    public static String[] getResStringArray(int id) {
        return getContext().getResources().getStringArray(id);
    }

    //根据id获取颜色的状态选择器
    public static ColorStateList getResColorStateList(int id) {
        return getContext().getResources().getColorStateList(id);
    }

    //根据id获取图片
    public static Drawable getResDrawable(int id) {
        return getContext().getResources().getDrawable(id);
    }

    //根据id获取颜色
    public static int getResColor(int id) {
        return getContext().getResources().getColor(id);
    }

    //根据id获取尺寸(像素代表的尺寸)
    public static int getResDimen(int id) {
        return getContext().getResources().getDimensionPixelSize(id);
    }

    ////////////////dip和pix之间转换//////////////////////
    public static int dip2px(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f);
    }

    public static float px2dip(int px) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return px / density;
    }

    ///////////////////加载布局文件//////////////////////
    public static View inflate(int id) {
        return View.inflate(getContext(), id, null);
    }

    /////////////////判断是否运行在子线程////////////////
    public static boolean isRunOnMainThread() {
        int tid = Process.myTid();
        if (tid == getMainThreadId()) {
            return true;
        }
        return false;
    }

    /////////////////让子线程的更新UI运行在主线程//////////////
    public static void runOnMainThread(Runnable runnable) {
        if (isRunOnMainThread()) {
            runnable.run();
        }
        getHandler().post(runnable);
    }
}
