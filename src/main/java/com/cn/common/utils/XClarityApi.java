package com.cn.common.utils;

import com.google.common.collect.Maps;
import com.xiaoleilu.hutool.cache.Cache;
import com.xiaoleilu.hutool.cache.CacheUtil;
import com.xiaoleilu.hutool.http.HttpUtil;
import com.xiaoleilu.hutool.json.JSONArray;
import com.xiaoleilu.hutool.json.JSONObject;
import com.xiaoleilu.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


/**
 * Created by bozhou on 2018/1/18.
 */
@Component
public class XClarityApi {
    @Value("${XClarity.userId}")
    private String userId;
    @Value("${XClarity.password}")
    private String password;
    @Value("${XClarity.apiHost}")
    private String apiHost;
    @Value("${XClarity.sessionExpiresTime}")
    private Long sessionExpiresTime;
    @Autowired
    private RestTemplate restTemplate;
    Cache<String,JSONObject> fifoCache = CacheUtil.newFIFOCache(1);

    private static final String session = "/sessions";
    private static final String cabinet = "/cabinet";
    private static final String fuelGauge = "/fuelGauge";
    private static final String task = "/tasks";
    private static final String updateMetrics="/stgupdates/inventory/updateMetrics";
    private static final String osImages="/osImages";
    private static final String hostPlatformsDeployStatus="/hostPlatforms/deploy/status";
    private static final String configDeployStatus="/config/deploy/status";
    private static final String configProfile="/config/profile";

    private HttpEntity<String> getTokenHeader(){
        JSONObject jsonObject = fifoCache.get("userToken");
        if(jsonObject==null){
            jsonObject = getToken();
        }
        String csrf = jsonObject.getJSONObject("response").getJSONObject("session").getStr("csrf");
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("csrf", csrf);
        HttpEntity<String> requestEntity = new HttpEntity<String>(null, requestHeaders);
        return requestEntity;
    }

    private JSONObject sendGet(String api){
        JSONObject jsonObject = restTemplate.exchange(api, HttpMethod.GET,getTokenHeader(),JSONObject.class, Maps.newHashMap()).getBody();
        return jsonObject;
    }

    private JSONArray sendGetArray(String api){
        JSONArray jsonArray = restTemplate.exchange(api, HttpMethod.GET,getTokenHeader(),JSONArray.class, Maps.newHashMap()).getBody();
        return jsonArray;
    }

    private JSONObject getToken() {
        JSONObject json = JSONUtil.createObj();
        json.put("UserId",userId);
        json.put("password",password);
        String apiUrl = apiHost+session;
        JSONObject jsonObject = restTemplate.postForEntity(apiUrl,json,JSONObject.class).getBody();
        fifoCache.put("userToken",jsonObject,sessionExpiresTime);
        return jsonObject;
    }

    public JSONObject cabinetApi(){
        String apiUrl = apiHost + cabinet + "?status=includeStandalone";
        JSONObject jsonObject = sendGet(apiUrl);
        return jsonObject;
    }

    public JSONObject fuelGaugeApi(){
        String apiUrl = apiHost + fuelGauge;
        JSONObject jsonObject = sendGet(apiUrl);
        return jsonObject;
    }

    public JSONObject sessionsApi(){
        String apiUrl = apiHost + session;
        JSONObject jsonObject = sendGet(apiUrl);
        return jsonObject;
    }

    public JSONArray taskApi(){
        String apiUrl = apiHost + task;
        JSONArray jsonArray = sendGetArray(apiUrl);
        return jsonArray;
    }

    public JSONObject updateMetricsApi(){
        String apiUrl = apiHost + updateMetrics;
        JSONObject jsonObject = sendGet(apiUrl);
        return jsonObject;
    }

    public JSONObject osImagesApi(){
        String apiUrl = apiHost + osImages;
        JSONObject jsonObject = sendGet(apiUrl);
        return jsonObject;
    }

    public JSONObject hostPlatformsDeployStatusApi(){
        String apiUrl = apiHost + hostPlatformsDeployStatus;
        JSONObject jsonObject = sendGet(apiUrl);
        return jsonObject;
    }

    public JSONObject configDeployStatusApi(){
        String apiUrl = apiHost + configDeployStatus;
        JSONObject jsonObject = sendGet(apiUrl);
        return jsonObject;
    }

    public JSONObject configProfileApi(){
        String apiUrl = apiHost + configProfile;
        JSONObject jsonObject = sendGet(apiUrl);
        return jsonObject;
    }
}
