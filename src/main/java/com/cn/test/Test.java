package com.cn.test;

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
        JSONArray jsonArray = jsonObject.getJSONArray("cabinetList");
        JSONObject object = new JSONObject(jsonArray.get(0));
        object.put("room","啦啦");
        System.out.println(jsonObject.toString());
    }
}
