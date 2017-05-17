package com.studio.googleplay.http.protocol;

import com.studio.googleplay.http.OkHttpClientUtils;
import com.studio.googleplay.utils.ConstantValue;
import com.studio.googleplay.utils.IOUtils;
import com.studio.googleplay.utils.StringUtils;
import com.studio.googleplay.utils.UiUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2017/4/1.
 */
public abstract class BaseProtocol<T> {
    public T getData(int index) {
        String result = getCache(index);
        if (StringUtils.isEmpty(result)) {
            result = getDataFormServer(index);
        }
        if (result != null) {
            T data = parseData(result);
            return data;
        }
        return null;
    }

    private String getDataFormServer(final int index) {
        try {
            String result = OkHttpClientUtils.getStringFromUrl(UiUtils.getContext(), ConstantValue.SERVER_URL
                    + getKey() + "?index=" + index + getParams(), null);
            if (!StringUtils.isEmpty(result)) {
                setCache(index, result);
                return result;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 写缓存
    // 以url为key, 以json为value
    public void setCache(int index, String json) {
        // 以url为文件名, 以json为文件内容,保存在本地
        File cacheDir = UiUtils.getContext().getCacheDir();// 本应用的缓存文件夹
        // 生成缓存文件
        File cacheFile = new File(cacheDir, getKey() + "?index=" + index
                + getParams());

        //不使用写入的的事件当做时间戳,可根据缓存文件最后一次修改的事件对比
        /*FileWriter writer = null;
        try {
            writer = new FileWriter(cacheFile);
            // 缓存失效的截止时间
            long deadline = System.currentTimeMillis() + 30 * 60 * 1000;// 半个小时有效期
            writer.write(deadline + "\n");// 在第一行写入缓存时间, 换行
            writer.write(json);// 写入json
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(writer);
        }*/
    }

    // 读缓存
    public String getCache(int index) {
        // 以url为文件名, 以json为文件内容,保存在本地
        File cacheDir = UiUtils.getContext().getCacheDir();// 本应用的缓存文件夹
        // 生成缓存文件
        File cacheFile = new File(cacheDir, getKey() + "?index=" + index
                + getParams());

        // 判断缓存是否存在i
        if (cacheFile.exists()) {
            // 判断缓存是否有效
            BufferedReader reader = null;
            try {
                //缓存事件用当前事件减去缓存文件最后修改时间
                long cacheTime = System.currentTimeMillis() - cacheFile.lastModified();
                long deadTime = 30 * 60 * 1000;//缓存有效期30分钟
                //reader = new BufferedReader(new FileReader(cacheFile));
                //String deadline = reader.readLine();// 读取第一行的有效期
                //long deadTime = Long.parseLong(deadline);
                if (cacheTime > deadTime) {// 当前时间小于截止时间,
                    // 说明缓存有效
                    // 缓存有效
                    StringBuffer sb = new StringBuffer();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    return sb.toString();
                } else {
                    cacheFile.delete();//失效删除缓存文件
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                IOUtils.close(reader);
            }
        }
        return null;
    }

    // 解析json数据, 子类必须实现
    public abstract T parseData(String result);

    //具体标签页的地址由子类去传递
    public abstract String getKey();

    //具体网络加载的参数由子类传递
    public abstract String getParams();
}
