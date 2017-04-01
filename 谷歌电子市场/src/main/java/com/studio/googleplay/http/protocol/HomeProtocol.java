package com.studio.googleplay.http.protocol;

import com.google.gson.Gson;
import com.studio.googleplay.domain.AppInfo;

/**
 * Created by Administrator on 2017/4/1.
 */
public class HomeProtocol extends BaseProtocol<AppInfo> {

    @Override
    public String getKey() {
        return "home";
    }

    @Override
    public String getParams() {
        return "";
    }

    @Override
    public AppInfo parseData(String result) {
        Gson gson = new Gson();
        AppInfo appInfo = gson.fromJson(result, AppInfo.class);
        return appInfo;
    }
}
