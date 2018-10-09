package com.cn.service;

import com.cn.common.utils.DateUtils;
import com.cn.common.utils.OvirtApi;
import com.cn.common.utils.SSanApi;
import com.cn.page.AjaxResponse;
import com.xiaoleilu.hutool.json.JSONArray;
import com.xiaoleilu.hutool.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
public class OvirtService {
    @Autowired
    OvirtApi ovirtApi;
    @Autowired
    SSanApi sSanApi;

    private BigDecimal changeNull(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return BigDecimal.valueOf(0);
        } else {
            return bigDecimal;
        }
    }

    private JSONArray changeJsonArrayNull(JSONArray jsonArray) {
        if (jsonArray == null) {
            return new JSONArray();
        } else {
            return jsonArray;
        }
    }

    /**
     * 获取所有主机
     *
     * @return
     */

    private JSONArray getHosts() {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = ovirtApi.getHosts();
        if (jsonObject != null) {
            jsonArray = changeJsonArrayNull(jsonObject.getJSONArray("host"));

        }
        return jsonArray;
    }

    /**
     * 获取所有存储域
     *
     * @return
     */

    private JSONArray getStorageDomains() {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = ovirtApi.getStorageDomains();
        if (jsonObject != null) {
            jsonArray = changeJsonArrayNull(jsonObject.getJSONArray("storage_domain"));

        }
        return jsonArray;
    }

    /**
     * 获取所有网络
     *
     * @return
     */
    private JSONArray getNetworks() {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = ovirtApi.getNetworks();
        if (jsonObject != null) {
            jsonArray = changeJsonArrayNull(jsonObject.getJSONArray("network"));
        }
        return jsonArray;
    }

    /**
     * 获取所有告警信息
     *
     * @return
     */
    private JSONArray getAlertEvents() {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = ovirtApi.getAlertEvents();
        if (jsonObject != null) {
            jsonArray = changeJsonArrayNull(jsonObject.getJSONArray("event"));
        }
        return jsonArray;
    }

    /**
     * 获取存储十分钟的iops信息
     *
     * @return
     */
    private Map<String, JSONArray> getStorageBandWidth() {
        Map<String, JSONArray> map = new LinkedHashMap<>();
        JSONArray jsonArrayWrite = new JSONArray();
        JSONArray jsonArrayRead = new JSONArray();
        JSONObject jsonObject = sSanApi.getStorageBandWidth();
        if (jsonObject != null) {
            jsonArrayWrite = changeJsonArrayNull(jsonObject.getJSONArray("bandwidth_write"));
            jsonArrayRead = changeJsonArrayNull(jsonObject.getJSONArray("bandwidth_read"));

        }
        map.put("write", jsonArrayWrite);
        map.put("read", jsonArrayRead);
        return map;
    }

    /**
     * 获取存储十分钟最新的iops信息
     *
     * @return
     */
    private Map<String, JSONArray> getNowStorageBandWidth() {
        Map<String, JSONArray> map = new LinkedHashMap<>();
        JSONArray jsonArrayWrite = new JSONArray();
        JSONArray jsonArrayRead = new JSONArray();
        JSONObject jsonObject = sSanApi.getNowStorageBandWidth();
        if (jsonObject != null) {
            jsonArrayWrite = changeJsonArrayNull(jsonObject.getJSONArray("bandwidth_write"));
            jsonArrayRead = changeJsonArrayNull(jsonObject.getJSONArray("bandwidth_read"));

        }
        map.put("write", jsonArrayWrite);
        map.put("read", jsonArrayRead);
        return map;
    }

    /**
     * 根据主机ID获取统计信息
     *
     * @return
     */
    private JSONArray getHostStatistic(JSONObject host) {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = ovirtApi.getHostStatistics(host.getStr("id"));
        if (jsonObject != null) {
            jsonArray = changeJsonArrayNull(jsonObject.getJSONArray("statistic"));
        }
        return jsonArray;
    }

    private JSONArray getHostNics(JSONObject host) {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = ovirtApi.getHostNics(host.getStr("id"));
        if (jsonObject != null) {
            jsonArray = changeJsonArrayNull(jsonObject.getJSONArray("host_nic"));
        }
        return jsonArray;
    }

    private JSONArray getNicStatistics(JSONObject host, JSONObject nic) {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = ovirtApi.getHostNicsStatistics(host.getStr("id"), nic.getStr("id"));
        if (jsonObject != null) {
            jsonArray = changeJsonArrayNull(jsonObject.getJSONArray("statistic"));
        }
        return jsonArray;
    }

    private Map<String, BigDecimal> getStorageInfo(JSONArray storageDomains) {
        BigDecimal availableCount = BigDecimal.valueOf(0);
        BigDecimal usedCount = BigDecimal.valueOf(0);
        Map<String, BigDecimal> stringBigDecimalMap = new LinkedHashMap<>();
        for (int i = 0; i < storageDomains.size(); i++) {
            JSONObject storageDomain = storageDomains.getJSONObject(i);
            availableCount = availableCount.add(changeNull(storageDomain.getBigDecimal("available")));
            usedCount = usedCount.add(changeNull(storageDomain.getBigDecimal("used")));
        }
        stringBigDecimalMap.put("availableCount", availableCount);
        stringBigDecimalMap.put("usedCount", usedCount);
        stringBigDecimalMap.put("allCount", availableCount.add(usedCount));
        return stringBigDecimalMap;
    }

    private Map<String, BigDecimal> getSSanStorageInfo() {
        JSONObject jsonObject = sSanApi.getStorage();
        BigDecimal availableCount = jsonObject.getBigDecimal("storage_free");
        BigDecimal usedCount = jsonObject.getBigDecimal("storage_used");
        Map<String, BigDecimal> stringBigDecimalMap = new LinkedHashMap<>();

        stringBigDecimalMap.put("availableCount", availableCount);
        stringBigDecimalMap.put("usedCount", usedCount);
        stringBigDecimalMap.put("allCount", availableCount.add(usedCount));
        return stringBigDecimalMap;
    }

    private int getNormalHostCount(JSONArray hosts) {
        int count = 0;
        for (int i = 0; i < hosts.size(); i++) {
            JSONObject host = hosts.getJSONObject(i);
            if ("up".equals(host.getStr("status"))) {
                count++;
            }
        }
        return count;
    }

    /**
     * 首页数据获取
     *
     * @return
     */
    public AjaxResponse<JSONObject> getIndexInfo() {
        JSONObject jsonObject = new JSONObject();
        //主机相关的数量信息
        Map<String, String> hostInfo = new LinkedHashMap<>();
        JSONArray hosts = getHosts();
        int hostCount = hosts.size();
        int normalHostCount = getNormalHostCount(hosts);
        int unNormalHostCount = hostCount - normalHostCount;
        hostInfo.put("normalHostCount", String.valueOf(normalHostCount));
        hostInfo.put("unNormalHostCount", String.valueOf(unNormalHostCount));
        hostInfo.put("hostCount", String.valueOf(hostCount));
        jsonObject.put("hostInfo", hostInfo);

        //存储相关的信息
        jsonObject.put("storageInfo", getSSanStorageInfo());

        //网络相关的信息
        JSONArray networks = getNetworks();
        Map<String, Object> networkInfo = new LinkedHashMap<>();
        networkInfo.put("count", networks.size());
        networkInfo.put("networks", networks);
        jsonObject.put("networkInfo", networkInfo);
        //告警相关信息
        JSONArray alertEvents = getAlertEvents();
        Map<String, Object> alertEventInfo = new LinkedHashMap<>();
        alertEventInfo.put("count", alertEvents.size());
        alertEventInfo.put("alertEvents", alertEvents);
        jsonObject.put("alertEvents", alertEvents);
        return new AjaxResponse(jsonObject);
    }

    private BigDecimal getCpuUsed(List<JSONArray> jsonArrays) {
        BigDecimal cpu = BigDecimal.valueOf(0);
        int count = 0;
        for (JSONArray statistics : jsonArrays) {
            for (int i = 0; i < statistics.size(); i++) {
                if ("cpu.current.idle".equals(statistics.getJSONObject(i).getStr("name"))) {
                    JSONObject values = statistics.getJSONObject(i).getJSONObject("values");
                    JSONArray value = values.getJSONArray("value");
                    for (int j = 0; j < value.size(); j++) {
                        JSONObject v = value.getJSONObject(j);
                        if (v.getBigDecimal("datum") != null) {
                            count++;
                            cpu = cpu.add(BigDecimal.valueOf(100).subtract(v.getBigDecimal("datum")));
                        }
                    }
                }
            }
        }
        if (count != 0) {
            cpu = cpu.divide(BigDecimal.valueOf(count), 2, BigDecimal.ROUND_HALF_UP);
        }
        return cpu;
    }

    private Map<String, BigDecimal> getNicInfo(List<JSONArray> jsonArrays) {
        Map<String, BigDecimal> map = new LinkedHashMap<>();
        BigDecimal receiveData = BigDecimal.valueOf(0);
        BigDecimal transmitData = BigDecimal.valueOf(0);
        for (JSONArray statistics : jsonArrays) {
            for (int i = 0; i < statistics.size(); i++) {
                if ("data.current.rx".equals(statistics.getJSONObject(i).getStr("name"))) {
                    JSONObject values = statistics.getJSONObject(i).getJSONObject("values");
                    JSONArray value = values.getJSONArray("value");
                    for (int j = 0; j < value.size(); j++) {
                        JSONObject v = value.getJSONObject(j);
                        if (v.getBigDecimal("datum") != null) {
                            receiveData = receiveData.add(v.getBigDecimal("datum"));
                        }
                    }
                } else if ("data.current.tx".equals(statistics.getJSONObject(i).getStr("name"))) {
                    JSONObject values = statistics.getJSONObject(i).getJSONObject("values");
                    JSONArray value = values.getJSONArray("value");
                    for (int j = 0; j < value.size(); j++) {
                        JSONObject v = value.getJSONObject(j);
                        if (v.getBigDecimal("datum") != null) {
                            transmitData = transmitData.add(v.getBigDecimal("datum"));
                        }
                    }
                }
            }
        }
        map.put("receiveData", receiveData);
        map.put("transmitData", transmitData);
        return map;
    }


    private BigDecimal getMenoryUsed(List<JSONArray> jsonArrays) {
        BigDecimal menoryUsed = BigDecimal.valueOf(0);
        BigDecimal allMonery = BigDecimal.valueOf(0);
        for (JSONArray statistics : jsonArrays) {
            for (int i = 0; i < statistics.size(); i++) {
                if ("memory.total".equals(statistics.getJSONObject(i).getStr("name"))) {
                    JSONObject values = statistics.getJSONObject(i).getJSONObject("values");
                    JSONArray value = values.getJSONArray("value");
                    for (int j = 0; j < value.size(); j++) {
                        JSONObject v = value.getJSONObject(j);
                        if (v.getBigDecimal("datum") != null) {
                            allMonery = allMonery.add(v.getBigDecimal("datum"));
                        }
                    }
                } else if ("memory.used".equals(statistics.getJSONObject(i).getStr("name"))) {
                    JSONObject values = statistics.getJSONObject(i).getJSONObject("values");
                    JSONArray value = values.getJSONArray("value");
                    for (int j = 0; j < value.size(); j++) {
                        JSONObject v = value.getJSONObject(j);
                        if (v.getBigDecimal("datum") != null) {
                            menoryUsed = menoryUsed.add(v.getBigDecimal("datum"));
                        }
                    }
                }
            }
        }
        if (!allMonery.equals(BigDecimal.valueOf(0))) {
            return menoryUsed.divide(allMonery, 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)).setScale(2);
        }
        return BigDecimal.valueOf(0);
    }


    /**
     * 获取实时CPU使用率
     */
    public AjaxResponse<Map<String, Object>> monitorData() {
        Map<String, Object> map = new LinkedHashMap<>();
        JSONArray hosts = getHosts();
        List<JSONArray> jsonArrays = new LinkedList<>();
        for (int i = 0; i < hosts.size(); i++) {
            JSONArray statistics = getHostStatistic(hosts.getJSONObject(i));
            jsonArrays.add(statistics);
        }

        List<JSONArray> netJsonArrays = new LinkedList<>();
        for (int i = 0; i < hosts.size(); i++) {
            JSONArray nics = getHostNics(hosts.getJSONObject(i));
            for (int j = 0; j < nics.size(); j++) {
                JSONObject jsonObject = nics.getJSONObject(j);
                JSONArray nicArray = getNicStatistics(hosts.getJSONObject(i), jsonObject);
                netJsonArrays.add(nicArray);
            }
        }

        Map<String, BigDecimal> nicInfo = getNicInfo(netJsonArrays);
        BigDecimal menoryUsed = getMenoryUsed(jsonArrays);
        BigDecimal cpuUsed = getCpuUsed(jsonArrays);
        map.put("menoryUsed", menoryUsed);
        map.put("cpuUsed", cpuUsed);
        map.put("nicInfo", nicInfo);
        return new AjaxResponse<>(map);
    }

    public AjaxResponse<Map<String, List<String>>> monitorStoreBandWidthData() {
        Map<String, JSONArray> iopsData = getStorageBandWidth();
        List<String> timeList = new ArrayList<>();
        List<String> readData = new ArrayList<>();
        List<String> writeDate = new ArrayList<>();
        JSONArray readJsonArray = iopsData.get("read");
        JSONArray writeJsonArray = iopsData.get("write");
        for(int i = 0;i<readJsonArray.size();i++){
            JSONObject jsonObject = readJsonArray.getJSONObject(i);
            timeList.add(DateUtils.getTime(jsonObject.getLong("time")));
            readData.add(jsonObject.getStr("value"));
        }
        for(int i = 0;i<writeJsonArray.size();i++){
            JSONObject jsonObject = writeJsonArray.getJSONObject(i);
            writeDate.add(jsonObject.getStr("value"));
        }
        Map<String,List<String>> dataList = new LinkedHashMap<>();
        dataList.put("timeData",timeList);
        dataList.put("readData",readData);
        dataList.put("writeDate",writeDate);
        return new AjaxResponse<>(dataList);
    }


    public AjaxResponse<Map<String, String>> monitorNowStoreIOPSData() {
        Map<String, JSONArray> iopsData = getNowStorageBandWidth();
        List<String> timeList = new ArrayList<>();
        List<String> readData = new ArrayList<>();
        List<String> writeDate = new ArrayList<>();
        JSONArray readJsonArray = iopsData.get("read");
        JSONArray writeJsonArray = iopsData.get("write");
        for(int i = 0;i<readJsonArray.size();i++){
            JSONObject jsonObject = readJsonArray.getJSONObject(i);
            timeList.add(DateUtils.getTime(jsonObject.getLong("time")));
            readData.add(jsonObject.getStr("value"));
        }
        for(int i = 0;i<writeJsonArray.size();i++){
            JSONObject jsonObject = writeJsonArray.getJSONObject(i);
            writeDate.add(jsonObject.getStr("value"));
        }
        Map<String,String> dataList = new LinkedHashMap<>();
        if(timeList.size()>0&&readData.size()>0&&writeDate.size()>0){
            dataList.put("timeData",timeList.get(0));
            dataList.put("readData",readData.get(0));
            dataList.put("writeDate",writeDate.get(0));
        }
        return new AjaxResponse<>(dataList);
    }
}
