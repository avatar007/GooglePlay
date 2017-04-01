package com.studio.googleplay.domain;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/1.
 */
public class AppInfo {
    public ArrayList<DetailInfo> list;
    public ArrayList<String> picture;

    public class DetailInfo {
        public String des;
        public String downloadUrl;
        public String iconUrl;
        public String id;
        public String name;
        public String packageName;
        public long size;
        public float stars;
    }
}
