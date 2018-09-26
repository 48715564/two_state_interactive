package com.cn.common.utils;

import com.google.common.collect.Maps;
import com.xiaoleilu.hutool.json.JSONArray;
import com.xiaoleilu.hutool.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import sun.misc.BASE64Encoder;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class OvirtApi {
    private static final String AUTHORIZATION = "Authorization";
    private static final String ACCEPT = "Accept";
    private static final String HOSTS = "/hosts";
    private static final String NETWORKS = "/networks";
    private static final String DATACENTERS = "/datacenters";
    private static final String CLUSTERS = "/clusters";
    private static final String STORAGEDOMAINS = "/storagedomains";
    private static final String VMS = "/vms";
    private static final String EVENTS = "/events";
    private static final String ALERT_EVENTS = "/events?search=severity=alert";
    //获得主机的所有网络端口
    private static final String HOST_NICS = "/hosts/%s/nics";
    //获得指定NIC的统计信息
    private static final String HOST_NICS_STATISTICS = "/hosts/%s/nics/%s/statistics";
    //获得指定NIC的统计信息
    private static final String HOST_STATISTICS = "/hosts/%s/statistics";
    @Value("${ovirt.url}")
    private String ovirtUrl;
    @Value("${ovirt.username}")
    private String ovirtUsername;
    @Value("${ovirt.password}")
    private String ovirtPassword;
    @Autowired
    private RestTemplate restTemplate;

    private String getRequestUrl(String api,String ... params){
        String url = ovirtUrl+api;
        url = String.format(url,params);
        return url;
    }

    private String getRequestUrl(String api){
        return ovirtUrl+api;
    }

    private String getFromBASE64(String s) {
        if (s == null) {
            return null;
        }
        BASE64Encoder encoder = new BASE64Encoder();
        try {
            String b = encoder.encode(s.getBytes());
            return b;
        } catch (Exception e) {
            return null;
        }
    }

    private Map<String, String> getAuthorization() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put(AUTHORIZATION, "Basic "+getFromBASE64(ovirtUsername+":"+ovirtPassword));
        return map;
    }

    private HttpEntity<String> getTokenHeader(){
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add(AUTHORIZATION, "Basic "+getFromBASE64(ovirtUsername+":"+ovirtPassword));
        requestHeaders.add(ACCEPT, "application/json");
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

    /**
     * 获得所有主机
     * @return
     */
    public JSONObject getHosts(){
        return sendGet(getRequestUrl(HOSTS));
    }
    /**
     * 获得所有主机
     * @return
     */
    public JSONObject getDataCenters(){
        return sendGet(getRequestUrl(DATACENTERS));
    }
    /**
     * 获得所有集群
     * @return
     */
    public JSONObject getClusters(){
        return sendGet(getRequestUrl(CLUSTERS));
    }

    /**
     * 获取所有数据存储域
     * @return
     */
    public JSONObject getStorageDomains(){
        return sendGet(getRequestUrl(STORAGEDOMAINS));
    }

    /**
     * 获取所有虚拟机
     * @return
     */
    public JSONObject getVMs(){
        return sendGet(getRequestUrl(VMS));
    }

    /**
     * 获取所有的事件
     */
    public JSONObject getEvents(){
        return sendGet(getRequestUrl(EVENTS));
    }

    /**
     * 获取告警的事件
     */
    public JSONObject getAlertEvents(){
        return sendGet(getRequestUrl(ALERT_EVENTS));
    }

    /**
     * 获取指定主机的所有NIC
     */
    public JSONObject getHostNics(String hostID){
        return sendGet(getRequestUrl(HOST_NICS,hostID));
    }

    /**
     * 获取指定主机的指定NIC的网络速率
     */
    public JSONObject getHostNicsStatistics(String hostID,String nicID){
        return sendGet(getRequestUrl(HOST_NICS_STATISTICS,hostID,nicID));
    }

    /**
     * 获取指定主机的统计信息
     */
    public JSONObject getHostStatistics(String hostID){
        return sendGet(getRequestUrl(HOST_STATISTICS,hostID));
    }

    /**
     * 获取网络信息
     */
    public JSONObject getNetworks(){
        return sendGet(getRequestUrl(NETWORKS));
    }
}
