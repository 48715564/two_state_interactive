package com.cn.test;

import com.xiaoleilu.hutool.cache.Cache;
import com.xiaoleilu.hutool.cache.CacheUtil;
import com.xiaoleilu.hutool.json.JSONObject;

/**
 * Created by bozhou on 2018/1/18.
 */
public class Test {
    public static void main(String[] args) {
        String s = "12.129";
        Integer kbToGb = 1024 * 1024 * 1024;
        System.out.println(kbToGb.toString());

        Cache<String,JSONObject> fifoCache = CacheUtil.newFIFOCache(1);
        fifoCache.put("a",new JSONObject(),1000);
        System.out.println(fifoCache.get("a"));
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(fifoCache.get("a")==null);
    }
}
