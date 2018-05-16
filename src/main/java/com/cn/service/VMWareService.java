package com.cn.service;

import com.cn.common.utils.DateUtils;
import com.cn.common.utils.YaViVMClinetUtils;
import com.cn.page.AjaxResponse;
import com.vmware.vim25.*;
import com.vmware.vim25.mo.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class VMWareService {
    ThreadLocal<YaViVMClinetUtils> threadLocal = new ThreadLocal<>();

    private void connectionVm() throws Exception {
        YaViVMClinetUtils vmClinetUtils = new YaViVMClinetUtils();
        vmClinetUtils.connect();
        threadLocal.set(vmClinetUtils);
    }

    private void disconnectionVm() throws Exception {
        YaViVMClinetUtils vmClinetUtils = threadLocal.get();
        if (vmClinetUtils != null) {
            vmClinetUtils.disconnect();
            threadLocal.remove();
        }
    }

    /**
     * 获取首页虚拟机个数
     *
     * @return
     * @throws Exception
     */
    public AjaxResponse<Map<String, List<ObjectContent>>> getIndexCount() throws Exception {
        try {
            connectionVm();
            Map data = new LinkedHashMap();
            YaViVMClinetUtils vmClinetUtils = threadLocal.get();
            //获取数据中心
            List<Datacenter> tmpDataCenterList = vmClinetUtils.getAllDataCeneter();
            List<ObjectContent> dataCenterList = new ArrayList<>();
            List<String> pathList = new ArrayList<>();
            pathList.add("name");
            pathList.add("overallStatus");
            for(Datacenter datacenter:tmpDataCenterList){
                ObjectContent objectContent = new ObjectContent();
                objectContent.obj = datacenter.getMOR();
                DynamicProperty[] dynamicProperties = vmClinetUtils.getDynamicPropertyByPaths(pathList,datacenter);
                objectContent.propSet=dynamicProperties;
                dataCenterList.add(objectContent);
            }
            //获取主机
            List<HostSystem> tmpHostList = vmClinetUtils.getAllHost();
            List<ObjectContent> hostList = new ArrayList<>();
            List<String> hostPathList = new ArrayList<>();
            hostPathList.add("runtime.connectionState");
            hostPathList.add("summary");
            hostPathList.addAll(pathList);

            for(HostSystem hostSystem:tmpHostList){
                ObjectContent objectContent = new ObjectContent();
                objectContent.obj = hostSystem.getMOR();
                DynamicProperty[] dynamicProperties = vmClinetUtils.getDynamicPropertyByPaths(hostPathList,hostSystem);
                objectContent.propSet=dynamicProperties;
                hostList.add(objectContent);
            }
            //获取集群
            List<ClusterComputeResource> tmpClusterComputeResourceList = vmClinetUtils.getAllCluster();
            List<ObjectContent> clusterList = new ArrayList<>();
            for(ClusterComputeResource clusterComputeResource:tmpClusterComputeResourceList){
                ObjectContent objectContent = new ObjectContent();
                objectContent.obj = clusterComputeResource.getMOR();
                DynamicProperty[] dynamicProperties = vmClinetUtils.getDynamicPropertyByPaths(pathList,clusterComputeResource);
                objectContent.propSet=dynamicProperties;
                clusterList.add(objectContent);
            }
            //获取虚拟机
            List<VirtualMachine> tmpVmList = vmClinetUtils.getAllVM();
            List<ObjectContent> vmList = new ArrayList<>();
            List<String> vmPathList = new ArrayList<>();
            vmPathList.add("runtime.powerState");
            vmPathList.addAll(pathList);
            for(VirtualMachine virtualMachine:tmpVmList){
                ObjectContent objectContent = new ObjectContent();
                objectContent.obj = virtualMachine.getMOR();
                DynamicProperty[] dynamicProperties = vmClinetUtils.getDynamicPropertyByPaths(vmPathList,virtualMachine);
                objectContent.propSet=dynamicProperties;
                vmList.add(objectContent);
            }
            List<Datastore> tmpDsList = vmClinetUtils.getAllDataStore();
            List<ObjectContent> dsList = new ArrayList<>();
            List<String> dataStorePathList = new ArrayList<>();
            dataStorePathList.add("summary");
            dataStorePathList.addAll(pathList);
            for(Datastore datastore:tmpDsList){
                ObjectContent objectContent = new ObjectContent();
                objectContent.obj = datastore.getMOR();
                DynamicProperty[] dynamicProperties = vmClinetUtils.getDynamicPropertyByPaths(dataStorePathList,datastore);
                objectContent.propSet=dynamicProperties;
                dsList.add(objectContent);
            }
            //获取网络
            List<Network> tmpNetworkList = vmClinetUtils.getAllNetWork();
            List<ObjectContent> networkList = new ArrayList<>();
            for(Network network:tmpNetworkList){
                ObjectContent objectContent = new ObjectContent();
                objectContent.obj = network.getMOR();
                DynamicProperty[] dynamicProperties = vmClinetUtils.getDynamicPropertyByPaths(pathList,network);
                objectContent.propSet=dynamicProperties;
                networkList.add(objectContent);
            }
            data.put("dataCenterList", dataCenterList);
            data.put("hostList", hostList);
            data.put("clusterList", clusterList);
            data.put("vmList", vmList);
            data.put("dsList", dsList);
            data.put("networkList", networkList);
            AjaxResponse<Map<String, List<ObjectContent>>> ajaxResponse = new AjaxResponse<>();
            ajaxResponse.setResult(data);
            return ajaxResponse;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            disconnectionVm();
        }
    }

    public AjaxResponse<Map<String, Object>> getHostMonitorData(String hostName) throws Exception {
        try {
            int intervalTime = 20;
            connectionVm();
            AjaxResponse<Map<String, Object>> ajaxResponse = new AjaxResponse<>();
            YaViVMClinetUtils vmClinetUtils = threadLocal.get();
            ManagedEntity host = vmClinetUtils.getHostByName(hostName);
            Map<String, Object> map = vmClinetUtils.getMonitorAllData(host,intervalTime);
            if(map!=null) {
                List<PerfEntityMetricBase> listpemb = (List<PerfEntityMetricBase>) map.get("listpemb");
                Map<Integer, PerfCounterInfo> counters = (Map<Integer, PerfCounterInfo>) map.get("counters");
                //cpu使用率
                Map<String,Object> cpuMap = getDateList(vmClinetUtils.getMonitorData(listpemb,counters,"usage","cpu"),intervalTime);

                //内存使用率
                Map<String,Object> memMap = getDateList(vmClinetUtils.getMonitorData(listpemb,counters,"usage","mem"),intervalTime);
                //磁盘使用率
                Map<String,Object> diskMap = getDateList(vmClinetUtils.getMonitorData(listpemb,counters,"usage","disk"),intervalTime);
                //磁盘使用率
                Map<String,Object> netMap = getDateList(vmClinetUtils.getMonitorData(listpemb,counters,"usage","net"),intervalTime);
                Map<String,Object> dataMap = new LinkedHashMap<>();
                dataMap.put("data1",cpuMap);
                dataMap.put("data2",memMap);
                dataMap.put("data3",diskMap);
                dataMap.put("data4",netMap);
                ajaxResponse.setResult(dataMap);
            }
            return ajaxResponse;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            disconnectionVm();
        }
    }


    public AjaxResponse<Map<String, Object>> getClusterMonitorData(String clusterName) throws Exception {
        try {
            int intervalTime = 300;
            connectionVm();
            AjaxResponse<Map<String, Object>> ajaxResponse = new AjaxResponse<>();
            YaViVMClinetUtils vmClinetUtils = threadLocal.get();
            ManagedEntity datcenter = vmClinetUtils.getClusterByName(clusterName);
            Map<String, Object> map = vmClinetUtils.getMonitorAllData(datcenter,intervalTime);
            if(map!=null) {
                List<PerfEntityMetricBase> listpemb = (List<PerfEntityMetricBase>) map.get("listpemb");
                Map<Integer, PerfCounterInfo> counters = (Map<Integer, PerfCounterInfo>) map.get("counters");
                //cpu使用率
                Map<String,Object> cpuMap = getDateList(vmClinetUtils.getMonitorData(listpemb,counters,"usage","cpu"),intervalTime);
                //cpu使用情况 MHZ
                Map<String,Object> cpuMHZMap = getDateList(vmClinetUtils.getMonitorData(listpemb,counters,"usagemhz","cpu"),intervalTime);
                //内存使用率
                Map<String,Object> memMap = getDateList(vmClinetUtils.getMonitorData(listpemb,counters,"usage","mem"),intervalTime);
                //内存已消耗 KB
                Map<String,Object> memConsumedMap = getDateList(vmClinetUtils.getMonitorData(listpemb,counters,"consumed","mem"),intervalTime);
                Map<String,Object> dataMap = new LinkedHashMap<>();
                dataMap.put("data1",cpuMap);
                dataMap.put("data2",cpuMHZMap);
                dataMap.put("data3",memMap);
                dataMap.put("data4",memConsumedMap);
                ajaxResponse.setResult(dataMap);
            }
            return ajaxResponse;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            disconnectionVm();
        }
    }


    public AjaxResponse<Map<String, Object>> getDataCenterMonitorData(String dataCenterName) throws Exception {
        try {
            int intervalTime = 300;
            connectionVm();
            AjaxResponse<Map<String, Object>> ajaxResponse = new AjaxResponse<>();
            YaViVMClinetUtils vmClinetUtils = threadLocal.get();
            ManagedEntity datcenter = vmClinetUtils.getDataCenterByName(dataCenterName);
            Date eTime = new Date();
            Date sTime = new Date(eTime.getTime() - 24 * 60 * 60 * 1000);
            Map<String, Object> map = vmClinetUtils.getMonitorAllData(datcenter,intervalTime,sTime,eTime);
            if(map!=null) {
                List<PerfEntityMetricBase> listpemb = (List<PerfEntityMetricBase>) map.get("listpemb");
                Map<Integer, PerfCounterInfo> counters = (Map<Integer, PerfCounterInfo>) map.get("counters");
                //虚拟机打开电源次数
                Map<String,Object> numPoweronMap = getDateList(vmClinetUtils.getMonitorData(listpemb,counters,"numPoweron","vmop"),intervalTime);
                //虚拟机关闭电源次数
                Map<String,Object> numPoweroffMap = getDateList(vmClinetUtils.getMonitorData(listpemb,counters,"numPoweroff","vmop"),intervalTime);
                //虚拟机克隆次数
                Map<String,Object> numCloneMap = getDateList(vmClinetUtils.getMonitorData(listpemb,counters,"numClone","vmop"),intervalTime);
                //虚拟机创建次数
                Map<String,Object> numCreateMap = getDateList(vmClinetUtils.getMonitorData(listpemb,counters,"numCreate","vmop"),intervalTime);
                Map<String,Object> dataMap = new LinkedHashMap<>();
                dataMap.put("data1",numPoweronMap);
                dataMap.put("data2",numPoweroffMap);
                dataMap.put("data3",numCloneMap);
                dataMap.put("data4",numCreateMap);
                ajaxResponse.setResult(dataMap);
            }
            return ajaxResponse;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            disconnectionVm();
        }
    }

    private Map<String,Object> getDateList(Map<String,Object> dataMap,int intervalTime){
        if(dataMap!=null) {
            String startTime = String.valueOf(dataMap.get("startTime"));
            String endTime = String.valueOf(dataMap.get("endTime"));
            List<Map> longs = (List<Map>) dataMap.get("longs");
            if(longs.size()>0) {
                Map<String, Object> tmpMap = longs.get(0);
                long[] list = (long[]) tmpMap.get("list");
                dataMap.put("xLine", DateUtils.getAllDateStr(startTime, endTime, list.length,intervalTime));
            }
        }
        return dataMap;
    }
}
