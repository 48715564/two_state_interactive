package com.cn.service;

import com.cn.common.utils.DateUtils;
import com.cn.common.utils.YaViVMClinetUtils;
import com.cn.page.AjaxResponse;
import com.vmware.vim25.*;
import com.vmware.vim25.mo.*;
import org.springframework.stereotype.Component;

import java.rmi.RemoteException;
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
            for (Datacenter datacenter : tmpDataCenterList) {
                ObjectContent objectContent = addProps(datacenter,pathList);
                dataCenterList.add(objectContent);
            }
            //获取主机
            List<HostSystem> tmpHostList = vmClinetUtils.getAllHost();
            List<ObjectContent> hostList = new ArrayList<>();
            List<String> hostPathList = new ArrayList<>();
            hostPathList.add("runtime.connectionState");
            hostPathList.add("summary");
            hostPathList.addAll(pathList);

            for (HostSystem hostSystem : tmpHostList) {
                ObjectContent objectContent = addProps(hostSystem,hostPathList);
                hostList.add(objectContent);
            }
            //获取集群
            List<ClusterComputeResource> tmpClusterComputeResourceList = vmClinetUtils.getAllCluster();
            List<ObjectContent> clusterList = new ArrayList<>();
            for (ClusterComputeResource clusterComputeResource : tmpClusterComputeResourceList) {
                ObjectContent objectContent = addProps(clusterComputeResource,pathList);
                clusterList.add(objectContent);
            }
            //获取虚拟机
            List<VirtualMachine> tmpVmList = vmClinetUtils.getAllVM();
            List<ObjectContent> vmList = new ArrayList<>();
            List<String> vmPathList = new ArrayList<>();
            vmPathList.add("runtime.powerState");
            vmPathList.addAll(pathList);
            for (VirtualMachine virtualMachine : tmpVmList) {
                ObjectContent objectContent = addProps(virtualMachine,vmPathList);
                vmList.add(objectContent);
            }
            List<Datastore> tmpDsList = vmClinetUtils.getAllDataStore();
            List<ObjectContent> dsList = new ArrayList<>();
            List<String> dataStorePathList = new ArrayList<>();
            dataStorePathList.add("summary");
            dataStorePathList.add("summary.accessible");
            dataStorePathList.addAll(pathList);
            for (Datastore datastore : tmpDsList) {
                ObjectContent objectContent = addProps(datastore,dataStorePathList);
                dsList.add(objectContent);
            }
            //获取网络
            List<Network> tmpNetworkList = vmClinetUtils.getAllNetWork();
            List<ObjectContent> networkList = new ArrayList<>();
            for (Network network : tmpNetworkList) {
                ObjectContent objectContent  = addProps(network,pathList);
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
            Map<String, Object> map = vmClinetUtils.getMonitorAllData(host, intervalTime);
            if (map != null) {
                List<PerfEntityMetricBase> listpemb = (List<PerfEntityMetricBase>) map.get("listpemb");
                Map<Integer, PerfCounterInfo> counters = (Map<Integer, PerfCounterInfo>) map.get("counters");
                //cpu使用率
                Map<String, Object> cpuMap = getDateList(vmClinetUtils.getMonitorData(listpemb, counters, "usage", "cpu"), intervalTime);

                //内存使用率
                Map<String, Object> memMap = getDateList(vmClinetUtils.getMonitorData(listpemb, counters, "usage", "mem"), intervalTime);
                //磁盘使用率
                Map<String, Object> diskMap = getDateList(vmClinetUtils.getMonitorData(listpemb, counters, "usage", "disk"), intervalTime);
                //磁盘使用率
                Map<String, Object> netMap = getDateList(vmClinetUtils.getMonitorData(listpemb, counters, "usage", "net"), intervalTime);
                Map<String, Object> dataMap = new LinkedHashMap<>();
                dataMap.put("data1", cpuMap);
                dataMap.put("data2", memMap);
                dataMap.put("data3", diskMap);
                dataMap.put("data4", netMap);
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

    public AjaxResponse<Map<String, Object>> getVMMonitorData(String vmName) throws Exception {
        try {
            int intervalTime = 20;
            connectionVm();
            AjaxResponse<Map<String, Object>> ajaxResponse = new AjaxResponse<>();
            YaViVMClinetUtils vmClinetUtils = threadLocal.get();
            ManagedEntity host = vmClinetUtils.getVMByName(vmName);
            Map<String, Object> map = vmClinetUtils.getMonitorAllData(host, intervalTime);
            if (map != null) {
                List<PerfEntityMetricBase> listpemb = (List<PerfEntityMetricBase>) map.get("listpemb");
                Map<Integer, PerfCounterInfo> counters = (Map<Integer, PerfCounterInfo>) map.get("counters");
                //cpu使用率
                Map<String, Object> cpuMap = getDateList(vmClinetUtils.getMonitorData(listpemb, counters, "usage", "cpu"), intervalTime);
                //内存使用率
                Map<String, Object> memMap = getDateList(vmClinetUtils.getMonitorData(listpemb, counters, "usage", "mem"), intervalTime);
                //磁盘最长滞后时间
                Map<String, Object> diskMap = getDateList(vmClinetUtils.getMonitorData(listpemb, counters, "maxTotalLatency", "disk"), intervalTime);
                //网络使用率
                Map<String, Object> netMap = getDateList(vmClinetUtils.getMonitorData(listpemb, counters, "usage", "net"), intervalTime);
                Map<String, Object> dataMap = new LinkedHashMap<>();
                dataMap.put("data1", cpuMap);
                dataMap.put("data2", memMap);
                dataMap.put("data3", diskMap);
                dataMap.put("data4", netMap);
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
            int intervalTime = 1800;
            connectionVm();
            Date eTime = new Date();
            Date sTime = new Date(eTime.getTime() - 24 * 60 * 60 * 1000);
            AjaxResponse<Map<String, Object>> ajaxResponse = new AjaxResponse<>();
            YaViVMClinetUtils vmClinetUtils = threadLocal.get();
            ManagedEntity datcenter = vmClinetUtils.getClusterByName(clusterName);
            Map<String, Object> map = vmClinetUtils.getMonitorAllData(datcenter, intervalTime, sTime, eTime);
            if (map != null) {
                List<PerfEntityMetricBase> listpemb = (List<PerfEntityMetricBase>) map.get("listpemb");
                Map<Integer, PerfCounterInfo> counters = (Map<Integer, PerfCounterInfo>) map.get("counters");
                //cpu使用率
                Map<String, Object> cpuMap = getDateList(vmClinetUtils.getMonitorData(listpemb, counters, "usage", "cpu"), intervalTime);
                //cpu使用情况 MHZ
                Map<String, Object> cpuMHZMap = getDateList(vmClinetUtils.getMonitorData(listpemb, counters, "usagemhz", "cpu"), intervalTime);
                //内存使用率
                Map<String, Object> memMap = getDateList(vmClinetUtils.getMonitorData(listpemb, counters, "usage", "mem"), intervalTime);
                //内存已消耗 KB
                Map<String, Object> memConsumedMap = getDateList(vmClinetUtils.getMonitorData(listpemb, counters, "consumed", "mem"), intervalTime);
                Map<String, Object> dataMap = new LinkedHashMap<>();
                dataMap.put("data1", ignoreLast(cpuMap));
                dataMap.put("data2", ignoreLast(cpuMHZMap));
                dataMap.put("data3", ignoreLast(memMap));
                dataMap.put("data4", ignoreLast(memConsumedMap));
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

    /**
     * 忽略统计结果最后出现的0,-1
     *
     * @return
     */
    private Map<String, Object> ignoreLast(Map<String, Object> dataMap) {
        if (dataMap != null) {
            List<Map<String, Object>> longs = (List<Map<String, Object>>) dataMap.get("longs");
            if (longs != null && longs.size() > 0) {
                Map<String, Object> log = longs.stream().filter(item -> "".equals(item.get("instance"))).findAny().get();
                long[] list = (long[]) log.get("list");
                Long[] longList = new Long[list.length];
                for (int i = 0; i < list.length; i++) {
                    longList[i] = list[i];
                }
                for (int i = longList.length - 1; i >= 0; i--) {
                    if (longList[i] <= 0) {
                        longList[i] = null;
                    } else {
                        break;
                    }
                }
                log.put("list", longList);
            }
        }
        return dataMap;
    }

    public AjaxResponse<Map<String, Object>> getDataCenterMonitorData(String dataCenterName) throws Exception {
        try {
            int intervalTime = 1800;
            connectionVm();
            AjaxResponse<Map<String, Object>> ajaxResponse = new AjaxResponse<>();
            YaViVMClinetUtils vmClinetUtils = threadLocal.get();
            ManagedEntity datcenter = vmClinetUtils.getDataCenterByName(dataCenterName);
            Date eTime = new Date();
            Date sTime = new Date(eTime.getTime() - 24 * 60 * 60 * 1000);
            Map<String, Object> map = vmClinetUtils.getMonitorAllData(datcenter, intervalTime, sTime, eTime);
            if (map != null) {
                List<PerfEntityMetricBase> listpemb = (List<PerfEntityMetricBase>) map.get("listpemb");
                Map<Integer, PerfCounterInfo> counters = (Map<Integer, PerfCounterInfo>) map.get("counters");
                //虚拟机打开电源次数
                Map<String, Object> numPoweronMap = getDateList(vmClinetUtils.getMonitorData(listpemb, counters, "numPoweron", "vmop"), intervalTime);
                //虚拟机关闭电源次数
                Map<String, Object> numPoweroffMap = getDateList(vmClinetUtils.getMonitorData(listpemb, counters, "numPoweroff", "vmop"), intervalTime);
                //虚拟机克隆次数
                Map<String, Object> numCloneMap = getDateList(vmClinetUtils.getMonitorData(listpemb, counters, "numClone", "vmop"), intervalTime);
                //虚拟机创建次数
                Map<String, Object> numCreateMap = getDateList(vmClinetUtils.getMonitorData(listpemb, counters, "numCreate", "vmop"), intervalTime);
                Map<String, Object> dataMap = new LinkedHashMap<>();
                dataMap.put("data1", numPoweronMap);
                dataMap.put("data2", numPoweroffMap);
                dataMap.put("data3", numCloneMap);
                dataMap.put("data4", numCreateMap);
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

    private Map<String, Object> getDateList(Map<String, Object> dataMap, int intervalTime) {
        if (dataMap != null) {
            String startTime = String.valueOf(dataMap.get("startTime"));
            String endTime = String.valueOf(dataMap.get("endTime"));
            List<Map> longs = (List<Map>) dataMap.get("longs");
            if (longs.size() > 0) {
                Map<String, Object> tmpMap = longs.get(0);
                long[] list = (long[]) tmpMap.get("list");
                dataMap.put("xLine", DateUtils.getAllDateStr(startTime, endTime, list.length, intervalTime));
            }
        }
        return dataMap;
    }

    public AjaxResponse<Map<String, Object>> getDataStoreMonitorData(String dsName) throws Exception {
        try {
            int intervalTime = 7200;
            connectionVm();
            AjaxResponse<Map<String, Object>> ajaxResponse = new AjaxResponse<>();
            YaViVMClinetUtils vmClinetUtils = threadLocal.get();
            Datastore datastore = (Datastore) vmClinetUtils.getDatastoreByName(dsName);
            Date eTime = new Date();
            Date sTime = new Date(eTime.getTime() - 24 * 60 * 60 * 1000);
            Map<String, Object> map = vmClinetUtils.getMonitorAllData(datastore, intervalTime, sTime, eTime);
            Map<String, Object> datamap = new LinkedHashMap<>();
            if (map != null) {
                List<PerfEntityMetricBase> listpemb = (List<PerfEntityMetricBase>) map.get("listpemb");
                Map<Integer, PerfCounterInfo> counters = (Map<Integer, PerfCounterInfo>) map.get("counters");
                //已使用
                Map<String, Object> usedMap = getDateList(vmClinetUtils.getMonitorData(listpemb, counters, "used", "disk"), intervalTime);
                //虚拟机关闭电源次数
                Map<String, Object> provisionedMap = getDateList(vmClinetUtils.getMonitorData(listpemb, counters, "provisioned", "disk"), intervalTime);
                //虚拟机克隆次数
                Map<String, Object> capacityMap = getDateList(vmClinetUtils.getMonitorData(listpemb, counters, "capacity", "disk"), intervalTime);
                datamap.put("usedMap", usedMap);
                datamap.put("provisionedMap", provisionedMap);
                datamap.put("capacityMap", capacityMap);
            }
            datamap.put("summary", datastore.getSummary());
            ajaxResponse.setResult(datamap);
            return ajaxResponse;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            disconnectionVm();
        }
    }

    public AjaxResponse<Map<String, Object>> getNetworkMonitorData(String networkName) throws Exception {
        try {
            connectionVm();
            AjaxResponse<Map<String, Object>> ajaxResponse = new AjaxResponse<>();
            YaViVMClinetUtils vmClinetUtils = threadLocal.get();
            Network network = (Network) vmClinetUtils.getNetworkByName(networkName);
            Map<String, Object> datamap = new LinkedHashMap<>();
            HostSystem[] hostSystems = network.getHosts();
            VirtualMachine[] virtualMachines = network.getVms();
            List<String> pathList = new ArrayList<>();
            pathList.add("name");
            pathList.add("overallStatus");
            List<String> hostPathList = new ArrayList<>();
            hostPathList.add("runtime.connectionState");
            hostPathList.add("summary");
            hostPathList.addAll(pathList);
            List<ObjectContent> hostList = new ArrayList<>();
            for (HostSystem hostSystem : hostSystems) {
                ObjectContent objectContent = addProps(hostSystem,hostPathList);
                hostList.add(objectContent);
            }
            List<String> vmPathList = new ArrayList<>();
            vmPathList.add("runtime.powerState");
            vmPathList.addAll(pathList);
            List<ObjectContent> vmList = new ArrayList<>();
            for (VirtualMachine virtualMachine : virtualMachines) {
                ObjectContent objectContent = addProps(virtualMachine,vmPathList);
                vmList.add(objectContent);
            }
            datamap.put("hostList",hostList);
            datamap.put("vmList",vmList);
            ajaxResponse.setResult(datamap);
            return ajaxResponse;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            disconnectionVm();
        }
    }

    private ObjectContent addProps(ManagedEntity managedEntity, List<String> pathList) throws RemoteException {
        YaViVMClinetUtils vmClinetUtils = threadLocal.get();
        ObjectContent objectContent = new ObjectContent();
        objectContent.obj = managedEntity.getMOR();
        DynamicProperty[] dynamicProperties = vmClinetUtils.getDynamicPropertyByPaths(pathList, managedEntity);
        objectContent.propSet = dynamicProperties;
        return objectContent;
    }
}
