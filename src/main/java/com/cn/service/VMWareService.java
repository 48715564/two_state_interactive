package com.cn.service;

import com.cn.common.utils.VMClinetUtils;
import com.cn.page.AjaxResponse;
import com.vmware.vim25.ManagedEntityStatus;
import com.vmware.vim25.ObjectContent;
import com.xiaoleilu.hutool.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class VMWareService {
    ThreadLocal<VMClinetUtils> threadLocal = new ThreadLocal<>();
    private void connectionVm() throws Exception {
            VMClinetUtils vmClinetUtils = new VMClinetUtils();
            vmClinetUtils.connect();
            threadLocal.set(vmClinetUtils);
    }

    private void disconnectionVm() throws Exception {
        VMClinetUtils vmClinetUtils = threadLocal.get();
        if(vmClinetUtils!=null) {
            vmClinetUtils.disconnect();
            threadLocal.remove();
        }
    }

    /**
     * 获取首页虚拟机个数
     * @return
     * @throws Exception
     */
    public AjaxResponse<Map<String,List<ObjectContent>>> getIndexCount() throws Exception {
        try {
            connectionVm();
            VMClinetUtils vmClinetUtils = threadLocal.get();
            List<String> pathList = new ArrayList<>();
            pathList.add("name");
            pathList.add("overallStatus");
            Map<String,List<ObjectContent>> data = new LinkedHashMap<>();
            //获取数据中心
            List<ObjectContent> dataCenterList = vmClinetUtils.getAllDataCeneter(pathList);
            List<String> hostPathList = new ArrayList<>();
            hostPathList.add("runtime.connectionState");
            hostPathList.addAll(pathList);
            //获取主机
            List<ObjectContent> hostList = vmClinetUtils.getAllHost(hostPathList);
            //获取集群
            List<ObjectContent> cluster = vmClinetUtils.getAllCluster(pathList);

            List<String> vmPathList = new ArrayList<>();
            vmPathList.add("runtime.powerState");
            vmPathList.addAll(pathList);
            //获取虚拟机
            List<ObjectContent> vmList = vmClinetUtils.getAllVM(vmPathList);

            List<String> dataStorePathList = new ArrayList<>();
            dataStorePathList.add("summary");
            dataStorePathList.addAll(pathList);
            //获取存储
            List<ObjectContent> dsList = vmClinetUtils.getAllDataStore(dataStorePathList);
            //获取网络
            List<ObjectContent> networkList = vmClinetUtils.getAllNetWork(pathList);
            data.put("dataCenterList",dataCenterList);
            data.put("hostList",hostList);
            data.put("clusterList",cluster);
            data.put("vmList",vmList);
            data.put("dsList",dsList);
            data.put("networkList",networkList);
            AjaxResponse<Map<String,List<ObjectContent>>> ajaxResponse = new AjaxResponse<>();
            ajaxResponse.setResult(data);
            return ajaxResponse;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        finally {
            disconnectionVm();
        }
    }
}
