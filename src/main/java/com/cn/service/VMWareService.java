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
            //获取存储
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

    public AjaxResponse<Map<String, Object>> getMonitorData(String hostName) throws Exception {
        try {
            connectionVm();
            AjaxResponse<Map<String, Object>> ajaxResponse = new AjaxResponse<>();
            YaViVMClinetUtils vmClinetUtils = threadLocal.get();
            List<String> pathList = new ArrayList<>();
            pathList.add("name");
            ManagedEntity host = vmClinetUtils.getHostByName(hostName);
            Map<String, Object> map = vmClinetUtils.getMonitorAllData(host,20);
            if(map!=null) {
                List<PerfEntityMetricBase> listpemb = (List<PerfEntityMetricBase>) map.get("listpemb");
                Map<Integer, PerfCounterInfo> counters = (Map<Integer, PerfCounterInfo>) map.get("counters");
                //cpu使用率
                Map<String,Object> cpuMap = getDateList(vmClinetUtils.getMonitorData(listpemb,counters,"usage","cpu"));

                //内存使用率
                Map<String,Object> memMap = getDateList(vmClinetUtils.getMonitorData(listpemb,counters,"usage","mem"));
                //磁盘使用率
                Map<String,Object> diskMap = getDateList(vmClinetUtils.getMonitorData(listpemb,counters,"usage","disk"));
                //磁盘使用率
                Map<String,Object> netMap = getDateList(vmClinetUtils.getMonitorData(listpemb,counters,"usage","net"));
                Map<String,Object> dataMap = new LinkedHashMap<>();
                dataMap.put("cpuMap",cpuMap);
                dataMap.put("memMap",memMap);
                dataMap.put("diskMap",diskMap);
                dataMap.put("netMap",netMap);
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


    private Map<String,Object> getDateList(Map<String,Object> dataMap){
        if(dataMap!=null) {
            String startTime = String.valueOf(dataMap.get("startTime"));
            String endTime = String.valueOf(dataMap.get("endTime"));
            List<Map> longs = (List<Map>) dataMap.get("longs");
            if(longs.size()>0) {
                Map<String, Object> tmpMap = longs.get(0);
                long[] list = (long[]) tmpMap.get("list");
                dataMap.put("xLine", DateUtils.getAllDateStr(startTime, endTime, list.length));
            }
        }
        return dataMap;
    }
}
