package com.cn.common.utils;

import com.cn.common.infrastructure.Constant;
import com.vmware.vim25.*;
import com.vmware.vim25.mo.*;
import com.xiaoleilu.hutool.date.DateUtil;

import javax.xml.ws.soap.SOAPFaultException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.*;

public class YaViVMClinetUtils {
    private String url = Constant.url;
    private String userName = Constant.userName;
    private final String password = Constant.password;
    public ServiceInstance si = null;

    /**
     * @description 链接vcenter
     * @date 2017年2月8日14:23:37
     * @version 1.1
     * @author DiWk
     */
    public void connect() {
        try {
            si = new ServiceInstance(new URL(url), userName, password, true);
        } catch (SOAPFaultException sfe) {
            sfe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @description 断开vcenter链接
     * @date 2017年2月8日14:23:37
     * @version 1.1
     * @author DiWk
     */
    public void disconnect() {
        si.getServerConnection().logout();
    }

    /**
     * @description 获取链接URL
     * @date 2017年2月8日14:23:37
     * @version 1.1
     * @author DiWk
     */
    public URL getUrl() {
        ServerConnection serverConnection = si.getServerConnection();
        URL url = null;
        if (serverConnection != null) {
            url = serverConnection.getUrl();
        } else {
            return null;
        }
        return url;
    }

    public static void main(String[] args) throws RemoteException {
        YaViVMClinetUtils yaViVMClinetUtils = new YaViVMClinetUtils();
        yaViVMClinetUtils.connect();
        yaViVMClinetUtils.disconnect();
    }

    public <T extends ManagedEntity> DynamicProperty[] getDynamicPropertyByPaths(List<String> path, T managedEntity) throws RemoteException {
        DynamicProperty[] dynamicProperties = new DynamicProperty[path.size()];
        Hashtable hashtable = managedEntity.getPropertiesByPaths(path.toArray(new String[path.size()]));
        int i = 0;
        for (Iterator<Map.Entry<String, Object>> iterator = hashtable.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, Object> entry = iterator.next();
            DynamicProperty dynamicProperty = new DynamicProperty();
            dynamicProperty.setName(entry.getKey());
            dynamicProperty.setVal(entry.getValue());
            dynamicProperties[i] = dynamicProperty;
            i++;
        }
        return dynamicProperties;
    }



    public List<Datastore> getAllDataStore() throws RemoteException {
        return getAllData("Datastore");
    }

    public List<Datacenter> getAllDataCeneter() throws RemoteException {
        return this.getAllData("Datacenter");
    }

    public <T> List<T> getAllData(String type) throws RemoteException {
        List<T> dataList = new ArrayList<T>();
        T data = null;
        ManagedEntity[] managedEntities = new InventoryNavigator(si.getRootFolder())
                .searchManagedEntities(type);
        if (managedEntities != null && managedEntities.length > 0) {
            for (ManagedEntity managedEntity : managedEntities) {
                data = (T) managedEntity;
                dataList.add(data);
            }
        }
        return dataList;
    }

    public List<HostSystem> getAllHost() throws RemoteException {
        return this.getAllData("HostSystem");
    }

    public List<ClusterComputeResource> getAllCluster() throws RemoteException {
        return this.getAllData("ClusterComputeResource");
    }

    public List<VirtualMachine> getAllVM() throws RemoteException {
        return this.getAllData("VirtualMachine");
    }

    public List<Network> getAllNetWork() throws RemoteException {
        return this.getAllData("Network");
    }

    public ManagedEntity getManagedEntityByName(String type, String name) throws RemoteException {
        return new InventoryNavigator(si.getRootFolder()).searchManagedEntity(type, name);
    }

    public ManagedEntity getHostByName(String hostName) throws RemoteException {
        return getManagedEntityByName("HostSystem", hostName);
    }

    public ManagedEntity getVMByName(String vmName) throws RemoteException {
        return getManagedEntityByName("VirtualMachine", vmName);
    }

    public ManagedEntity getDataCenterByName(String dataCenterName) throws RemoteException {
        return getManagedEntityByName("Datacenter", dataCenterName);
    }

    public ManagedEntity getClusterByName(String clusterName) throws RemoteException {
        return getManagedEntityByName("ClusterComputeResource", clusterName);
    }

    public ManagedEntity getDatastoreByName(String dsName) throws RemoteException {
        return getManagedEntityByName("Datastore", dsName);
    }

    public ManagedEntity getNetworkByName(String networkName) throws RemoteException {
        return getManagedEntityByName("Network", networkName);
    }

    public Map<String, Object> getMonitorAllData(ManagedEntity mo, int interval) throws RemoteException {
        Map<String, Object> map = new LinkedHashMap<>();
        if (mo != null) {
            PerformanceManager performanceManager = si.getPerformanceManager();
            PerfCounterInfo[] cInfo = performanceManager.getPerfCounter();
            Map<Integer, PerfCounterInfo> counters = new HashMap<Integer, PerfCounterInfo>();
            for (PerfCounterInfo pcInfo : cInfo) {
                counters.put(new Integer(pcInfo.getKey()), pcInfo);
            }
            PerfMetricId[] listpermeid = performanceManager.queryAvailablePerfMetric(mo, null, null, interval);
            ArrayList<PerfMetricId> mMetrics = new ArrayList<PerfMetricId>();
            if (listpermeid != null) {
                for (int index = 0; index < listpermeid.length; ++index) {
                    if (counters.containsKey(new Integer(listpermeid[index].getCounterId()))) {
                        mMetrics.add(listpermeid[index]);
                    }
                }
            }
            PerfQuerySpec qSpec = new PerfQuerySpec();
            qSpec.setEntity(mo.getMOR());
            qSpec.setMetricId(listpermeid);
            qSpec.setIntervalId(interval);
//            qSpec.setFormat("normal");
            PerfQuerySpec[] arryQuery = {qSpec};
            PerfEntityMetricBase[] pValues = performanceManager.queryPerf(arryQuery);
            if(pValues!=null&&counters!=null) {
                List<PerfEntityMetricBase> listpemb = Arrays.asList(pValues);
                map.put("counters", counters);
                map.put("listpemb", listpemb);
            }
        }
        return map;
    }

    public Map<String, Object> getMonitorAllData(ManagedEntity mo, int interval,Date sTime,Date eTime) throws RemoteException {
        Map<String, Object> map = new LinkedHashMap<>();
        if (mo != null) {
            Calendar calBegin = Calendar.getInstance();
            calBegin.setTime(sTime);
            Calendar calEnd = Calendar.getInstance();
            calEnd.setTime(eTime);
            PerformanceManager performanceManager = si.getPerformanceManager();
            PerfCounterInfo[] cInfo = performanceManager.getPerfCounter();
            Map<Integer, PerfCounterInfo> counters = new HashMap<Integer, PerfCounterInfo>();
            for (PerfCounterInfo pcInfo : cInfo) {
                counters.put(new Integer(pcInfo.getKey()), pcInfo);
            }
            PerfMetricId[] listpermeid = performanceManager.queryAvailablePerfMetric(mo, null, null, interval);
            ArrayList<PerfMetricId> mMetrics = new ArrayList<PerfMetricId>();
            if (listpermeid != null) {
                for (int index = 0; index < listpermeid.length; ++index) {
                    if (counters.containsKey(new Integer(listpermeid[index].getCounterId()))) {
                        mMetrics.add(listpermeid[index]);
                    }
                }
            }
            PerfQuerySpec qSpec = new PerfQuerySpec();
            qSpec.setEntity(mo.getMOR());
            qSpec.setMetricId(listpermeid);
            qSpec.setIntervalId(interval);
            qSpec.setStartTime(calBegin);
            qSpec.setEndTime(calEnd);
//            qSpec.setFormat("normal");
            PerfQuerySpec[] arryQuery = {qSpec};
            PerfEntityMetricBase[] pValues = performanceManager.queryPerf(arryQuery);
            List<PerfEntityMetricBase> listpemb = Arrays.asList(pValues);
            map.put("counters", counters);
            map.put("listpemb", listpemb);
        }
        return map;
    }

    public Map<String,Object> getMonitorData(List<PerfEntityMetricBase> listpemb, Map<Integer, PerfCounterInfo> counters, String nameInfo, String groupInfo) {
        if(listpemb!=null&&counters!=null&&listpemb.size()>0&&counters.size()>0) {
            Map<String, Object> map = new LinkedHashMap<>();
            List<Map> longs = new ArrayList<>();
            List<PerfEntityMetricBase> pValues = listpemb;
            PerfSampleInfo[] listperfsinfo = ((PerfEntityMetric) pValues.get(0)).getSampleInfo();
            map.put("startTime", DateUtil.format(listperfsinfo[0].getTimestamp().getTime(),"yyyy-MM-dd HH:mm:ss"));
            map.put("endTime", DateUtil.format(listperfsinfo[listperfsinfo.length - 1].getTimestamp().getTime(),"yyyy-MM-dd HH:mm:ss"));
            for (int i = 0; i < pValues.size(); i++) {
                PerfMetricSeries[] listpems = ((PerfEntityMetric) pValues.get(i)).getValue();
                for (int vi = 0; vi < listpems.length; ++vi) {
                    String printInf = "";
                    PerfCounterInfo pci = (PerfCounterInfo) counters.get(new Integer(listpems[vi].getId().getCounterId()));
                    if (pci != null) {
                        if (pci.getNameInfo().getKey().equalsIgnoreCase(nameInfo) && pci.getGroupInfo().getKey().equalsIgnoreCase(groupInfo)) {
                            if (listpems[vi] instanceof PerfMetricIntSeries) {
                                PerfMetricIntSeries val = (PerfMetricIntSeries) listpems[vi];
                                long[] lislon = val.getValue();
                                Map<String, Object> tmpMap = new LinkedHashMap<>();
                                tmpMap.put("instance", val.getId().getInstance());
                                tmpMap.put("list", lislon);
                                longs.add(tmpMap);
                            }
                        }
                    }
                }
            }
            map.put("longs", longs);
            return map;
        }
        return null;
    }
}
