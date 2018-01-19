package com.cn.test;

import com.cn.common.service.XClarityService;
import com.xiaoleilu.hutool.http.HttpUtil;
import com.xiaoleilu.hutool.json.JSONArray;
import com.xiaoleilu.hutool.json.JSONObject;
import com.xiaoleilu.hutool.json.JSONUtil;
import com.xiaoleilu.hutool.util.NumberUtil;

import java.math.RoundingMode;

/**
 * Created by bozhou on 2018/1/18.
 */
public class Test {
    public static void main(String[] args) {
        String s = "12.129";
        Integer kbToGb = 1024 * 1024 * 1024;
        System.out.println(kbToGb.toString());
    }
}
