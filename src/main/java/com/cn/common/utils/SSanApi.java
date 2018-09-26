package com.cn.common.utils;

import com.xiaoleilu.hutool.json.JSONArray;
import com.xiaoleilu.hutool.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SSanApi {
    private static final String storage = "/api/monitors/clusters/ssan/storage/?format=json";
    private static final String storageIOPS = "/api/monitors/clusters/ssan/iops/10/i/?format=json";

    @Value("${ssan.url}")
    private String ssanUrl;
    @Autowired
    private RestTemplate restTemplate;

    private String getRequestUrl(String api, String... params) {
        String url = ssanUrl + api;
        url = String.format(url, params);
        return url;
    }

    private String getRequestUrl(String api) {
        return ssanUrl + api;
    }

    private JSONObject sendGet(String api) {
        JSONObject jsonObject = restTemplate.getForObject(api, JSONObject.class);
        return jsonObject;
    }

    private JSONArray sendGetArray(String api) {
        JSONArray jsonArray = restTemplate.getForObject(api, JSONArray.class);
        return jsonArray;
    }

    /**
     * 获取所有数据存储
     *
     * @return
     */
    public JSONObject getStorage() {
        return sendGet(getRequestUrl(storage));
    }

    /**
     * 获取10分钟以内的存储IOPS信息
     *
     * @return
     */
    public JSONObject getStorageIOPS() {
        return sendGet(getRequestUrl(storageIOPS));
    }

}
