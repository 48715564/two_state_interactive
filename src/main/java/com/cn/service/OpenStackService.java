package com.cn.service;

import com.cn.common.utils.OpenStackApi;
import com.cn.domain.entity.BusOpenstackLogs;
import com.cn.domain.entity.BusOpenstackLogsExample;
import com.cn.page.AjaxResponse;
import com.github.pagehelper.PageInfo;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.compute.SecurityGroup;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.ext.Hypervisor;
import org.openstack4j.model.compute.ext.HypervisorStatistics;
import org.openstack4j.model.network.Network;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by bozhou on 2017/12/18.
 */
@Service
public class OpenStackService {
    @Autowired
    OpenStackApi openStackApi;
    @Autowired
    private BusOpenstackLogsService busOpenstackLogsService;

    public BusOpenstackLogs getCountInfo(){
        BusOpenstackLogs jsonObject = new BusOpenstackLogs();
        OSClient.OSClientV3 osClientV3 = openStackApi.getAuthenticateUnscoped();
        HypervisorStatistics hypervisorStatistics =osClientV3.compute().hypervisors().statistics();
        jsonObject.setHypervisorCount(hypervisorStatistics.getCount());
        jsonObject.setRunningVmCount(hypervisorStatistics.getRunningVM());
        jsonObject.setDiskAvailableLeast(hypervisorStatistics.getLeastAvailableDisk());
        jsonObject.setFreeDisk(hypervisorStatistics.getFreeDisk());
        jsonObject.setLocal(hypervisorStatistics.getLocal());
        jsonObject.setLocalUsed(hypervisorStatistics.getLocalUsed());
        jsonObject.setFreeRam(hypervisorStatistics.getFreeRam());
        jsonObject.setMemory(hypervisorStatistics.getMemory());
        jsonObject.setMemoryUsed(hypervisorStatistics.getMemoryUsed());
        jsonObject.setVirtualCpu(hypervisorStatistics.getVirtualCPU());
        jsonObject.setVirtualUsedCpu(hypervisorStatistics.getVirtualUsedCPU());
        //网络个数
        List<? extends Network> netList = osClientV3.networking().network().list();
        Integer netWordCount = netList.size();
        jsonObject.setNetworkCount(netWordCount);
        jsonObject.setCreateTm(new Date());
        return jsonObject;
    }

    public AjaxResponse<BusOpenstackLogs> getInfo(){
        BusOpenstackLogs busOpenstackLogs = getCountInfo();
        OSClient.OSClientV3 osClientV3 = openStackApi.getAuthenticateUnscoped();
        List<? extends Hypervisor> hostList = osClientV3.compute().hypervisors().list();
        List vmList = osClientV3.compute().servers().list();
        //网络个数
        List<? extends Network> netList = osClientV3.networking().network().list();
        Map<String,Object> infoMap = new LinkedHashMap<>();
        infoMap.put("hostList",hostList);
        infoMap.put("vmList",vmList);
        infoMap.put("netList",netList);
        busOpenstackLogs.setInfoMap(infoMap);
        return new AjaxResponse(busOpenstackLogs);
    }
    public AjaxResponse getHostByID(String hostID) {
        AjaxResponse ajaxResponse = new AjaxResponse();
        OSClient.OSClientV3 osClientV3 = openStackApi.getAuthenticateUnscoped();
        Hypervisor hypervisor = osClientV3.compute().hypervisors().list().stream().filter(item->hostID.equals(item.getId())).findFirst().get();
        Map map = new LinkedHashMap();
        map.put("type","host");
        map.put("item",hypervisor);
        ajaxResponse.setResult(map);
        return ajaxResponse;
    }

    public AjaxResponse getVMByID(String vmID) {
        AjaxResponse ajaxResponse = new AjaxResponse();
        OSClient.OSClientV3 osClientV3 = openStackApi.getAuthenticateUnscoped();
        Server server = osClientV3.compute().servers().get(vmID);
        Map map = new LinkedHashMap();
        map.put("type","vm");
        map.put("item",server);
        ajaxResponse.setResult(map);
        return ajaxResponse;
    }

    public AjaxResponse<PageInfo<BusOpenstackLogs>> getInfoByPage(Integer page, Integer limit){
        BusOpenstackLogsExample busOpenstackLogsExample = new BusOpenstackLogsExample();
        busOpenstackLogsExample.setOrderByClause("create_tm desc");
        AjaxResponse<PageInfo<BusOpenstackLogs>> response = busOpenstackLogsService.findPage(page,limit,busOpenstackLogsExample);
        Collections.sort(response.getResult().getList(), Comparator.comparing(BusOpenstackLogs::getCreateTm));
       return response;
    }
}
