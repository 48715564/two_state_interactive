package com.cn.test;

import com.cn.common.service.XClarityService;
import com.xiaoleilu.hutool.http.HttpUtil;
import com.xiaoleilu.hutool.json.JSONArray;
import com.xiaoleilu.hutool.json.JSONObject;
import com.xiaoleilu.hutool.json.JSONUtil;

/**
 * Created by bozhou on 2018/1/18.
 */
public class Test {
    public static void main(String[] args) {
        String json = HttpUtil.get("http://127.0.0.1:8180/test.json");
        JSONObject jsonObject = JSONUtil.parseObj(json);
        XClarityService xClarityService = new XClarityService();
        xClarityService._onFinishInitializeInventory(jsonObject);
    }
}
